package com.ashlux.twitarr.observer.observers;

import com.ashlux.twitarr.publishers.TwitarrPublisher;
import com.ashlux.twitarr.exception.TwitarrWriterException;
import com.ashlux.potbs4j.vo.ServerDocument;

import java.util.List;
import java.util.LinkedList;

public class TwitarrPublisherStub
    implements TwitarrPublisher
{
    private List<ServerDocument.Server> serversUpdated = new LinkedList<ServerDocument.Server>();

    @Override
    public void publishServerStatusChange( ServerDocument.Server server )
        throws TwitarrWriterException
    {
        serversUpdated.add( server );
    }

    public List<ServerDocument.Server> getServersUpdated()
    {
        return serversUpdated;
    }
}
