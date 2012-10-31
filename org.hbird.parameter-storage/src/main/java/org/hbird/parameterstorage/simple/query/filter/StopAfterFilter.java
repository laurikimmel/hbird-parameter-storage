package org.hbird.parameterstorage.simple.query.filter;

import org.hbird.parameterstorage.api.ParameterValueInTime;

public class StopAfterFilter<T> extends BeforeFilter<T> {

    /**
     * Creates new StopAfterFilter.
     * 
     * @param timeStamp
     */
    public StopAfterFilter(Long timeStamp) {
        super(timeStamp);
    }

    /** @{inheritDoc . */
    @Override
    public boolean accept(ParameterValueInTime<T> value) {
        return !super.accept(value);
    }
}
