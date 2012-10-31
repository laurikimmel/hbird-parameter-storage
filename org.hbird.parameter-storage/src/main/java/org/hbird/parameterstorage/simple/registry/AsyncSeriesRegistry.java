package org.hbird.parameterstorage.simple.registry;

import java.util.concurrent.ExecutorService;

import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.api.registry.SeriesRegistry;


public class AsyncSeriesRegistry<K, T> implements SeriesRegistry<K, T> {

    protected final SeriesRegistry<K, T> registry;
    protected final ExecutorService execService;

    /**
     * Creates new AsyncSeriesRegistry.
     * 
     * @param registry
     * @param execService
     */
    public AsyncSeriesRegistry(SeriesRegistry<K, T> registry, ExecutorService execService) {
        this.registry = registry;
        this.execService = execService;
    }

    /** @{inheritDoc . */
    @Override
    public void getOrCreate(K id, CallbackWithValue<ParameterValueSeries<T>> callback) {
        execService.submit(createGetOrCreateTask(registry, id, callback));
    }

    /** @{inheritDoc . */
    @Override
    public void remove(K id, CallbackWithValue<ParameterValueSeries<T>> callback) {
        execService.submit(createRemoveTask(registry, id, callback));
    }

    protected Runnable createGetOrCreateTask(final SeriesRegistry<K, T> registry, final K id,
            final CallbackWithValue<ParameterValueSeries<T>> callback) {
        return new Runnable() {
            @Override
            public void run() {
                registry.getOrCreate(id, callback);
            }
        };
    }

    protected Runnable createRemoveTask(final SeriesRegistry<K, T> registry, final K id,
            final CallbackWithValue<ParameterValueSeries<T>> callback) {
        return new Runnable() {
            @Override
            public void run() {
                registry.remove(id, callback);
            }
        };
    }
}
