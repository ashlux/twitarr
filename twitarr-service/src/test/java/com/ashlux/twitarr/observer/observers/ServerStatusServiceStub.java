package com.ashlux.twitarr.observer.observers;

import com.ashlux.potbs.potbs4j.services.server.ServerStatusService;
import com.ashlux.potbs.potbs4j.exception.PotbsServiceException;
import com.ashlux.potbs4j.vo.ServerListDocument;
import com.ashlux.potbs4j.vo.ServerDocument;
import com.ashlux.potbs4j.vo.ServerName;

public class ServerStatusServiceStub
    implements ServerStatusService
{
    ServerListDocument serverListDocumentToReturn;

    public ServerStatusServiceStub( ServerListDocument serverListDocumentToReturn )
    {
        this.serverListDocumentToReturn = serverListDocumentToReturn;
    }

    @Override
    public ServerListDocument getAllServerStatuses()
        throws PotbsServiceException
    {
        return serverListDocumentToReturn;
    }

    @Override
    public ServerDocument getServerStatus( ServerName.Enum anEnum )
        throws PotbsServiceException
    {
        return null;
    }

    @Override
    public long getMinimumUpdateFrequency()
    {
        return 0;
    }
}

