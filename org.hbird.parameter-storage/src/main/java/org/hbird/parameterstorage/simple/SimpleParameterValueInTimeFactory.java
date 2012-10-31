package org.hbird.parameterstorage.simple;

import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.ParameterValueInTimeFactory;

public class SimpleParameterValueInTimeFactory<T> implements ParameterValueInTimeFactory<T> {

    public SimpleParameterValueInTimeFactory() {
    }

    /** @{inheritDoc . */
    @Override
    public ParameterValueInTime<T> create(Long timeStamp, T value) throws IllegalArgumentException {
        return createNew(value, timeStamp);
    }

    protected ParameterValueInTime<T> createNew(T value, Long timestamp) {
        return new SimpleParameterValueInTime<T>(value, timestamp);
    }
}
