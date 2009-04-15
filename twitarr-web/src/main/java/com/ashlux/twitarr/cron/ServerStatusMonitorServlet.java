package com.ashlux.twitarr.cron;

import com.ashlux.twitarr.observer.observers.ServerStatusChangeObserver;
import com.ashlux.twitarr.publishers.TwitarrPublisher;
import com.ashlux.twitarr.publishers.twitter.TwitterPublisher;
import com.ashlux.twitarr.exception.TwitarrException;
import com.ashlux.potbs.potbs4j.services.server.ServerStatusService;
import com.ashlux.potbs.potbs4j.services.server.ServerStatusServiceImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

import org.apache.commons.io.IOUtils;

public class ServerStatusMonitorServlet
    extends HttpServlet
{
    @Override
    protected void doGet( HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse )
        throws ServletException, IOException
    {
        doPost( httpServletRequest, httpServletResponse );
    }

    @Override
    protected void doPost( HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse )
        throws ServletException, IOException
    {
//        if ( CronUtils.isCron( httpServletRequest ) )
//        {
//            throw new ServletException( "Should not be called by privileged user, only cron." );
//        }

        List<TwitarrPublisher> twitarrPublishers = new LinkedList<TwitarrPublisher>();
        twitarrPublishers.add( new TwitterPublisher( System.getProperty( "twitarr.twitter.username" ),
                                                     System.getProperty( "twitarr.twitter.password" ) ) );

        ServerStatusService serverStatusService =
            new ServerStatusServiceImpl( System.getProperty( "twitarr.potbs.apikey" ),
                                         System.getProperty( "twitarr.potbs.userid" ) );

        ServerStatusChangeObserver serverStatusChangeObserver = new ServerStatusChangeObserver();
        serverStatusChangeObserver.setPublishers( twitarrPublishers );
        serverStatusChangeObserver.setServerStatusService( serverStatusService );

        try
        {
            serverStatusChangeObserver.observe();
        }
        catch ( TwitarrException e )
        {
            throw new ServletException( e );
        }

        // everything went good
        httpServletResponse.setStatus( HttpServletResponse.SC_OK );
        IOUtils.write( "OK", httpServletResponse.getOutputStream() );
    }
}
