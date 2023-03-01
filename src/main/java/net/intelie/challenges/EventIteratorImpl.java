package net.intelie.challenges;

import java.util.Collections;
import java.util.List;

public class EventIteratorImpl implements EventIterator {

    private final List<Event> iteratorList;
    private int index;

    public EventIteratorImpl(List<Event> events) {
        // The method synchronizedList is used because it returns a synchronized (thread-safe) list
        iteratorList = Collections.synchronizedList(events);
        index = 0;
    }

    @Override
    public boolean moveNext() {
        // Iterator index will not move if it's already at the end
        if (index < iteratorList.size()) {
            index++;
            return true;
        }

        return false;
    }

    @Override
    public Event current() {
        if (index == 0)
            throw new IllegalStateException();

        return iteratorList.get(index - 1);
    }

    @Override
    public void remove() {
        if (index == 0)
            throw new IllegalStateException();

        iteratorList.remove(index - 1);
        index--;
    }

    @Override
    public void close() throws Exception {}
}
