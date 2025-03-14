package edu.bbte.idde.teim2310.spring.dataaccess;

import edu.bbte.idde.teim2310.spring.exception.DataRetrievalException;
import edu.bbte.idde.teim2310.spring.model.Event;

import java.util.Collection;
import java.util.Optional;

public interface EventDao {
    Event save(Event event) throws DataRetrievalException;

    Collection<Event> findAll()throws DataRetrievalException;

    Optional<Event> findById(Long id) throws DataRetrievalException;

    void update(Event event) throws DataRetrievalException;

    void delete(Event event)throws DataRetrievalException;

    Collection<Event> filterByReminder(Integer limit) throws DataRetrievalException;

}