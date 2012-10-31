package org.hbird.parameterstorage.simple;

import java.util.concurrent.ExecutorService;

import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.api.CrudSupport;
import org.hbird.parameterstorage.api.ParameterValueInTime;


public class AsyncCrudSupport<T> implements CrudSupport<T> {

    private final CrudSupport<T> crud;
    private final ExecutorService execService;

    /**
     * Creates new AsyncCrudSupport.
     * 
     * @param crud
     * @param execService
     */
    public AsyncCrudSupport(CrudSupport<T> crud, ExecutorService execService) {
        this.crud = crud;
        this.execService = execService;
    }

    /**
     * Returns crud.
     * 
     * @return the crud
     */
    public CrudSupport<T> getCrud() {
        return crud;
    }

    /**
     * Returns execService.
     * 
     * @return the execService
     */
    public ExecutorService getExecService() {
        return execService;
    }

    /** @{inheritDoc . */
    @Override
    public void add(final Long timeStamp, final T value, final CallbackWithValue<ParameterValueInTime<T>> callback) {
        try {
            execService.submit(createAddTask(crud, timeStamp, value, callback));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /** @{inheritDoc . */
    @Override
    public void get(Long timeStamp, CallbackWithValue<ParameterValueInTime<T>> callback) {
        execService.submit(createGetTask(crud, timeStamp, callback));
    }

    /** @{inheritDoc . */
    @Override
    public void update(Long timeStamp, T value, CallbackWithValue<ParameterValueInTime<T>> callback) {
        execService.submit(createUpdateTask(crud, timeStamp, value, callback));
    }

    /** @{inheritDoc . */
    @Override
    public void delete(Long timeStamp, CallbackWithValue<ParameterValueInTime<T>> callback) {
        execService.submit(createDeleteTask(crud, timeStamp, callback));
    }

    protected Runnable createAddTask(final CrudSupport<T> crud, final Long timeStamp, final T value,
            final CallbackWithValue<ParameterValueInTime<T>> callback) {
        return new Runnable() {
            @Override
            public void run() {
                crud.add(timeStamp, value, callback);
            }
        };
    }

    protected Runnable createGetTask(final CrudSupport<T> crud, final Long timeStamp,
            final CallbackWithValue<ParameterValueInTime<T>> callback) {
        return new Runnable() {
            @Override
            public void run() {
                crud.get(timeStamp, callback);
            }
        };
    }

    protected Runnable createUpdateTask(final CrudSupport<T> crud, final Long timeStamp, final T value,
            final CallbackWithValue<ParameterValueInTime<T>> callback) {
        return new Runnable() {
            @Override
            public void run() {
                crud.update(timeStamp, value, callback);
            }
        };
    }

    protected Runnable createDeleteTask(final CrudSupport<T> crud, final Long timeStamp,
            final CallbackWithValue<ParameterValueInTime<T>> callback) {
        return new Runnable() {
            @Override
            public void run() {
                crud.delete(timeStamp, callback);
            }
        };
    }
}
