package org.hbird.parameterstorage.simple.query.filter;

import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.query.filter.Filter;

public class AcceptAllFilter<T> implements Filter<T> {

    /** @{inheritDoc . */
    @Override
    public boolean accept(ParameterValueInTime<T> value) {
        return true;
    }
}
