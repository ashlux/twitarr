package com.ashlux.twitarr.observer.observers;

import com.ashlux.potbs4j.vo.ServerDocument;
import com.ashlux.potbs4j.vo.ServerListDocument;
import com.ashlux.potbs4j.vo.ServerName;
import com.ashlux.potbs4j.vo.ServerStatus;
import com.ashlux.twitarr.exception.TwitarrException;
import com.ashlux.twitarr.publishers.TwitarrPublisher;
import org.apache.xmlbeans.XmlObject;
import static org.testng.AssertJUnit.*;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class ServerStatusChangeObserverTest
{
    @Test
    public void testNoPreviousUpdates_noServerStatusChangesArePublished()
        throws TwitarrException
    {
        ServerStatusServiceStub serverStatusServiceStub =
            new ServerStatusServiceStub( serverListDocument_AntiguaOnline_BlackbeardOnline );
        ServerStatusChangeObserver serverStatusChangeObserver = new ServerStatusChangeObserver();
        serverStatusChangeObserver.setServerStatusService( serverStatusServiceStub );
        TwitarrPublisherStub twitarrPublisherStub = new TwitarrPublisherStub();
        List<TwitarrPublisher> twitarrPublishers = new LinkedList<TwitarrPublisher>();
        twitarrPublishers.add( twitarrPublisherStub );
        serverStatusChangeObserver.setPublishers( twitarrPublishers );

        XmlObject currentXmlObject = serverStatusChangeObserver.doObservation( null );

        assertNotNull( currentXmlObject );
        assertSame( currentXmlObject, serverListDocument_AntiguaOnline_BlackbeardOnline );
        assertEquals( 0, twitarrPublisherStub.getServersUpdated().size() );
    }
                          
    @Test
    public void testAntiguaWentOffline_onlyPublishAntiguaGoingOffline()
        throws TwitarrException
    {
        ServerStatusServiceStub serverStatusServiceStub =
            new ServerStatusServiceStub( serverListDocument_AntiguaOffline_BlackbeardOnline );
        ServerStatusChangeObserver serverStatusChangeObserver = new ServerStatusChangeObserver();
        serverStatusChangeObserver.setServerStatusService( serverStatusServiceStub );
        TwitarrPublisherStub twitarrPublisherStub = new TwitarrPublisherStub();
        List<TwitarrPublisher> twitarrPublishers = new LinkedList<TwitarrPublisher>();
        twitarrPublishers.add( twitarrPublisherStub );
        serverStatusChangeObserver.setPublishers( twitarrPublishers );

        XmlObject currentXmlObject =
            serverStatusChangeObserver.doObservation( serverListDocument_AntiguaOnline_BlackbeardOnline );

        assertNotNull( currentXmlObject );
        assertSame( currentXmlObject, serverListDocument_AntiguaOffline_BlackbeardOnline );
        assertEquals( 1, twitarrPublisherStub.getServersUpdated().size() );
        assertEquals( ServerName.ANTIGUA, twitarrPublisherStub.getServersUpdated().get( 0 ).getName() );
        assertEquals( ServerStatus.OFFLINE, twitarrPublisherStub.getServersUpdated().get( 0 ).getStatus() );
    }

    @Test
    public void testAntiguaWentOfflineButLastUpdatedTimeTooFarApart_noServerStatusChangesArePublished()
        throws TwitarrException
    {
        ServerStatusServiceStub serverStatusServiceStub =
            new ServerStatusServiceStub( serverListDocument_AntiguaOffline_BlackbeardOnline );
        ServerStatusChangeObserver serverStatusChangeObserver = new ServerStatusChangeObserver();
        serverStatusChangeObserver.setServerStatusService( serverStatusServiceStub );
        TwitarrPublisherStub twitarrPublisherStub = new TwitarrPublisherStub();
        List<TwitarrPublisher> twitarrPublishers = new LinkedList<TwitarrPublisher>();
        twitarrPublishers.add( twitarrPublisherStub );
        serverStatusChangeObserver.setPublishers( twitarrPublishers );
        ServerListDocument serverListDocument_AntiguaOnline_BlackbeardOnline =
            ServerStatusChangeObserverTest.serverListDocument_AntiguaOnline_BlackbeardOnline;
        Calendar lastUpdated6HoursAgoCalendar = Calendar.getInstance();
        lastUpdated6HoursAgoCalendar.add( Calendar.HOUR, -6 );
        lastUpdated6HoursAgoCalendar.add( Calendar.MINUTE, -1 );
        serverListDocument_AntiguaOnline_BlackbeardOnline.getServerList().setUpdated( lastUpdated6HoursAgoCalendar );

        XmlObject currentXmlObject =
            serverStatusChangeObserver.doObservation( serverListDocument_AntiguaOnline_BlackbeardOnline );

        assertNotNull( currentXmlObject );
        assertSame( currentXmlObject, serverListDocument_AntiguaOffline_BlackbeardOnline );
        assertEquals( 0, twitarrPublisherStub.getServersUpdated().size() );
    }

    public static ServerListDocument serverListDocument_AntiguaOnline_BlackbeardOnline =
        ServerListDocument.Factory.newInstance();

    public static ServerListDocument serverListDocument_AntiguaOffline_BlackbeardOnline =
        ServerListDocument.Factory.newInstance();

    static
    {
        ServerListDocument.ServerList serverList = serverListDocument_AntiguaOnline_BlackbeardOnline.addNewServerList();
        serverList.setUpdated( Calendar.getInstance() );
        ServerDocument.Server antigua = serverList.addNewServer();
        antigua.setName( ServerName.ANTIGUA );
        antigua.setStatus( ServerStatus.ONLINE );
        ServerDocument.Server blackbeard = serverList.addNewServer();
        blackbeard.setName( ServerName.BLACKBEARD );
        blackbeard.setStatus( ServerStatus.ONLINE );
    }

    static
    {
        ServerListDocument.ServerList serverList =
            serverListDocument_AntiguaOffline_BlackbeardOnline.addNewServerList();
        serverList.setUpdated( Calendar.getInstance() );
        ServerDocument.Server antigua = serverList.addNewServer();
        antigua.setName( ServerName.ANTIGUA );
        antigua.setStatus( ServerStatus.OFFLINE );
        ServerDocument.Server blackbeard = serverList.addNewServer();
        blackbeard.setName( ServerName.BLACKBEARD );
        blackbeard.setStatus( ServerStatus.ONLINE );
    }
}
