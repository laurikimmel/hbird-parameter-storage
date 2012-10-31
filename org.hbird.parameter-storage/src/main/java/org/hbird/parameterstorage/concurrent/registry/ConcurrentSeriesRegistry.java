package org.hbird.parameterstorage.concurrent.registry;

import java.util.HashMap;
import java.util.Map;

import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.api.registry.SeriesFactory;
import org.hbird.parameterstorage.api.registry.SeriesRegistry;


public class ConcurrentSeriesRegistry<K, T> implements SeriesRegistry<K, T> {

    protected final SeriesFactory<T> factory;
    protected final Map<K, ParameterValueSeries<T>> map = createRegistry();

    public ConcurrentSeriesRegistry(SeriesFactory<T> factory) {
        if (factory == null) {
            throw new IllegalArgumentException("SeriesFactory can't be null");
        }
        this.factory = factory;
    }

    /** @{inheritDoc . */
    @Override
    public void getOrCreate(K id, CallbackWithValue<ParameterValueSeries<T>> callback) {
        ParameterValueSeries<T> series;
        synchronized (map) {
            series = map.get(id);
            if (series == null) {
                series = create(id, factory, map);
            }
        }
        callback.onValue(series);
    }

    /** @{inheritDoc . */
    @Override
    public void remove(K id, CallbackWithValue<ParameterValueSeries<T>> callback) {
        ParameterValueSeries<T> series;
        synchronized (map) {
            series = map.remove(id);
        }
        callback.onValue(series);
    }

    ParameterValueSeries<T> create(K id, SeriesFactory<T> factory, Map<K, ParameterValueSeries<T>> map) {
        ParameterValueSeries<T> series = factory.create();
        map.put(id, series);
        return series;
    }

    protected Map<K, ParameterValueSeries<T>> createRegistry() {
        return new HashMap<K, ParameterValueSeries<T>>();
    }
}
