package org.hbird.parameterstorage.simple.query;

import java.util.ArrayList;
import java.util.List;

import org.hbird.parameterstorage.api.ParameterValueInTime;


public class ListQueryCallbackAdapter<T> extends QueryCallbackAdapter<T> {

    private final List<ParameterValueInTime<T>> list;

    /**
     * Creates new ListQueryCallbackAdapter.
     * 
     * @param list
     */
    public ListQueryCallbackAdapter(List<ParameterValueInTime<T>> list) {
        this.list = list;
    }

    /**
     * Creates new ListQueryCallbackAdapter.
     * 
     * @param list
     */
    public ListQueryCallbackAdapter(int initialSize) {
        this(new ArrayList<ParameterValueInTime<T>>(initialSize));
    }

    /**
     * Creates new ListQueryCallbackAdapter.
     * 
     * @param list
     */
    public ListQueryCallbackAdapter() {
        this(new ArrayList<ParameterValueInTime<T>>());
    }

    /**
     * Returns list.
     * 
     * @return the list
     */
    public List<ParameterValueInTime<T>> getList() {
        return list;
    }

    /** @{inheritDoc . */
    @Override
    public void onValue(ParameterValueInTime<T> value) {
        list.add(value);
    }
}
