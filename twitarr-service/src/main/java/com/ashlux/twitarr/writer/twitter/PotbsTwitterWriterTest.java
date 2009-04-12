package com.ashlux.twitarr.writer.twitter;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import com.ashlux.twitarr.writer.TwitarrWriterException;
import com.ashlux.potbs4j.vo.ServerDocument;
import com.ashlux.potbs4j.vo.ServerName;
import com.ashlux.potbs4j.vo.ServerStatus;

public class PotbsTwitterWriterTest
{
    @Test
    public void test_publishServerStatusChange_offline()
        throws TwitarrWriterException
    {
        ServerDocument.Server server = ServerDocument.Server.Factory.newInstance();
        server.setName( ServerName.ANTIGUA );
        server.setStatus( ServerStatus.OFFLINE );

        PotbsTwitterWriterStub potbsTwitterWriterStub = new PotbsTwitterWriterStub( "id", "password" );
        potbsTwitterWriterStub.publishServerStatusChange( server );

        assertEquals( potbsTwitterWriterStub.publishedStatus, "Server #antigua has gone OFFLINE. #potbs" );
    }

    @Test
    public void test_publishServerStatusChange_online()
        throws TwitarrWriterException
    {
        ServerDocument.Server server = ServerDocument.Server.Factory.newInstance();
        server.setName( ServerName.ANTIGUA );
        server.setStatus( ServerStatus.ONLINE );

        PotbsTwitterWriterStub potbsTwitterWriterStub = new PotbsTwitterWriterStub( "id", "password" );
        potbsTwitterWriterStub.publishServerStatusChange( server );

        assertEquals( potbsTwitterWriterStub.publishedStatus, "Server #antigua is back ONLINE. #potbs" );
    }

    @Test
    public void test_publishServerStatusChange_locked()
        throws TwitarrWriterException
    {
        ServerDocument.Server server = ServerDocument.Server.Factory.newInstance();
        server.setName( ServerName.ANTIGUA );
        server.setStatus( ServerStatus.LOCKED );

        PotbsTwitterWriterStub potbsTwitterWriterStub = new PotbsTwitterWriterStub( "id", "password" );
        potbsTwitterWriterStub.publishServerStatusChange( server );

        assertEquals( potbsTwitterWriterStub.publishedStatus, "Server #antigua is now LOCKED. #potbs" );
    }

    @Test(expectedExceptions = TwitarrWriterException.class)
    public void test_publishServerStatusChange_unknownStatus()
        throws TwitarrWriterException
    {
        ServerDocument.Server server = ServerDocument.Server.Factory.newInstance();
        server.setName( ServerName.ANTIGUA );
        server.setStatus( null );

        PotbsTwitterWriterStub potbsTwitterWriterStub = new PotbsTwitterWriterStub( "id", "password" );
        potbsTwitterWriterStub.publishServerStatusChange( server );

        assertEquals( potbsTwitterWriterStub.publishedStatus, "Server #antigua is back ONLINE. #potbs" );
    }

    public class PotbsTwitterWriterStub
        extends PotbsTwitterWriter
    {
        public String publishedStatus = null;

        public PotbsTwitterWriterStub( String id, String password )
        {
            super( id, password );
        }

        @Override
        protected void publishMessage( String statusString )
            throws TwitarrWriterException
        {
            // override so we don't actually publish a twitter message
            this.publishedStatus = statusString;
        }
    }
}
