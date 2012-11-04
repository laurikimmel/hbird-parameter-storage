package org.hbird.parameterstorage.simple.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.ParameterValueInTimeFactory;
import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.api.query.filter.Filter;
import org.hbird.parameterstorage.api.registry.SeriesFactory;
import org.hbird.parameterstorage.api.registry.SeriesRegistry;
import org.hbird.parameterstorage.concurrent.AtomicParameterValueInTimeFactory;
import org.hbird.parameterstorage.concurrent.registry.ConcurrentSeriesFactory;
import org.hbird.parameterstorage.concurrent.registry.ConcurrentSeriesRegistry;
import org.hbird.parameterstorage.simple.AsyncCrudSupport;
import org.hbird.parameterstorage.simple.SimpleCrudSupport;
import org.hbird.parameterstorage.simple.query.SimpleQuerySupport;
import org.hbird.parameterstorage.simple.query.SyncQuerySupport;
import org.hbird.parameterstorage.simple.query.filter.Filters;
import org.junit.Before;
import org.junit.Test;

public class ConcurrentRegistryTest {

    private static final String INPUT_FILE = "/RegistryTest.txt";
    private static final int NUMBER_OF_THREADS = 50;
    private static final int NUMBER_OF_LINES = 15;
    private static final long TIME_LIMIT = 1000;
    private static final int MAX_DELAY = 10;

    private ExecutorService execService;
    private SeriesFactory<Integer> seriesFactory;
    private SeriesRegistry<String, Integer> registry;
    private ParameterValueInTimeFactory<Integer> paramFactory;
    private AtomicLong timestamp;
    private Random random;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        execService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        seriesFactory = new ConcurrentSeriesFactory<Integer>();
        registry = new ConcurrentSeriesRegistry<String, Integer>(seriesFactory);
        timestamp = new AtomicLong(System.currentTimeMillis());
        paramFactory = new AtomicParameterValueInTimeFactory<Integer>();
        random = new Random();
    }

    @Test
    public void test() throws Exception {

        final AtomicLong invokeCount = new AtomicLong(0);
        final AtomicLong failCount = new AtomicLong(0);
        final AtomicLong addedCount = new AtomicLong(0);
        final AtomicLong addFailedCount = new AtomicLong(0);

        List<String> lines = readFile();
        assertNotNull(lines);
        assertEquals(NUMBER_OF_LINES, lines.size());
        for (int i = 0; i < lines.size(); i++) {
            final String[] tmp = lines.get(i).split(",");
            final Long ts = timestamp.incrementAndGet();
            execService.submit(new Runnable() {
                @Override
                public void run() {
                    invokeCount.incrementAndGet();

                    try {
                        Thread.sleep(random.nextInt(MAX_DELAY));
                    } catch (InterruptedException e) {
                    }

                    registry.getOrCreate(tmp[0], new CallbackWithValue<ParameterValueSeries<Integer>>() {
                        @Override
                        public void onValue(ParameterValueSeries<Integer> t) {
                            SimpleCrudSupport<Integer> scs = new SimpleCrudSupport<Integer>(t, paramFactory);
                            AsyncCrudSupport<Integer> crud = new AsyncCrudSupport<Integer>(scs, execService);
                            Integer value = Integer.parseInt(tmp[1]);
//                            System.out.printf("Adding %s with timestamp %s for key %s to series %s%n", value, ts,
//                                    tmp[0], t);
                            crud.add(ts, value, new CallbackWithValue<ParameterValueInTime<Integer>>() {
                                @Override
                                public void onValue(ParameterValueInTime<Integer> t) {
                                    addedCount.incrementAndGet();
                                }

                                @Override
                                public void onException(Throwable t) {
                                    addFailedCount.incrementAndGet();
                                    System.err.println(t);
                                    t.printStackTrace(System.err);
                                    fail();
                                }
                            });
                        }

                        @Override
                        public void onException(Throwable t) {
                            failCount.incrementAndGet();
                            System.err.println(t);
                            t.printStackTrace(System.err);
                            fail();
                        }
                    });
                }
            });
        }

        Thread.sleep(3 * 1000);

        execService.shutdown();
        execService.awaitTermination(TIME_LIMIT, TimeUnit.SECONDS);

        assertTrue("Not finished in time", execService.isTerminated());
        assertEquals(NUMBER_OF_LINES, invokeCount.get());
        assertEquals(NUMBER_OF_LINES, addedCount.get());
        assertEquals(0, failCount.get());
        assertEquals(0, addFailedCount.get());

        validate(registry);

    }

    private void validate(SeriesRegistry<String, Integer> registry) throws Exception {
        Filter<Integer> all = Filters.acceptAll();

        SyncSeriesRegistry<String, Integer> syncRegistry = new SyncSeriesRegistry<String, Integer>(registry);

        ParameterValueSeries<Integer> seriesA = syncRegistry.getOrGreate("A");
        ParameterValueSeries<Integer> seriesB = syncRegistry.getOrGreate("B");
        ParameterValueSeries<Integer> seriesC = syncRegistry.getOrGreate("C");

        SimpleQuerySupport<Integer> q = new SimpleQuerySupport<Integer>(seriesA);
        SyncQuerySupport<Integer> query = new SyncQuerySupport<Integer>(q);

        assertEquals("Size has to be 5", 5, query.count(all));
//        ParameterValueInTime<Integer> entry = seriesA.getLastValue();
//        while (entry != null) {
//            System.out.printf("A[%s] = %s (%s)%n", entry.getTimeStamp(), entry.getValue(), entry.getClass().getName());
//            entry = entry.getNext();
//        }

        assertEquals("First value has to be 5", new Integer(5), seriesA.getLastValue().getValue());
        assertEquals("Second value has to be 4", new Integer(4), seriesA.getLastValue().getNext().getValue());
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

    private List<String> readFile() {

        List<String> lines = new ArrayList<String>();
        InputStream is = getClass().getResourceAsStream(INPUT_FILE);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                lines.add(line);
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
        return lines;
    }
}
