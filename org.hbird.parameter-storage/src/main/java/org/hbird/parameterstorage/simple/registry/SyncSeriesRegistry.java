package org.hbird.parameterstorage.simple.registry;

import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.api.registry.SeriesRegistry;
import org.hbird.parameterstorage.simple.SimpleCallbackWithValue;

public class SyncSeriesRegistry<K, T> {

    protected final SeriesRegistry<K, T> registry;

    /**
     * Creates new SyncSeriesRegistry.
     * 
     * @param registry
     */
    public SyncSeriesRegistry(SeriesRegistry<K, T> registry) {
        this.registry = registry;
    }

    /**
     * Returns registry.
     * 
     * @return the registry
     */
    public SeriesRegistry<K, T> getRegistry() {
        return registry;
    }

    public ParameterValueSeries<T> getOrGreate(K id) throws Exception {
        SimpleCallbackWithValue<ParameterValueSeries<T>> callback = new SimpleCallbackWithValue<ParameterValueSeries<T>>();
        registry.getOrCreate(id, callback);
        SimpleCallbackWithValue.verifyCallbackException(callback,
                        String.format("Failed to get ParameterValueSeries for id: %s", id));
        return callback.getValue();
    }

    public ParameterValueSeries<T> remove(K id) throws Exception {
        SimpleCallbackWithValue<ParameterValueSeries<T>> callback = new SimpleCallbackWithValue<ParameterValueSeries<T>>();
        registry.remove(id, callback);
        SimpleCallbackWithValue.verifyCallbackException(callback,
                        String.format("Failed to remove ParameterValueSeries for id: %s", id));
        return callback.getValue();
    }

    public Boolean containsKey(K id) throws Exception {
        SimpleCallbackWithValue<Boolean> callback = new SimpleCallbackWithValue<Boolean>();
        registry.containsKey(id, callback);
        SimpleCallbackWithValue.verifyCallbackException(callback,
                        String.format("Failed to check exitence of ParameterValueSeries for id: %s", id));
        return callback.getValue();
    }
}
