package com.ashlux.twitarr.observers;

import com.ashlux.twitarr.exception.TwitarrException;

public interface Observer
{
    void observe()
        throws TwitarrException;

}