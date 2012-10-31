package org.hbird.parameterstorage.simple;

import java.util.Iterator;

import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.ParameterValueSeries;


public class ParameterValueSeriesIterator<T> implements Iterator<ParameterValueInTime<T>> {

    protected final ParameterValueSeries<T> series;
    protected ParameterValueInTime<T> current;

    public ParameterValueSeriesIterator(ParameterValueSeries<T> series) {
        if (series == null) {
            throw new NullPointerException("ParameterValueSeries is null");
        }
        this.series = series;
    }

    /** @{inheritDoc . */
    @Override
    public boolean hasNext() {
        return current == null ? series.getLastValue() != null : series.next(current) != null;
    }

    /** @{inheritDoc . */
    @Override
    public ParameterValueInTime<T> next() {
        current = current == null ? series.getLastValue() : series.next(current);
        return current;
    }

    /** @{inheritDoc . */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove() not implemented for " + getClass().getName());
    }
}
