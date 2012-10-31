package org.hbird.parameterstorage.concurrent.registry;

import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.api.registry.SeriesFactory;
import org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries;

public class ConcurrentSeriesFactory<T> implements SeriesFactory<T> {

    /** @{inheritDoc . */
    @Override
    public ParameterValueSeries<T> create() {
        return new ConcurrentParameterValueSeries<T>();
    }
}
