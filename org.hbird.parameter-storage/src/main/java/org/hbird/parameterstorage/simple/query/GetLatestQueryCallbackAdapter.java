package org.hbird.parameterstorage.simple.query;

import org.hbird.parameterstorage.api.ParameterValueInTime;

public class GetLatestQueryCallbackAdapter<T> extends QueryCallbackAdapter<T> {

    private ParameterValueInTime<T> value;

    /**
     * Returns value.
     * 
     * @return the value
     */
    public ParameterValueInTime<T> getValue() {
        return value;
    }

    /** @{inheritDoc . */
    @Override
    public void onValue(ParameterValueInTime<T> value) {
        this.value = value;
    }
}
