package org.hbird.parameterstorage.api.registry;

import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.api.ParameterValueSeries;

public interface SeriesRegistry<K, T> {

    public void getOrCreate(K id, CallbackWithValue<ParameterValueSeries<T>> callback);

    public void remove(K id, CallbackWithValue<ParameterValueSeries<T>> callback);

}
