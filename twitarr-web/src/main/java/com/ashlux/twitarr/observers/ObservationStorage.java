package com.ashlux.twitarr.observers;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlException;

import java.io.IOException;

import com.ashlux.twitarr.exception.TwitarrException;

public interface ObservationStorage
{
    public XmlObject get( Class clazz )
        throws TwitarrException;

    public void set( Class clazz, XmlObject xmlObject )
        throws TwitarrException;
}
