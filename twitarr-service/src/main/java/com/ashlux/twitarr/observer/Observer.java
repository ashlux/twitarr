package com.ashlux.twitarr.observer;

import com.ashlux.twitarr.exception.TwitarrException;

public interface Observer
{
    void observe()
        throws TwitarrException;

}
