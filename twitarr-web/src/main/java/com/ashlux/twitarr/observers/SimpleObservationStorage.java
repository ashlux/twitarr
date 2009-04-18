package com.ashlux.twitarr.observers;

import com.ashlux.twitarr.exception.TwitarrException;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SimpleObservationStorage
    implements ObservationStorage
{
    private final static Logger log = LoggerFactory.getLogger( SimpleObservationStorage.class );

    private Map<Class, XmlObject> previousXmlObjects =
        Collections.synchronizedMap( new HashMap<Class, XmlObject>() );

    public XmlObject get( Class clazz )
        throws TwitarrException
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

    public void set( Class clazz, XmlObject xmlObject )
        throws TwitarrException
    {
        if ( log.isTraceEnabled() )
        {
            log.trace( "Saving observation for [" + clazz.getName() + "]." );
        }

        previousXmlObjects.put( clazz, xmlObject );
    }

}
