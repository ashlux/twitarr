package com.ashlux.twitarr.publishers.twitter;

import com.ashlux.twitarr.exception.TwitarrWriterException;
import com.ashlux.potbs4j.vo.ServerDocument;
import com.ashlux.potbs4j.vo.ServerStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class TwitterPublisher
    implements com.ashlux.twitarr.publishers.TwitarrPublisher
{
    final Logger log = LoggerFactory.getLogger( TwitterPublisher.class );

    private String id;

    private String password;

    public TwitterPublisher( String id, String password )
    {
        this.id = id;
        this.password = password;
    }

    @Override
    public void publishServerStatusChange( ServerDocument.Server currentServerStatus )
        throws TwitarrWriterException
    {
        if ( log.isTraceEnabled() )
        {
            log.debug( "Publishing to twitter server status change for [" + currentServerStatus.getName() +
                "].  Status is now [" + currentServerStatus.getStatus() + "]." );
        }

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
            throw new TwitarrWriterException(
                "Server status [" + currentServerStatus.getStatus() + "] is unkown.  PotBS API changed?",
                currentServerStatus );
        }
    }

    protected void publishMessage( String statusString )
        throws TwitarrWriterException
    {
        if ( log.isTraceEnabled() )
        {
            log.trace( "Publishing [" + statusString + "] using twitter username [" + id +
                "] (password intentionally supressed)." );
        }

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
