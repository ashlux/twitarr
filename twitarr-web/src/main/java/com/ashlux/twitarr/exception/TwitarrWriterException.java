package com.ashlux.twitarr.exception;

import org.apache.xmlbeans.XmlObject;

public class TwitarrWriterException
    extends TwitarrException
{
    XmlObject xmlObject = null;

    public TwitarrWriterException( String message )
    {
        super( message );
    }

    public TwitarrWriterException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public TwitarrWriterException( String message, XmlObject xmlObject )
    {
        super( message );
        this.xmlObject = xmlObject;
    }

    public TwitarrWriterException( String message, XmlObject xmlObject, Throwable cause )
    {
        super( message, cause );
        this.xmlObject = xmlObject;
    }

    public XmlObject getXmlObject()
    {
        return xmlObject;
    }
}
