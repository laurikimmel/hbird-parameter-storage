package org.hbird.parameterstorage.simple.query;

import java.util.ArrayList;
import java.util.List;


import org.hbird.parameterstorage.api.CrudSupport;
import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.ParameterValueInTimeFactory;
import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.api.query.QuerySupport;
import org.hbird.parameterstorage.api.query.filter.Filter;
import org.hbird.parameterstorage.simple.CallbackWithValueAdapter;
import org.hbird.parameterstorage.simple.SimpleCrudSupport;
import org.hbird.parameterstorage.simple.SimpleParameterValueInTimeFactory;
import org.hbird.parameterstorage.simple.SimpleParameterValueSeries;
import org.hbird.parameterstorage.simple.query.QueryCallbackAdapter;
import org.hbird.parameterstorage.simple.query.SimpleQuerySupport;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TemperatureDataTest {

    private static long NOW = System.currentTimeMillis();

    private static final double[] DATA = { 2.18D, 2.04D, 2.09D, 2.48D, 2.17D, 2.11D, 2.48D, 2.04D, 2.09D, 2.44D };
    private static final double[] RESULT = { -0.35D, -0.05D, 0.44D, -0.37D, 0.06D, 0.31D, -0.39D, -0.05D, 0.14D };

    private ParameterValueSeries<Double> series;
    private ParameterValueInTimeFactory<Double> factory;
    private CrudSupport<Double> crud;
    private QuerySupport<Double> query;
    private List<Double> diffs;
    private Double max;
    private Double min;
    private Double avg;

    @Before
    public void setUp() {
        series = new SimpleParameterValueSeries<Double>();
        factory = new SimpleParameterValueInTimeFactory<Double>();
        crud = new SimpleCrudSupport<Double>(series, factory);
        query = new SimpleQuerySupport<Double>(series);

        for (double d : DATA) {
            crud.add(NOW++, d, new CallbackWithValueAdapter<ParameterValueInTime<Double>>());
        }
    }

    @Test
    public void testTemperatureDiff() {
        diffs = new ArrayList<Double>();
        Filter<Double> diffCollector = new Filter<Double>() {
            @Override
            public boolean accept(ParameterValueInTime<Double> value) {
                if (value.getNext() != null) {
                    diffs.add(value.getNext().getValue() - value.getValue());
                }
                return false;
            }
        };
        query.get(diffCollector, new QueryCallbackAdapter<Double>());
        assertEquals(RESULT.length, diffs.size());
        for (int i = 0; i < RESULT.length; i++) {
            assertEquals(RESULT[i], diffs.get(i), 0.00000001D);
        }
    }

    @Test
    public void testMax() {
        max = Double.MIN_VALUE;
        Filter<Double> maxCalculator = new Filter<Double>() {
            @Override
            public boolean accept(ParameterValueInTime<Double> value) {
                max = Math.max(max, value.getValue());
                return false;
            }
        };
        query.get(maxCalculator, new QueryCallbackAdapter<Double>());
        assertEquals(2.48D, max, 0.0D);
    }

    @Test
    public void testMin() {
        min = Double.MAX_VALUE;
        Filter<Double> minCalculator = new Filter<Double>() {
            @Override
            public boolean accept(ParameterValueInTime<Double> value) {
                min = Math.min(min, value.getValue());
                return false;
            }
        };
        query.get(minCalculator, new QueryCallbackAdapter<Double>());
        assertEquals(2.04D, min, 0.0D);
    }

    @Test
    public void testAvg() {
        avg = 0D;
        Filter<Double> avgCalculator = new Filter<Double>() {
            private int counter = 0;

            @Override
            public boolean accept(ParameterValueInTime<Double> value) {
                avg = avg * ((double) counter / (counter + 1)) + value.getValue() / (counter + 1);
                counter++;
                return false;
            }
        };
        query.get(avgCalculator, new QueryCallbackAdapter<Double>());
        assertEquals(2.212D, avg, 0.000000000000001D);
    }
}
