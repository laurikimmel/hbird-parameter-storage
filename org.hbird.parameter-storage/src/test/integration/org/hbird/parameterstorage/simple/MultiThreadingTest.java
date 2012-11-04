package org.hbird.parameterstorage.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


import org.hbird.parameterstorage.api.CrudSupport;
import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.ParameterValueInTimeFactory;
import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.api.query.filter.Filter;
import org.hbird.parameterstorage.concurrent.AtomicParameterValueInTimeFactory;
import org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries;
import org.hbird.parameterstorage.simple.CallbackWithValueAdapter;
import org.hbird.parameterstorage.simple.SimpleCrudSupport;
import org.hbird.parameterstorage.simple.SyncCrudSupport;
import org.hbird.parameterstorage.simple.query.SimpleQuerySupport;
import org.hbird.parameterstorage.simple.query.SyncQuerySupport;
import org.hbird.parameterstorage.simple.query.filter.Filters;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiThreadingTest {

    private static final Long NOW = System.currentTimeMillis();
    private static final int NUMBER_OF_THREADS = 16;
    private static final int LIMIT = 10000;
    private static final int MAX_DELAY = 10;
    private static final int TIME_LIMIT = 600;

    private ExecutorService execService;

    private ParameterValueSeries<Double> series;
    private ParameterValueInTimeFactory<Double> factory;
    private CrudSupport<Double> crud;
    private Random random;
    private AtomicInteger counter;
    private SimpleQuerySupport<Double> simpleQuery;
    private SyncQuerySupport<Double> query;
    private List<Long> timestamps;
    private SyncCrudSupport<Double> syncCrud;
    private Filter<Double> acceptAll;

    @Before
    public void setUp() {
        execService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
//        series = new SimpleParameterValueSeries<Double>(def);
        series = new ConcurrentParameterValueSeries<Double>();
        factory = new AtomicParameterValueInTimeFactory<Double>();
        crud = new SimpleCrudSupport<Double>(series, factory);
        random = new Random();
        counter = new AtomicInteger(0);
        simpleQuery = new SimpleQuerySupport<Double>(series);
        query = new SyncQuerySupport<Double>(simpleQuery);
        syncCrud = new SyncCrudSupport<Double>(crud);
        timestamps = new ArrayList<Long>(LIMIT);
        for (long i = 0; i < LIMIT; i++) {
            timestamps.add(i);
        }
        System.out.printf("MAX: %s; MIN: %s%n", NOW + timestamps.get(0), NOW + timestamps.get(LIMIT - 1));
        Collections.shuffle(timestamps);
        assertEquals(LIMIT, timestamps.size());
        acceptAll = Filters.acceptAll();
    }

    @Test
    public void testWrite() throws InterruptedException {
        final AtomicLong addCounter = new AtomicLong(0);
        final AtomicLong invokeCounter = new AtomicLong(0);
        final AtomicLong finishCounter = new AtomicLong(0);
        final AtomicLong fail1Counter = new AtomicLong(0);
        final AtomicLong fail2Counter = new AtomicLong(0);
        long start = System.currentTimeMillis();
        for (int i = 0; i < LIMIT; i++) {
            execService.submit(new Runnable() {
                @Override
                public void run() {
                    invokeCounter.incrementAndGet();
                    double value = random.nextInt(MAX_DELAY);
                    long timeStamp = NOW + timestamps.get(counter.getAndIncrement());
                    try {
                        crud.add(timeStamp, value, new CallbackWithValueAdapter<ParameterValueInTime<Double>>() {
                            @Override
                            public void onException(Throwable t) {
                                fail1Counter.incrementAndGet();
                                System.err.println(t);
                                t.printStackTrace(System.err);
                                fail();
                            }

                            @Override
                            public void onValue(ParameterValueInTime<Double> t) {
                                addCounter.incrementAndGet();
                            }
                        });
                    } catch (Throwable t) {
                        fail2Counter.incrementAndGet();
                        System.err.println(t);
                        t.printStackTrace(System.err);
                        fail();
                    }
                    finishCounter.incrementAndGet();
                }
            });
        }
        execService.shutdown();
        execService.awaitTermination(TIME_LIMIT, TimeUnit.SECONDS);

        assertTrue("Not finished in time", execService.isTerminated());

        System.out.printf("Added %s entries without delay using %s threads in %s ms%n", LIMIT, NUMBER_OF_THREADS,
                System.currentTimeMillis() - start);

        System.out.printf("Invoke: %s; Fail A: %s; Fail B: %s; Finish: %s; Added: %s%n", invokeCounter.get(),
                fail1Counter.get(), fail2Counter.get(), finishCounter.get(), addCounter.get());

        assertEquals("Invoke", LIMIT, invokeCounter.get());
        assertEquals("Fail 1", 0, fail1Counter.get());
        assertEquals("Fail 2", 0, fail2Counter.get());
        assertEquals("Finish", LIMIT, finishCounter.get());
        assertEquals("Added", LIMIT, addCounter.get());

        List<ParameterValueInTime<Double>> list = query.get(acceptAll);
        long count = query.count(acceptAll);
        System.out.printf("Count: %s - %s %n", count, list.size());

        assertEquals("Count", LIMIT, count);
        assertEquals("Size", LIMIT, count);
    }

    @Test
    public void testWriteWithDelay() throws InterruptedException {
        final AtomicLong addCounter = new AtomicLong(0);
        final AtomicLong invokeCounter = new AtomicLong(0);
        final AtomicLong finishCounter = new AtomicLong(0);
        final AtomicLong fail1Counter = new AtomicLong(0);
        final AtomicLong fail2Counter = new AtomicLong(0);
        long start = System.currentTimeMillis();
        for (int i = 0; i < LIMIT; i++) {
            execService.submit(new Runnable() {
                @Override
                public void run() {
                    invokeCounter.incrementAndGet();
                    double value = random.nextInt(MAX_DELAY);
                    long timeStamp = NOW + timestamps.get(counter.getAndIncrement());
                    try {
                        Thread.sleep((long) value);
                    } catch (InterruptedException e) {
                    }
                    try {
                        crud.add(timeStamp, value, new CallbackWithValueAdapter<ParameterValueInTime<Double>>() {
                            @Override
                            public void onException(Throwable t) {
                                fail1Counter.incrementAndGet();
                                System.err.println(t);
                                t.printStackTrace(System.err);
                                fail();
                            }

                            @Override
                            public void onValue(ParameterValueInTime<Double> t) {
                                addCounter.incrementAndGet();
                            }
                        });
                    } catch (Throwable t) {
                        fail2Counter.incrementAndGet();
                        System.err.println(t);
                        t.printStackTrace(System.err);
                        fail();
                    }
                    finishCounter.incrementAndGet();
                }
            });
        }
        execService.shutdown();
        execService.awaitTermination(TIME_LIMIT, TimeUnit.SECONDS);

        assertTrue("Not finished in time", execService.isTerminated());

        System.out.printf("Added %s entries with max delay %s ms using %s threads in %s ms%n", LIMIT, MAX_DELAY,
                NUMBER_OF_THREADS, System.currentTimeMillis() - start);

        System.out.printf("Invoke: %s; Fail A: %s; Fail B: %s; Finish: %s; Added: %s%n", invokeCounter.get(),
                fail1Counter.get(), fail2Counter.get(), finishCounter.get(), addCounter.get());

        assertEquals("Invoke", LIMIT, invokeCounter.get());
        assertEquals("Fail 1", 0, fail1Counter.get());
        assertEquals("Fail 2", 0, fail2Counter.get());
        assertEquals("Finish", LIMIT, finishCounter.get());
        assertEquals("Added", LIMIT, addCounter.get());

        List<ParameterValueInTime<Double>> list = query.get(acceptAll);
        long count = query.count(acceptAll);
        System.out.printf("Count: %s - %s %n", count, list.size());

        assertEquals("Count", LIMIT, count);
        assertEquals("Size", LIMIT, count);
    }

    @Test
    public void testRemove() throws Exception {

        for (int i = 0; i < LIMIT; i++) {
            double value = random.nextInt(10);
            crud.add(NOW + i, value, new CallbackWithValueAdapter<ParameterValueInTime<Double>>());
        }
        assertEquals(LIMIT, query.count(acceptAll));

        for (int i = 0; i < LIMIT; i++) {
            assertNotNull(syncCrud.get(NOW + i));
        }

        final AtomicLong counter = new AtomicLong(0);
        final AtomicLong invokeCounter = new AtomicLong(0);
        final AtomicLong finishCounter = new AtomicLong(0);
        final AtomicLong failACounter = new AtomicLong(0);
        final AtomicLong failBCounter = new AtomicLong(0);
        final AtomicLong removeCounter = new AtomicLong(0);

        long start = System.currentTimeMillis();

        for (int i = 0; i < LIMIT; i++) {
            execService.submit(new Runnable() {
                @Override
                public void run() {
                    invokeCounter.incrementAndGet();
                    try {
                        long timeStamp = NOW + timestamps.get((int) counter.getAndIncrement());
                        crud.delete(timeStamp, new CallbackWithValueAdapter<ParameterValueInTime<Double>>() {
                            @Override
                            public void onException(Throwable t) {
                                failACounter.incrementAndGet();
                                System.err.println(t);
                                t.printStackTrace(System.err);
                                fail();
                            }

                            @Override
                            public void onValue(ParameterValueInTime<Double> t) {
                                removeCounter.incrementAndGet();
                            }
                        });
                    } catch (Throwable t) {
                        failBCounter.incrementAndGet();
                        System.err.println(t);
                        t.printStackTrace();
                        fail();
                    }
                    finishCounter.incrementAndGet();
                }
            });
        }

        execService.shutdown();
        execService.awaitTermination(TIME_LIMIT, TimeUnit.SECONDS);

        assertTrue("Not finished in time", execService.isTerminated());

        System.out.printf("Removed %s entries with out delay using %s threads in %s ms%n", LIMIT, NUMBER_OF_THREADS,
                System.currentTimeMillis() - start);

        System.out.printf("Invoke: %s; Fail A: %s; Fail B: %s; Finish: %s; Removed: %s%n", invokeCounter.get(),
                failACounter.get(), failBCounter.get(), finishCounter.get(), removeCounter.get());

        assertEquals("Invoke", LIMIT, invokeCounter.get());
        assertEquals("Fail A", 0, failACounter.get());
        assertEquals("Fail B", 0, failBCounter.get());
        assertEquals("Finish", LIMIT, finishCounter.get());
        assertEquals("Remove", LIMIT, removeCounter.get());

        List<ParameterValueInTime<Double>> list = query.get(acceptAll);
        long count = query.count(acceptAll);
        System.out.printf("Count: %s - %s %n", count, list.size());

        assertEquals("Count", 0, count);
        assertEquals("Size", 0, count);

    }

    @Test
    public void testAddAndRemove() throws Exception {
        for (int i = 0; i < LIMIT; i++) {
            double value = random.nextInt(10);
            crud.add(NOW + i, value, new CallbackWithValueAdapter<ParameterValueInTime<Double>>());
        }
        assertEquals(LIMIT, query.count(acceptAll));

        for (int i = 0; i < LIMIT; i++) {
            assertNotNull(syncCrud.get(NOW + i));
        }

        final AtomicLong counter = new AtomicLong(0);
        final AtomicLong removeInvokeCounter = new AtomicLong(0);
        final AtomicLong removeFinishCounter = new AtomicLong(0);
        final AtomicLong removeFailACounter = new AtomicLong(0);
        final AtomicLong removeFailBCounter = new AtomicLong(0);
        final AtomicLong removeCounter = new AtomicLong(0);

        final AtomicLong addInvokeCounter = new AtomicLong(0);
        final AtomicLong addFinishCounter = new AtomicLong(0);
        final AtomicLong addFailACounter = new AtomicLong(0);
        final AtomicLong addFailBCounter = new AtomicLong(0);
        final AtomicLong addCounter = new AtomicLong(0);

        long start = System.currentTimeMillis();

        for (int i = 0; i < LIMIT; i++) {

            if (i % 2 == 0) {

                execService.submit(new Runnable() {
                    @Override
                    public void run() {
                        removeInvokeCounter.incrementAndGet();
                        try {
                            long timeStamp = NOW + timestamps.get((int) counter.getAndIncrement());
                            crud.delete(timeStamp, new CallbackWithValueAdapter<ParameterValueInTime<Double>>() {
                                @Override
                                public void onException(Throwable t) {
                                    removeFailACounter.incrementAndGet();
                                    System.err.println(t);
                                    t.printStackTrace(System.err);
                                    fail();
                                }

                                @Override
                                public void onValue(ParameterValueInTime<Double> t) {
                                    removeCounter.incrementAndGet();
                                }
                            });
                        } catch (Throwable t) {
                            removeFailBCounter.incrementAndGet();
                            System.err.println(t);
                            t.printStackTrace();
                            fail();
                        }
                        removeFinishCounter.incrementAndGet();
                    }
                });
            } else {
                execService.submit(new Runnable() {
                    @Override
                    public void run() {
                        addInvokeCounter.incrementAndGet();
                        try {
                            double value = random.nextInt(10);
                            long timeStamp = NOW + LIMIT + timestamps.get((int) counter.getAndIncrement());
                            crud.add(timeStamp, value, new CallbackWithValueAdapter<ParameterValueInTime<Double>>() {
                                @Override
                                public void onException(Throwable t) {
                                    addFailACounter.incrementAndGet();
                                    System.err.println(t);
                                    t.printStackTrace(System.err);
                                    fail();
                                }

                                @Override
                                public void onValue(ParameterValueInTime<Double> t) {
                                    addCounter.incrementAndGet();
                                }
                            });
                        } catch (Throwable t) {
                            addFailBCounter.incrementAndGet();
                            System.err.println(t);
                            t.printStackTrace();
                            fail();
                        }
                        addFinishCounter.incrementAndGet();
                    }
                });
            }
        }

        execService.shutdown();
        execService.awaitTermination(TIME_LIMIT, TimeUnit.SECONDS);

        assertTrue("Not finished in time", execService.isTerminated());

        System.out.printf("Removed and added %s entries with out delay using %s threads in %s ms%n", LIMIT,
                NUMBER_OF_THREADS, System.currentTimeMillis() - start);

        System.out.printf("REMOVE - Invoke: %s; Fail A: %s; Fail B: %s; Finish: %s; Removed: %s%n",
                removeInvokeCounter.get(), removeFailACounter.get(), removeFailBCounter.get(),
                removeFinishCounter.get(), removeCounter.get());

        assertEquals("REMOVE Invoke", LIMIT / 2, removeInvokeCounter.get());
        assertEquals("REMOVE Fail A", 0, removeFailACounter.get());
        assertEquals("REMOVE Fail B", 0, removeFailBCounter.get());
        assertEquals("REMOVE Finish", LIMIT / 2, removeFinishCounter.get());
        assertEquals("REMOVE Remove", LIMIT / 2, removeCounter.get());

        System.out.printf("ADD - Invoke: %s; Fail A: %s; Fail B: %s; Finish: %s; Removed: %s%n",
                addInvokeCounter.get(), addFailACounter.get(), addFailBCounter.get(), addFinishCounter.get(),
                addCounter.get());

        assertEquals("ADD Invoke", LIMIT / 2, addInvokeCounter.get());
        assertEquals("ADD Fail A", 0, addFailACounter.get());
        assertEquals("ADD Fail B", 0, addFailBCounter.get());
        assertEquals("ADD Finish", LIMIT / 2, addFinishCounter.get());
        assertEquals("ADD Add", LIMIT / 2, addCounter.get());

        List<ParameterValueInTime<Double>> list = query.get(acceptAll);
        long count = query.count(acceptAll);
        System.out.printf("Count: %s - %s %n", count, list.size());

        assertEquals("Count", LIMIT, count);
        assertEquals("Size", LIMIT, count);
    }

    @Test
    public void testUpdate() throws Exception {
        for (int i = 0; i < LIMIT; i++) {
            double value = random.nextInt(10);
            crud.add(NOW + i, value, new CallbackWithValueAdapter<ParameterValueInTime<Double>>());
        }
        assertEquals(LIMIT, query.count(acceptAll));

        for (int i = 0; i < LIMIT; i++) {
            assertNotNull(syncCrud.get(NOW + i));
        }

        final AtomicLong counter = new AtomicLong(0);
        final AtomicLong invokeCounter = new AtomicLong(0);
        final AtomicLong finishCounter = new AtomicLong(0);
        final AtomicLong failACounter = new AtomicLong(0);
        final AtomicLong failBCounter = new AtomicLong(0);
        final AtomicLong updateCounter = new AtomicLong(0);

        long start = System.currentTimeMillis();

        for (int i = 0; i < LIMIT; i++) {
            execService.submit(new Runnable() {
                @Override
                public void run() {
                    invokeCounter.incrementAndGet();
                    try {
                        long timeStamp = NOW + timestamps.get((int) counter.getAndIncrement());
                        double value = random.nextInt(20);
                        crud.update(timeStamp, value, new CallbackWithValueAdapter<ParameterValueInTime<Double>>() {
                            @Override
                            public void onException(Throwable t) {
                                failACounter.incrementAndGet();
                                System.err.println(t);
                                t.printStackTrace(System.err);
                                fail();
                            }

                            @Override
                            public void onValue(ParameterValueInTime<Double> t) {
                                updateCounter.incrementAndGet();
                            }
                        });
                    } catch (Throwable t) {
                        failBCounter.incrementAndGet();
                        System.err.println(t);
                        t.printStackTrace();
                        fail();
                    }
                    finishCounter.incrementAndGet();
                }
            });
        }

        execService.shutdown();
        execService.awaitTermination(TIME_LIMIT, TimeUnit.SECONDS);

        assertTrue("Not finished in time", execService.isTerminated());

        System.out.printf("Updated %s entries with out delay using %s threads in %s ms%n", LIMIT, NUMBER_OF_THREADS,
                System.currentTimeMillis() - start);

        System.out.printf("Invoke: %s; Fail A: %s; Fail B: %s; Finish: %s; Removed: %s%n", invokeCounter.get(),
                failACounter.get(), failBCounter.get(), finishCounter.get(), updateCounter.get());

        assertEquals("Invoke", LIMIT, invokeCounter.get());
        assertEquals("Fail A", 0, failACounter.get());
        assertEquals("Fail B", 0, failBCounter.get());
        assertEquals("Finish", LIMIT, finishCounter.get());
        assertEquals("Remove", LIMIT, updateCounter.get());

        List<ParameterValueInTime<Double>> list = query.get(acceptAll);
        long count = query.count(acceptAll);
        System.out.printf("Count: %s - %s %n", count, list.size());

        assertEquals("Count", LIMIT, count);
        assertEquals("Size", LIMIT, count);

    }

    @Test
    public void testUpdateWithDelay() throws Exception {
        for (int i = 0; i < LIMIT; i++) {
            double value = random.nextInt(10);
            crud.add(NOW + i, value, new CallbackWithValueAdapter<ParameterValueInTime<Double>>());
        }
        assertEquals(LIMIT, query.count(acceptAll));

        for (int i = 0; i < LIMIT; i++) {
            assertNotNull(syncCrud.get(NOW + i));
        }

        final AtomicLong counter = new AtomicLong(0);
        final AtomicLong invokeCounter = new AtomicLong(0);
        final AtomicLong finishCounter = new AtomicLong(0);
        final AtomicLong failACounter = new AtomicLong(0);
        final AtomicLong failBCounter = new AtomicLong(0);
        final AtomicLong updateCounter = new AtomicLong(0);

        long start = System.currentTimeMillis();

        for (int i = 0; i < LIMIT; i++) {
            execService.submit(new Runnable() {
                @Override
                public void run() {
                    invokeCounter.incrementAndGet();
                    try {
                        long timeStamp = NOW + timestamps.get((int) counter.getAndIncrement());
                        double value = random.nextInt(MAX_DELAY);
                        Thread.sleep((long) value);
                        crud.update(timeStamp, value, new CallbackWithValueAdapter<ParameterValueInTime<Double>>() {
                            @Override
                            public void onException(Throwable t) {
                                failACounter.incrementAndGet();
                                System.err.println(t);
                                t.printStackTrace(System.err);
                                fail();
                            }

                            @Override
                            public void onValue(ParameterValueInTime<Double> t) {
                                updateCounter.incrementAndGet();
                            }
                        });
                    } catch (Throwable t) {
                        failBCounter.incrementAndGet();
                        System.err.println(t);
                        t.printStackTrace();
                        fail();
                    }
                    finishCounter.incrementAndGet();
                }
            });
        }

        execService.shutdown();
        execService.awaitTermination(TIME_LIMIT, TimeUnit.SECONDS);

        assertTrue("Not finished in time", execService.isTerminated());

        System.out.printf("Updated %s entries with max delay %s ms using %s threads in %s ms%n", LIMIT, MAX_DELAY,
                NUMBER_OF_THREADS, System.currentTimeMillis() - start);

        System.out.printf("Updated %s entries with out delay using %s threads in %s ms%n", LIMIT, NUMBER_OF_THREADS,
                System.currentTimeMillis() - start);

        System.out.printf("Invoke: %s; Fail A: %s; Fail B: %s; Finish: %s; Removed: %s%n", invokeCounter.get(),
                failACounter.get(), failBCounter.get(), finishCounter.get(), updateCounter.get());

        assertEquals("Invoke", LIMIT, invokeCounter.get());
        assertEquals("Fail A", 0, failACounter.get());
        assertEquals("Fail B", 0, failBCounter.get());
        assertEquals("Finish", LIMIT, finishCounter.get());
        assertEquals("Remove", LIMIT, updateCounter.get());

        List<ParameterValueInTime<Double>> list = query.get(acceptAll);
        long count = query.count(acceptAll);
        System.out.printf("Count: %s - %s %n", count, list.size());

        assertEquals("Count", LIMIT, count);
        assertEquals("Size", LIMIT, count);

    }

}
