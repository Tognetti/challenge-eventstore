package net.intelie.challenges;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventIteratorImplTest {

    private final EventStoreImpl eventStore = new EventStoreImpl();

    @Before
    public void createEvents() {
        eventStore.insert(new Event("some_type1", 123L));
        eventStore.insert(new Event("some_type2", 125L));
        eventStore.insert(new Event("some_type1", 124L));
        eventStore.insert(new Event("some_type2", 126L));
    }

    @Test
    public void testMoveNext() {
        EventIterator eventIterator = new EventIteratorImpl(eventStore.getEvents());

        assertTrue(eventIterator.moveNext());

        assertEquals("some_type1", eventIterator.current().type());
        assertEquals(123L, eventIterator.current().timestamp());
    }

    @Test
    public void testMoveNextUntilReachesEnd() {
        EventIterator eventIterator = new EventIteratorImpl(eventStore.getEvents());

        assertTrue(eventIterator.moveNext());
        assertTrue(eventIterator.moveNext());
        assertTrue(eventIterator.moveNext());
        assertTrue(eventIterator.moveNext());
        assertFalse(eventIterator.moveNext());

        assertEquals("some_type2", eventIterator.current().type());
        assertEquals(126L, eventIterator.current().timestamp());
    }

    @Test
    public void testMoveNextAfterReachingEnd() {
        EventIterator eventIterator = new EventIteratorImpl(eventStore.getEvents());

        assertTrue(eventIterator.moveNext());
        assertTrue(eventIterator.moveNext());
        assertTrue(eventIterator.moveNext());
        assertTrue(eventIterator.moveNext());
        assertFalse(eventIterator.moveNext());
        assertFalse(eventIterator.moveNext());

        assertEquals("some_type2", eventIterator.current().type());
        assertEquals(126L, eventIterator.current().timestamp());
    }

    @Test
    public void testCurrent() {
        EventIterator eventIterator = new EventIteratorImpl(eventStore.getEvents());

        assertThrows(IllegalStateException.class, eventIterator::current);

        eventIterator.moveNext();

        assertEquals("some_type1", eventIterator.current().type());
        assertEquals(123L, eventIterator.current().timestamp());
    }

    @Test
    public void testRemove() {
        EventIterator eventIterator = new EventIteratorImpl(eventStore.getEvents());

        eventIterator.moveNext();
        eventIterator.moveNext();

        eventIterator.remove();

        assertEquals("some_type1", eventIterator.current().type());
        assertEquals(123L, eventIterator.current().timestamp());
    }

    @Test
    public void testRemoveFirstEvent() {
        EventIterator eventIterator = new EventIteratorImpl(eventStore.getEvents());

        eventIterator.moveNext();

        eventIterator.remove();

        assertThrows(IllegalStateException.class, eventIterator::current);
    }

    @Test
    public void testRemoveException() {
        EventIterator eventIterator = new EventIteratorImpl(eventStore.getEvents());

        assertThrows(IllegalStateException.class, eventIterator::remove);
    }
}
