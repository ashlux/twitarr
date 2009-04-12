package com.ashlux.twitarr.writer;

public class TwitarrWriterException
    extends Exception
{
    public TwitarrWriterException()
    {
    }

    public TwitarrWriterException( String message )
    {
        super( message );
    }

    public TwitarrWriterException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public TwitarrWriterException( Throwable cause )
    {
        super( cause );
    }
}
