package com.ashlux.twitarr.cron;

import com.ashlux.potbs.potbs4j.services.landmark.LandmarkStatusService;
import com.ashlux.potbs.potbs4j.services.landmark.LandmarkStatusServiceImpl;
import com.ashlux.potbs4j.vo.ServerName;
import com.ashlux.twitarr.exception.TwitarrException;
import com.ashlux.twitarr.observers.PortStatusChangeObserver;
import com.ashlux.twitarr.publishers.TwitarrPublisher;
import com.ashlux.twitarr.publishers.TwitterPublisher;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PortStateMonitorServlet
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
        List<TwitarrPublisher> twitarrPublishers = new LinkedList<TwitarrPublisher>();
        twitarrPublishers.add( new TwitterPublisher( System.getProperty( "twitarr.twitter.username" ),
                                                     System.getProperty( "twitarr.twitter.password" ) ) );

        LandmarkStatusService landmarkStatusService =
            new LandmarkStatusServiceImpl( System.getProperty( "twitarr.potbs.apikey" ),
                                           System.getProperty( "twitarr.potbs.userid" ) );

        PortStatusChangeObserver portStatusChangeObserver = new PortStatusChangeObserver();
        portStatusChangeObserver.setPublishers( twitarrPublishers );
        portStatusChangeObserver.setLandmarkStatusService( landmarkStatusService );
        portStatusChangeObserver.setServerName( ServerName.ANTIGUA );

        try
        {
            portStatusChangeObserver.observe();
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
