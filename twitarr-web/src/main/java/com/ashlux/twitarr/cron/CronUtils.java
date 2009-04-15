package com.ashlux.twitarr.cron;

import javax.servlet.http.HttpServletRequest;

public class CronUtils
{
    public static boolean isCron( HttpServletRequest httpServletRequest )
    {
        return "true".equals( httpServletRequest.getHeader( "X-AppEngine-Cron" ) );
    }
}
