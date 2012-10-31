package org.hbird.parameterstorage.simple.query.filter;


import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.query.filter.Filter;
import org.hbird.parameterstorage.simple.query.filter.AcceptAllFilter;
import org.hbird.parameterstorage.simple.query.filter.AndFilter;
import org.hbird.parameterstorage.simple.query.filter.BeforeFilter;
import org.hbird.parameterstorage.simple.query.filter.StopAfterFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


import static org.hbird.parameterstorage.simple.query.filter.Filters.*;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FiltersTest {

    private static final Long NOW = System.currentTimeMillis();

    @Mock
    private Filter<Object> filter1;

    @Mock
    private Filter<Object> filter2;

    @Mock
    private ParameterValueInTime<Object> value;

    private InOrder inOrder;

    @Before
    public void setUp() {
        inOrder = inOrder(filter1, filter2, value);
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.query.filter.Filters#acceptAll()}.
     */
    @Test
    public void testAcceptAll() {
        Filter<Object> f1 = acceptAll();
        Filter<Object> f2 = acceptAll();
        assertNotNull(f1);
        assertNotNull(f2);
        assertNotSame(f1, f2);
        assertEquals(AcceptAllFilter.class, f1.getClass());
        assertEquals(AcceptAllFilter.class, f2.getClass());
        assertTrue(f1.accept(value));
        assertTrue(f2.accept(value));
    }

    /**
     * Test method for {@link
     * org.hbird.parameterstorage.simple.query.filter.Filters#andFilter(org.hbird.parameterstorage.api.query.Filter<T>[])}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAnd() {
        when(filter1.accept(value)).thenReturn(true);
        when(filter2.accept(value)).thenReturn(true);
        Filter<Object> f1 = and(filter1, filter2);
        Filter<Object> f2 = and(filter1, filter2);
        assertNotNull(f1);
        assertNotNull(f2);
        assertNotSame(f1, f2);
        assertEquals(AndFilter.class, f1.getClass());
        assertEquals(AndFilter.class, f2.getClass());

        assertTrue(f1.accept(value));
        inOrder.verify(filter1, times(1)).accept(value);
        inOrder.verify(filter2, times(1)).accept(value);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.query.filter.Filters#before(java.lang.Long)}.
     */
    @Test
    public void testBefore() {
        when(value.getTimeStamp()).thenReturn(NOW, NOW - 1, NOW + 1, NOW - 1);

        Filter<Object> f1 = before(NOW);
        Filter<Object> f2 = before(NOW);
        assertNotNull(f1);
        assertNotNull(f2);
        assertNotSame(f1, f2);
        assertEquals(BeforeFilter.class, f1.getClass());
        assertEquals(BeforeFilter.class, f2.getClass());

        assertFalse(f1.accept(value));
        assertTrue(f1.accept(value));
        assertFalse(f2.accept(value));
        assertTrue(f2.accept(value));

        inOrder.verify(value, times(4)).getTimeStamp();
        inOrder.verifyNoMoreInteractions();

    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.query.filter.Filters#stopAfter(java.lang.Long)}.
     */
    @Test
    public void testStopAfter() {
        when(value.getTimeStamp()).thenReturn(NOW, NOW - 1, NOW + 1, NOW - 2);

        Filter<Object> f1 = stopAfter(NOW);
        Filter<Object> f2 = stopAfter(NOW);
        assertNotNull(f1);
        assertNotNull(f2);
        assertNotSame(f1, f2);
        assertEquals(StopAfterFilter.class, f1.getClass());
        assertEquals(StopAfterFilter.class, f2.getClass());

        assertTrue(f1.accept(value));
        assertFalse(f1.accept(value));
        assertTrue(f2.accept(value));
        assertFalse(f2.accept(value));

        inOrder.verify(value, times(4)).getTimeStamp();
        inOrder.verifyNoMoreInteractions();
    }
}
