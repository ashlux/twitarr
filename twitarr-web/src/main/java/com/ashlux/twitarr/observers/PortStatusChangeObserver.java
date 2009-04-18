package com.ashlux.twitarr.observers;

import com.ashlux.potbs.potbs4j.exception.PotbsServiceException;
import com.ashlux.potbs.potbs4j.services.server.ServerStatusService;
import com.ashlux.potbs.potbs4j.services.landmark.LandmarkStatusService;
import com.ashlux.potbs4j.vo.PortDocument;
import com.ashlux.potbs4j.vo.PortListDocument;
import com.ashlux.potbs4j.vo.PortName;
import com.ashlux.potbs4j.vo.PortState;
import com.ashlux.potbs4j.vo.ServerName;
import com.ashlux.twitarr.exception.TwitarrException;
import com.ashlux.twitarr.exception.TwitarrWriterException;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * Monitors server status and notifies all of its publishers when a server's
 * status has changed.  <code>ServerStatusChangeObserver</code> is responsible
 * for determining if a server's status has (likely) changed.
 * <p/>
 * A server's status has likely changed if server's status has changed since
 * the last check, the last check is known to have completed, and the last
 * check was less than an hour ago.
 */
public class PortStatusChangeObserver
    extends AbstractObserver
{
    private final static Logger log = LoggerFactory.getLogger( PortStatusChangeObserver.class );

    private ServerName.Enum serverName;
    private LandmarkStatusService landmarkStatusService;

    @Override
    public void doObservation( XmlObject nextXmlObject, XmlObject previousXmlObject )
        throws TwitarrException
    {
        PortListDocument currentPortListDocument = (PortListDocument) nextXmlObject;
        PortListDocument previousPortListDocument = (PortListDocument) previousXmlObject;

        // nothing to compare, so can't determine server status changes
        if ( previousPortListDocument == null )
        {
            log.trace( "No previous port list found, so no notifications will be sent out." );
            return;
        }

        // if too much time since last update, do not bother publishing server statuses
        if ( tooMuchTimeSinceLastUpdate( currentPortListDocument.getPortList().getUpdated(),
                                         previousPortListDocument.getPortList().getUpdated() ) )
        {
            log.trace( "Too much time since checked for port status change, so no notfications will be sent out." );
            return;
        }

        // look for status changes and publish them
        publishServerStatusChanges( currentPortListDocument, previousPortListDocument );
    }

    @Override
    public XmlObject getNextObservation()
        throws TwitarrException
    {
        try
        {
            return landmarkStatusService.getAllLandmarkStatuses( serverName );
        }
        catch ( PotbsServiceException e )
        {
            throw new TwitarrException( e );
        }
    }

    private void publishServerStatusChanges( PortListDocument currentServerListDocument,
                                             PortListDocument previousPortListDocument )
        throws TwitarrWriterException
    {
        for ( PortDocument.Port currentPort : currentServerListDocument.getPortList().getPortArray() )
        {
            PortDocument.Port previousPort = getPreviousObservedPort( previousPortListDocument, currentPort.getName() );
            PortState.Enum currentPortState = currentPort.getPortState();
            PortState.Enum previousPortState = previousPort.getPortState();

            if ( !currentPortState.equals( previousPortState ) )
            {
                if ( log.isDebugEnabled() )
                {
                    log.debug(
                        "Port's [" + currentPort.getName() + "] status has changed from [" + previousPortState + "] to [" +
                            currentPortState + "].  Publishing port status change." );
                }
                publishPortStateChange( previousPort, currentPort );
            }
        }
    }

    private boolean tooMuchTimeSinceLastUpdate( Calendar mostRecentUpdateOn, Calendar previouslyUpdatedOn )
    {
        // has it been over 6 hours since last update?
        long timeSinceLastUpdateInMs = mostRecentUpdateOn.getTimeInMillis() - previouslyUpdatedOn.getTimeInMillis();
        return timeSinceLastUpdateInMs > 21600000;
    }

    private void publishPortStateChange( PortDocument.Port previousPort, PortDocument.Port currentPort )
        throws TwitarrWriterException
    {
        for ( com.ashlux.twitarr.publishers.TwitarrPublisher twitarrPublisher : getPublishers() )
        {
            twitarrPublisher.publishPortStatusChange( previousPort, currentPort );
        }
    }

    private PortDocument.Port getPreviousObservedPort( PortListDocument portListDocument,
                                                    PortName.Enum searchForPortName )
    {
        for ( PortDocument.Port port : portListDocument.getPortList().getPortArray() )
        {
            if ( port.getName().equals( searchForPortName ) )
            {
                return port;
            }
        }
        return null;
    }

    public void setServerName( ServerName.Enum serverName )
    {
        this.serverName = serverName;
    }

    public void setLandmarkStatusService( LandmarkStatusService landmarkStatusService )
    {
        this.landmarkStatusService = landmarkStatusService;
    }
}