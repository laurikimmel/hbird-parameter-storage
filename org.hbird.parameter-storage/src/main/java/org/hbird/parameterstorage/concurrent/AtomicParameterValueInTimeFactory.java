package org.hbird.parameterstorage.concurrent;

import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.simple.SimpleParameterValueInTimeFactory;

public class AtomicParameterValueInTimeFactory<T> extends SimpleParameterValueInTimeFactory<T> {

    /** @{inheritDoc . */
    @Override
    protected ParameterValueInTime<T> createNew(T value, Long timestamp) {
        return new AtomicParameterValueInTime<T>(value, timestamp);
    }
}
