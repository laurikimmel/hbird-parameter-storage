package org.hbird.parameterstorage.simple.query.filter;

import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.query.filter.Filter;

public class AndFilter<T> implements Filter<T> {

    protected final Filter<T>[] filters;

    /**
     * Creates new AndFilter.
     * 
     * @param first
     * @param second
     */
    public AndFilter(Filter<T>... filters) {
        this.filters = filters;
    }

    /** @{inheritDoc . */
    @Override
    public boolean accept(ParameterValueInTime<T> value) {
        for (Filter<T> filter : filters) {
            if (!filter.accept(value)) {
                return false;
            }
        }
        return true;
    }
}
