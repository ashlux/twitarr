package com.ashlux.twitarr.observer;

import com.ashlux.twitarr.exception.TwitarrException;
import com.ashlux.twitarr.publishers.TwitarrPublisher;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

abstract public class AbstractObserver
    implements Observer
{
    private final static Logger log = LoggerFactory.getLogger( AbstractObserver.class );

    private List<TwitarrPublisher> publishers = new LinkedList<TwitarrPublisher>();

    // holds previous xmlobjects, persistance (at least until the app is out of memory)
    private static Map<Class, XmlObject> previousXmlObjects =
        Collections.synchronizedMap( new HashMap<Class, XmlObject>() );

    public final void observe()
        throws TwitarrException
    {
        try
        {
            XmlObject previousXmlObject = getPreviousObservation( this.getClass() );
            XmlObject currentXmlObject = doObservation( previousXmlObject );
            // everything went fine, so save off current observation as previous
            savePreviousObservation( this.getClass(), currentXmlObject );
        }
        catch ( IOException e )
        {
            throw new TwitarrException( e );
        }
        catch ( XmlException e )
        {
            throw new TwitarrException( e );
        }
    }


    public static XmlObject getPreviousObservation( Class clazz )
        throws IOException, XmlException
    {
        if ( log.isTraceEnabled() )
        {
            if ( previousXmlObjects.containsKey( clazz ) )
            {
                log.trace( "Previous observation for [" + clazz.getName() + "] exists." );
            }
            else
            {
                log.trace( "Previous observation for [" + clazz.getName() + "] does NOT exist." );
            }
        }

        return previousXmlObjects.get( clazz );
    }

    public void savePreviousObservation( Class clazz, XmlObject xmlObject )
        throws IOException, XmlException
    {
        if ( log.isTraceEnabled() )
        {
            log.trace( "Saving observation for [" + clazz.getName() + "]." );
        }

        previousXmlObjects.put( clazz, xmlObject );
    }

    /**
     * Observe and publish observations. Polling frequency determined
     * by <code>getPollTime</code>.
     *
     * @param previousXmlObject previous XML received by the PotBS service. Null if no known previous.
     * @return return current XmlObject received by the PotBS service.
     */
    public abstract XmlObject doObservation( XmlObject previousXmlObject )
        throws TwitarrException;

    synchronized public List<TwitarrPublisher> getPublishers()
    {
        return publishers;
    }

    synchronized public void setPublishers( final List<TwitarrPublisher> publishers )
    {
        this.publishers = publishers;
    }
}
