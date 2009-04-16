package com.ashlux.twitarr.twitter;

import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.OutputStream;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterStatusChangeServlet
    extends HttpServlet
{
    protected void doPost( HttpServletRequest request,
                           HttpServletResponse response )
        throws ServletException, IOException
    {
        String status = request.getParameter( "status" );
        String username = request.getParameter( "username" );
        String password = request.getParameter( "password" );

        Twitter twitter = new Twitter();
        twitter.setUserId( username );
        twitter.setPassword( password );
        try
        {
            twitter.update( status );
        }
        catch ( TwitterException e )
        {
            e.printStackTrace();
            OutputStream outputStream = response.getOutputStream();
            IOUtils.write( "FAILED to changed status. :'(", outputStream );
            IOUtils.closeQuietly( outputStream );
        }

        OutputStream outputStream = response.getOutputStream();
        IOUtils.write( "Successfully changed status.", outputStream );
        IOUtils.closeQuietly( outputStream );
    }

    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response )
        throws ServletException, IOException
    {
        OutputStream outputStream = response.getOutputStream();
        IOUtils.write( "GET not supported, must send POST.", outputStream );
        IOUtils.closeQuietly( outputStream );
    }
}
