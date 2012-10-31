package org.hbird.parameterstorage.simple;


import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.ParameterValueInTimeFactory;
import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.simple.SimpleCrudSupport;
import org.hbird.parameterstorage.simple.SimpleParameterValueInTimeFactory;
import org.hbird.parameterstorage.simple.SimpleParameterValueSeries;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataTest {

    private ParameterValueSeries<Double> series;
    private ParameterValueInTimeFactory<Double> factory;
    private SimpleCrudSupport<Double> crud;
    private CallbackWithValue<ParameterValueInTime<Double>> callback;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        series = new SimpleParameterValueSeries<Double>();

        factory = new SimpleParameterValueInTimeFactory<Double>();
        crud = new SimpleCrudSupport<Double>(series, factory);
        callback = new CallbackWithValue<ParameterValueInTime<Double>>() {
            @Override
            public void onValue(ParameterValueInTime<Double> t) {
            }

            @Override
            public void onException(Throwable t) {
            }
        };
    }

    @Test
    public void testAddLotsOfEntries() {
        long start = System.currentTimeMillis();
        int limit = 100000;
        for (int i = 0; i < limit; i++) {
            crud.add(start + i, Double.valueOf(i), callback);
        }
        long afterInsert = System.currentTimeMillis();
        final long[] result = new long[1];
        series.count(new CallbackWithValue<Long>() {
            @Override
            public void onValue(Long t) {
                result[0] = t;
            }

            @Override
            public void onException(Throwable t) {
            }
        });
        long end = System.currentTimeMillis();
        assertEquals(limit, result[0]);
        System.out.printf("time for %s inserts - %s ms%n", limit, afterInsert - start);
        System.out.printf("time for count %s items - %s ms%n", result[0], end - afterInsert);
        System.out.printf("time for %s inserts + count - %s ms%n", limit, end - start);
    }

    @Test
    public void testAddLotsOfEntriesAndDeleteFromEnd() {
        long start = System.currentTimeMillis();
        int limit = 100000;
        for (int i = 0; i < limit; i++) {
            crud.add(start + i, Double.valueOf(i), callback);
        }
        long m1 = System.currentTimeMillis();
        final long[] result1 = new long[1];
        series.count(new CallbackWithValue<Long>() {
            @Override
            public void onValue(Long t) {
                result1[0] = t;
            }

            @Override
            public void onException(Throwable t) {
            }
        });
        long m2 = System.currentTimeMillis();
        assertEquals(limit, result1[0]);

        for (int i = 0; i < limit; i++) {
            crud.delete(start + limit - 1 - i, callback);
        }

        long m3 = System.currentTimeMillis();
        final long[] result2 = new long[1];
        series.count(new CallbackWithValue<Long>() {
            @Override
            public void onValue(Long t) {
                result2[0] = t;
            }

            @Override
            public void onException(Throwable t) {
            }
        });
        long m4 = System.currentTimeMillis();
        assertEquals(0, result2[0]);

        System.out.printf("time for %s inserts - %s ms%n", limit, m1 - start);
        System.out.printf("time for count %s items - %s ms%n", result1[0], m2 - m1);
        System.out.printf("time for delete %s items from end - %s ms%n", result1[0], m3 - m2);
        System.out.printf("time for count %s items - %s ms%n", result1[0], m4 - m3);
        System.out.printf("total time - ms %s %n", limit, m4 - start);
    }
}
