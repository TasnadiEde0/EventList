package edu.bbte.idde.teim2310.spring.dataaccess.inmemory;

import edu.bbte.idde.teim2310.spring.dataaccess.EventDao;
import edu.bbte.idde.teim2310.spring.model.Event;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("inmem")
public class InMemoryEventDao implements EventDao {
    protected Map<Long, Event> events = new ConcurrentHashMap<>();
    private final AtomicLong increment = new AtomicLong(0L);

    @Override
    public Event save(Event event) {
        event.setId(increment.getAndIncrement());
        events.put(event.getId(), event);
        return event;
    }

    @Override
    public Collection<Event> findAll() {
        return events.values();
    }

    @Override
    public Optional<Event> findById(Long id) {
        return Optional.ofNullable(events.get(id));
    }

    @Override
    public void update(Event event) {
        if (findById(event.getId()).isEmpty()) {
            throw new NoSuchElementException();
        }

        events.put(event.getId(), event);
    }

    @Override
    public void delete(Event event) {
        if (findById(event.getId()).isEmpty()) {
            throw new NoSuchElementException();
        }

        events.remove(event.getId());
    }

    @Override
    public Collection<Event> filterByReminder(Integer limit) {

        Collection<Event> filtered = new ArrayList<>();

        for (Event event : events.values())  {
            if (event.getReminderTime() <= limit) {
                filtered.add(event);
            }
        }

        return filtered;
    }


}
