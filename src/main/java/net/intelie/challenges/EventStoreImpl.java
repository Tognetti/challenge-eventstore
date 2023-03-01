package net.intelie.challenges;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventStoreImpl implements EventStore {

    // The method synchronizedList is used because it returns a synchronized (thread-safe) list
    private final List<Event> events = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void insert(Event event) {
        if (event == null) return;

        events.add(event);
    }

    @Override
    public void removeAll(String type) {
        events.removeIf(event -> event.type().equals(type));
    }

    @Override
    public EventIterator query(String type, long startTime, long endTime) {
        List<Event> queriedEvents = new ArrayList<>();

        // The synchronized block is used so that multiple threads cannot modify the list while the loop is running
        synchronized (events) {
            for (Event event : events) {
                if (event.type().equals(type) && event.timestamp() >= startTime && event.timestamp() < endTime) {
                    queriedEvents.add(event);
                }
            }
        }
        return new EventIteratorImpl(queriedEvents);
    }

    public List<Event> getEvents() {
        return events;
    }

}
