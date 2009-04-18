package com.ashlux.twitarr.observers;

import com.ashlux.twitarr.exception.TwitarrException;
import com.ashlux.twitarr.publishers.TwitarrPublisher;
import org.apache.xmlbeans.XmlObject;

import java.util.LinkedList;
import java.util.List;

abstract public class AbstractObserver
    implements Observer
{
    private static ObservationStorage observationStorage = new SimpleObservationStorage();

    private List<TwitarrPublisher> publishers = new LinkedList<TwitarrPublisher>();

    public final void observe()
        throws TwitarrException
    {
        XmlObject previousXmlObject = observationStorage.get( this.getClass() );
        XmlObject nextXmlObject = getNextObservation();
        doObservation( nextXmlObject, previousXmlObject );
        observationStorage.set( this.getClass(), nextXmlObject );
    }

    synchronized public List<TwitarrPublisher> getPublishers()
    {
        return publishers;
    }

    synchronized public void setPublishers( final List<TwitarrPublisher> publishers )
    {
        this.publishers = publishers;
    }

    /**
     * Observe and publish observations. Polling frequency determined
     * by <code>getPollTime</code>.
     *
     * @param previousXmlObject previous XML received by the PotBS service. Null if no known previous.
     */
    public abstract void doObservation( XmlObject nextXmlObject, XmlObject previousXmlObject )
        throws TwitarrException;

    /**
     * Gets the next observation with the appropriate service.
     *
     * @return XmlObject representing the next observation.
     * @throws TwitarrException
     */
    public abstract XmlObject getNextObservation()
        throws TwitarrException;
}
