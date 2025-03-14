package edu.bbte.idde.teim2310.spring.bussinesslogic;

import edu.bbte.idde.teim2310.spring.dataaccess.EventDao;
import edu.bbte.idde.teim2310.spring.exception.DataRetrievalException;
import edu.bbte.idde.teim2310.spring.exception.NoElementToDeleteException;
import edu.bbte.idde.teim2310.spring.exception.NoElementToUpdateException;
import edu.bbte.idde.teim2310.spring.exception.ProcessingException;
import edu.bbte.idde.teim2310.spring.model.Event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class EventService implements Serializable {
    @Autowired
    private EventDao eventDao;

    public Event save(Event event) throws ProcessingException {
        try {
            return eventDao.save(event);
        } catch (DataRetrievalException e) {
            log.error(e.toString());
            throw new ProcessingException(e);
        }
    }

    public Collection<Event> findAll() throws ProcessingException {
        try {
            return eventDao.findAll();
        } catch (DataRetrievalException e) {
            log.error(e.toString());
            throw new ProcessingException(e);
        }
    }

    public Event findById(Long id) throws ProcessingException {
        try {
            return eventDao.findById(id).orElse(null);
        } catch (DataRetrievalException e) {
            log.error(e.toString());
            throw new ProcessingException(e);
        }
    }

    public Event update(Event event) throws NoElementToUpdateException, ProcessingException {
        try {
            eventDao.update(event);
            return eventDao.findById(event.getId()).orElse(null);
        } catch (NoSuchElementException e) {
            throw new NoElementToUpdateException(e.getCause());
        } catch (DataRetrievalException e) {
            log.error(e.toString());
            throw new ProcessingException(e);
        }
    }

    public void delete(Event event) throws NoElementToDeleteException, ProcessingException {
        try {
            eventDao.delete(event);
        } catch (NoSuchElementException e) {
            throw new NoElementToDeleteException(e.getCause());
        } catch (DataRetrievalException e) {
            throw new ProcessingException(e);
        }
    }

    public Collection<Event> filterByReminder(Integer limit) throws ProcessingException {
        try {
            return eventDao.filterByReminder(limit);
        } catch (DataRetrievalException e) {
            throw new ProcessingException(e);
        }
    }

}
