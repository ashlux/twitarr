package com.ashlux.twitarr.publishers;

import com.ashlux.potbs4j.vo.ServerDocument;
import com.ashlux.twitarr.exception.TwitarrWriterException;

public interface TwitarrPublisher
{
    public void publishServerStatusChange( ServerDocument.Server server )
        throws TwitarrWriterException;
}
