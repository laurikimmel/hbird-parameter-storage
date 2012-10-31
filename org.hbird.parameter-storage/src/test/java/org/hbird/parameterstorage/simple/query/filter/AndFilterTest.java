package org.hbird.parameterstorage.simple.query.filter;


import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.query.filter.Filter;
import org.hbird.parameterstorage.simple.query.filter.AndFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AndFilterTest {

    @Mock
    private Filter<Object> f1;

    @Mock
    private Filter<Object> f2;

    @Mock
    private Filter<Object> f3;

    @Mock
    private ParameterValueInTime<Object> value;

    private Filter<Object> andFilter;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        andFilter = new AndFilter<Object>(f1, f2, f3);
        inOrder = inOrder(f1, f2, f3, value);
    }

    /**
     * Test method for {@link
     * org.hbird.parameterstorage.simple.query.AndFilter#AndFilter(org.hbird.parameterstorage.api.query.Filter<T>[])}.
     */
    @Test
    public void testAndFilter() {
        testAccept();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.filter.AndFilter#accept(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testAccept() {
        when(f1.accept(value)).thenReturn(true);
        when(f2.accept(value)).thenReturn(true);
        when(f3.accept(value)).thenReturn(true);
        assertTrue(andFilter.accept(value));
        inOrder.verify(f1, times(1)).accept(value);
        inOrder.verify(f2, times(1)).accept(value);
        inOrder.verify(f3, times(1)).accept(value);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.filter.AndFilter#accept(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testAcceptThirdRejects() {
        when(f1.accept(value)).thenReturn(true);
        when(f2.accept(value)).thenReturn(true);
        when(f3.accept(value)).thenReturn(false);
        assertFalse(andFilter.accept(value));
        inOrder.verify(f1, times(1)).accept(value);
        inOrder.verify(f2, times(1)).accept(value);
        inOrder.verify(f3, times(1)).accept(value);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.filter.AndFilter#accept(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testAcceptSecondRejects() {
        when(f1.accept(value)).thenReturn(true);
        when(f2.accept(value)).thenReturn(false);
        assertFalse(andFilter.accept(value));
        inOrder.verify(f1, times(1)).accept(value);
        inOrder.verify(f2, times(1)).accept(value);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.filter.AndFilter#accept(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testAcceptFirstRejects() {
        when(f1.accept(value)).thenReturn(false);
        assertFalse(andFilter.accept(value));
        inOrder.verify(f1, times(1)).accept(value);
        inOrder.verifyNoMoreInteractions();
    }
}
