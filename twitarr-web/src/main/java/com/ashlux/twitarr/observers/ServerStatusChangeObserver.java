package com.ashlux.twitarr.observers;

import com.ashlux.potbs.potbs4j.exception.PotbsServiceException;
import com.ashlux.potbs.potbs4j.services.server.ServerStatusService;
import com.ashlux.potbs4j.vo.ServerDocument;
import com.ashlux.potbs4j.vo.ServerListDocument;
import com.ashlux.potbs4j.vo.ServerName;
import com.ashlux.potbs4j.vo.ServerStatus;
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
public class ServerStatusChangeObserver
    extends AbstractObserver
{
    private final static Logger log = LoggerFactory.getLogger( ServerStatusChangeObserver.class );

    private ServerStatusService serverStatusService;

    @Override
    public void doObservation( XmlObject nextXmlObject, XmlObject previousXmlObject )
        throws TwitarrException
    {
        ServerListDocument currentServerListDocument = (ServerListDocument) nextXmlObject;
        ServerListDocument previousServerListDocument = (ServerListDocument) previousXmlObject;

        // nothing to compare, so can't determine server status changes
        if ( previousServerListDocument == null )
        {
            log.trace( "No previous server list found, so no notifications will be sent out." );
            return;
        }

        // if too much time since last update, do not bother publishing server statuses
        if ( tooMuchTimeSinceLastUpdate( currentServerListDocument.getServerList().getUpdated(),
                                         previousServerListDocument.getServerList().getUpdated() ) )
        {
            log.trace( "Too much time since checked for server status change, so no notfications will be sent out." );
            return;
        }

        // look for status changes and publish them
        publishServerStatusChanges( currentServerListDocument, previousServerListDocument );
    }

    @Override
    public XmlObject getNextObservation()
        throws TwitarrException
    {
        try
        {
            return serverStatusService.getAllServerStatuses();
        }
        catch ( PotbsServiceException e )
        {
            throw new TwitarrException( e );
        }
    }

    private void publishServerStatusChanges( ServerListDocument currentServerListDocument,
                                             ServerListDocument previousServerListDocument )
        throws TwitarrWriterException
    {
        for ( ServerDocument.Server server : currentServerListDocument.getServerList().getServerArray() )
        {
            ServerStatus.Enum currentServerStatus = server.getStatus();
            ServerStatus.Enum previousServerStatus =
                getPreviousObservedServerStatus( previousServerListDocument, server.getName() );

            if ( !currentServerStatus.equals( previousServerStatus ) )
            {
                if ( log.isDebugEnabled() )
                {
                    log.debug( "Server's [" + server.getName() + "] status has changed from [" + previousServerStatus +
                        "] to [" + currentServerStatus + "].  Publishing server status change." );
                }
                publishServerStatusChange( server );
            }
        }
    }

    private boolean tooMuchTimeSinceLastUpdate( Calendar mostRecentUpdateOn, Calendar previouslyUpdatedOn )
    {
        // has it been over 6 hours since last update?
        long timeSinceLastUpdateInMs = mostRecentUpdateOn.getTimeInMillis() - previouslyUpdatedOn.getTimeInMillis();
        return timeSinceLastUpdateInMs > 21600000;
    }

    private void publishServerStatusChange( ServerDocument.Server server )
        throws TwitarrWriterException
    {
        for ( com.ashlux.twitarr.publishers.TwitarrPublisher twitarrPublisher : getPublishers() )
        {
            twitarrPublisher.publishServerStatusChange( server );
        }
    }

    private ServerStatus.Enum getPreviousObservedServerStatus( ServerListDocument serverListDocument,
                                                               ServerName.Enum searchForServerName )
    {
        for ( ServerDocument.Server server : serverListDocument.getServerList().getServerArray() )
        {
            if ( server.getName().equals( searchForServerName ) )
            {
                return server.getStatus();
            }
        }
        return null;
    }

    public void setServerStatusService( ServerStatusService serverStatusService )
    {
        this.serverStatusService = serverStatusService;
    }
}
