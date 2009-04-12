package com.ashlux.twitarr.writer;

import com.ashlux.potbs4j.vo.ServerDocument;

public interface PotbsWriter
{
    public void publishServerStatusChange( ServerDocument.Server server )
        throws TwitarrWriterException;
}
