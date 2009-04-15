package com.ashlux.twitarr.exception;

public class TwitarrException extends Exception
{
    public TwitarrException()
    {
    }

    public TwitarrException( String message )
    {
        super( message );
    }

    public TwitarrException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public TwitarrException( Throwable cause )
    {
        super( cause );
    }
}
