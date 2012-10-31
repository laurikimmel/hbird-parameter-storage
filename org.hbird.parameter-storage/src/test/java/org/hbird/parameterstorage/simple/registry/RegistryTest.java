package org.hbird.parameterstorage.simple.registry;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.hbird.parameterstorage.api.CrudSupport;
import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.ParameterValueInTimeFactory;
import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.api.query.filter.Filter;
import org.hbird.parameterstorage.api.registry.SeriesFactory;
import org.hbird.parameterstorage.api.registry.SeriesRegistry;
import org.hbird.parameterstorage.simple.CallbackWithValueAdapter;
import org.hbird.parameterstorage.simple.SimpleCrudSupport;
import org.hbird.parameterstorage.simple.SimpleParameterValueInTimeFactory;
import org.hbird.parameterstorage.simple.query.SimpleQuerySupport;
import org.hbird.parameterstorage.simple.query.SyncQuerySupport;
import org.hbird.parameterstorage.simple.query.filter.Filters;
import org.hbird.parameterstorage.simple.registry.SimpleSeriesFactory;
import org.hbird.parameterstorage.simple.registry.SimpleSeriesRegistry;
import org.hbird.parameterstorage.simple.registry.SyncSeriesRegistry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegistryTest {

    private static final String INPUT_FILE = "/RegistryTest.txt";

    private SeriesRegistry<String, Integer> registry;
    private SeriesFactory<Integer> factory;
    private SyncSeriesRegistry<String, Integer> syncRegistry;
    private long timestamp;
    private ParameterValueInTimeFactory<Integer> paramFactory;

    @Before
    public void setup() {
        factory = new SimpleSeriesFactory<Integer>();
        registry = new SimpleSeriesRegistry<String, Integer>(factory);
        syncRegistry = new SyncSeriesRegistry<String, Integer>(registry);
        timestamp = System.currentTimeMillis();
        paramFactory = new SimpleParameterValueInTimeFactory<Integer>();
    }

    @Test
    public void testRegistry() throws Exception {
        readFile();
        validate();
    }

    private void readFile() {
        InputStream is = getClass().getResourceAsStream(INPUT_FILE);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                onLine(line);
            }
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                fail(e.getMessage());
            }
        }
    }

    private void validate() throws Exception {
        Filter<Integer> all = Filters.acceptAll();

        ParameterValueSeries<Integer> seriesA = syncRegistry.getOrGreate("A");
        ParameterValueSeries<Integer> seriesB = syncRegistry.getOrGreate("B");
        ParameterValueSeries<Integer> seriesC = syncRegistry.getOrGreate("C");

        SimpleQuerySupport<Integer> q = new SimpleQuerySupport<Integer>(seriesA);
        SyncQuerySupport<Integer> query = new SyncQuerySupport<Integer>(q);

        assertEquals(5, query.count(all));
        assertEquals(new Integer(5), seriesA.getLastValue().getValue());
        assertEquals(new Integer(4), seriesA.getLastValue().getNext().getValue());
        assertEquals(new Integer(3), seriesA.getLastValue().getNext().getNext().getValue());
        assertEquals(new Integer(2), seriesA.getLastValue().getNext().getNext().getNext().getValue());
        assertEquals(new Integer(1), seriesA.getLastValue().getNext().getNext().getNext().getNext().getValue());

        q = new SimpleQuerySupport<Integer>(seriesB);
        query = new SyncQuerySupport<Integer>(q);

        assertEquals(5, query.count(all));
        assertEquals(new Integer(10), seriesB.getLastValue().getValue());
        assertEquals(new Integer(9), seriesB.getLastValue().getNext().getValue());
        assertEquals(new Integer(8), seriesB.getLastValue().getNext().getNext().getValue());
        assertEquals(new Integer(7), seriesB.getLastValue().getNext().getNext().getNext().getValue());
        assertEquals(new Integer(6), seriesB.getLastValue().getNext().getNext().getNext().getNext().getValue());

        q = new SimpleQuerySupport<Integer>(seriesC);
        query = new SyncQuerySupport<Integer>(q);

        assertEquals(5, query.count(all));
        assertEquals(new Integer(15), seriesC.getLastValue().getValue());
        assertEquals(new Integer(14), seriesC.getLastValue().getNext().getValue());
        assertEquals(new Integer(13), seriesC.getLastValue().getNext().getNext().getValue());
        assertEquals(new Integer(12), seriesC.getLastValue().getNext().getNext().getNext().getValue());
        assertEquals(new Integer(11), seriesC.getLastValue().getNext().getNext().getNext().getNext().getValue());
    }

    private void onLine(String line) throws Exception {
        String[] tmp = line.split(",");
        ParameterValueSeries<Integer> series = syncRegistry.getOrGreate(tmp[0]);
        assertNotNull(series);
        CrudSupport<Integer> c = new SimpleCrudSupport<Integer>(series, paramFactory);
        c.add(timestamp++, Integer.parseInt(tmp[1]), new CallbackWithValueAdapter<ParameterValueInTime<Integer>>());
    }
}
