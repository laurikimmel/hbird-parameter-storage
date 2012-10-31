package org.hbird.parameterstorage.simple.query;

import java.util.concurrent.atomic.AtomicLong;

import org.hbird.parameterstorage.api.ParameterValueInTime;


public class QueryCounterCallback<T> extends QueryCallbackAdapter<T> {

    private final AtomicLong counter = new AtomicLong();

    /**
     * Returns counter.
     * 
     * @return the counter
     */
    public long getCount() {
        return counter.get();
    }

    /** @{inheritDoc . */
    @Override
    public void onValue(ParameterValueInTime<T> value) {
        counter.incrementAndGet();
    }
}
