package com.ashlux.twitarr.writer.twitter;

import com.ashlux.twitarr.writer.PotbsWriter;
import com.ashlux.twitarr.writer.TwitarrWriterException;
import com.ashlux.potbs4j.vo.ServerDocument;
import com.ashlux.potbs4j.vo.ServerStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class PotbsTwitterWriter
    implements PotbsWriter
{
    private String id;

    private String password;

    public PotbsTwitterWriter( String id, String password )
    {
        this.id = id;
        this.password = password;
    }

    @Override
    public void publishServerStatusChange( ServerDocument.Server currentServerStatus )
        throws TwitarrWriterException
    {
        if ( ServerStatus.OFFLINE.equals( currentServerStatus.getStatus() ) )
        {
            publishMessage( "Server #" + currentServerStatus.getName() + " has gone OFFLINE. #potbs" );
        }
        else if ( ServerStatus.ONLINE.equals( currentServerStatus.getStatus() ) )
        {
            publishMessage( "Server #" + currentServerStatus.getName() + " is back ONLINE. #potbs" );
        }
        else if ( ServerStatus.LOCKED.equals( currentServerStatus.getStatus() ) )
        {
            publishMessage( "Server #" + currentServerStatus.getName() + " is now LOCKED. #potbs" );
        }
        else
        {
            throw new TwitarrWriterException( "Unknown server status, xml=[" + currentServerStatus.xmlText() + "]." );
        }
    }

    protected void publishMessage( String statusString )
        throws TwitarrWriterException
    {
        Twitter twitter = new Twitter( id, password );
        try
        {
            twitter.update( statusString );
        }
        catch ( TwitterException e )
        {
            throw new TwitarrWriterException( "Could not update twitter status.", e );
        }
    }

}
