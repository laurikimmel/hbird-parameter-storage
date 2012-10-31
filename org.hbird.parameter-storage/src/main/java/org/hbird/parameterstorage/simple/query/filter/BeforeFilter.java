package org.hbird.parameterstorage.simple.query.filter;

import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.query.filter.Filter;

public class BeforeFilter<T> implements Filter<T> {

    protected final Long before;

    public BeforeFilter(Long before) {
        this.before = before;
    }

    /** @{inheritDoc . */
    @Override
    public boolean accept(ParameterValueInTime<T> value) {
        return value.getTimeStamp() < before;
    }
}
