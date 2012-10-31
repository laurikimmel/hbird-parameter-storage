package org.hbird.parameterstorage.simple.query;

import java.util.List;


import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.simple.query.ListQueryCallbackAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ListQueryCallbackAdapterTest {

    @Mock
    private List<ParameterValueInTime<Object>> list;

    @Mock
    private ParameterValueInTime<Object> value1;

    @Mock
    private ParameterValueInTime<Object> value2;

    private ListQueryCallbackAdapter<Object> adapter;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        adapter = new ListQueryCallbackAdapter<Object>(list);
        inOrder = inOrder(list, value1, value2);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.ListQueryCallbackAdapter#onValue(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testOnValue() {
        adapter.onValue(value1);
        adapter.onValue(value2);
        adapter.onValue(value1);
        adapter.onValue(value2);
        adapter.onValue(value2);
        inOrder.verify(list, times(1)).add(value1);
        inOrder.verify(list, times(1)).add(value2);
        inOrder.verify(list, times(1)).add(value1);
        inOrder.verify(list, times(2)).add(value2);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.ListQueryCallbackAdapter#ListQueryCallbackAdabter(java.util.List)}.
     */
    @Test
    public void testListQueryCallbackAdabterListOfParameterValueInTimeOfT() {
        testOnValue();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.ListQueryCallbackAdapter#ListQueryCallbackAdabter(int)}.
     */
    @Test
    public void testListQueryCallbackAdabterInt() {
        adapter = new ListQueryCallbackAdapter<Object>(5);
        adapter.onValue(value1);
        adapter.onValue(value1);
        adapter.onValue(value2);
        adapter.onValue(value1);
        adapter.onValue(value2);
        List<ParameterValueInTime<Object>> list = adapter.getList();
        assertEquals(5, list.size());
        assertEquals(value1, list.get(0));
        assertEquals(value1, list.get(1));
        assertEquals(value2, list.get(2));
        assertEquals(value1, list.get(3));
        assertEquals(value2, list.get(4));
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.ListQueryCallbackAdapter#ListQueryCallbackAdabter()}.
     */
    @Test
    public void testListQueryCallbackAdabter() {
        adapter = new ListQueryCallbackAdapter<Object>();
        adapter.onValue(value1);
        adapter.onValue(value1);
        adapter.onValue(value2);
        adapter.onValue(value1);
        adapter.onValue(value2);
        List<ParameterValueInTime<Object>> list = adapter.getList();
        assertEquals(5, list.size());
        assertEquals(value1, list.get(0));
        assertEquals(value1, list.get(1));
        assertEquals(value2, list.get(2));
        assertEquals(value1, list.get(3));
        assertEquals(value2, list.get(4));
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.query.ListQueryCallbackAdapter#getList()}.
     */
    @Test
    public void testGetList() {
        testListQueryCallbackAdabter();
    }
}
