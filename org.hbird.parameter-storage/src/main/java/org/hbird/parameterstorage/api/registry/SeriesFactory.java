package org.hbird.parameterstorage.api.registry;

import org.hbird.parameterstorage.api.ParameterValueSeries;

public interface SeriesFactory<T> {

    public ParameterValueSeries<T> create();
}
