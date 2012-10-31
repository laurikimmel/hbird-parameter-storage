package org.hbird.parameterstorage.simple.registry;

import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.api.registry.SeriesFactory;
import org.hbird.parameterstorage.simple.SimpleParameterValueSeries;

public class SimpleSeriesFactory<T> implements SeriesFactory<T> {

    /** @{inheritDoc . */
    @Override
    public ParameterValueSeries<T> create() {
        return new SimpleParameterValueSeries<T>();
    }
}
