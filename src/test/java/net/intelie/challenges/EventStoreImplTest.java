package net.intelie.challenges;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class EventStoreImplTest {

    private final EventStoreImpl eventStore = new EventStoreImpl();

    @Test
    public void testInsertEvent() {
        assertEquals(0, eventStore.getEvents().size());

        eventStore.insert(new Event("some_type1", 123L));

        assertEquals(1, eventStore.getEvents().size());
    }

    @Test
    public void testInsertNullEvent() {
        assertEquals(0, eventStore.getEvents().size());

        eventStore.insert(null);

        assertEquals(0, eventStore.getEvents().size());
    }

    @Test
    public void testRemoveAll() {
        eventStore.insert(new Event("some_type1", 123L));
        eventStore.insert(new Event("some_type1", 124L));
        eventStore.insert(new Event("some_type2", 125L));

        assertEquals(3, eventStore.getEvents().size());

        eventStore.removeAll("some_type1");

        assertEquals(1, eventStore.getEvents().size());
    }

    @Test
    public void testQuery() {
        eventStore.insert(new Event("some_type1", 123L));
        eventStore.insert(new Event("some_type1", 200L));
        eventStore.insert(new Event("some_type1", 250L));
        eventStore.insert(new Event("some_type2", 250L));

        EventIterator eventIterator = eventStore.query("some_type1", 0, 250);

        eventIterator.moveNext();

        assertEquals("some_type1", eventIterator.current().type());
        assertEquals(123L, eventIterator.current().timestamp());

        eventIterator.moveNext();

        assertEquals("some_type1", eventIterator.current().type());
        assertEquals(200L, eventIterator.current().timestamp());

        assertFalse(eventIterator.moveNext());

    }

}
