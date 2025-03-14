package edu.bbte.idde.teim2310.spring.controller;

import edu.bbte.idde.teim2310.spring.bussinesslogic.EventService;
import edu.bbte.idde.teim2310.spring.dto.out.EventOutPartDTO;
import edu.bbte.idde.teim2310.spring.exception.IncorrectFullParameterException;
import edu.bbte.idde.teim2310.spring.exception.ProcessingException;
import edu.bbte.idde.teim2310.spring.exception.NoElementToDeleteException;
import edu.bbte.idde.teim2310.spring.exception.NoElementToUpdateException;
import edu.bbte.idde.teim2310.spring.model.Event;

import edu.bbte.idde.teim2310.spring.dto.mapper.EventMapper;
import edu.bbte.idde.teim2310.spring.dto.in.EventInDTO;
import edu.bbte.idde.teim2310.spring.dto.out.EventOutDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Objects;

@Slf4j
@RestController
@Controller
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private EventService service;
    @Autowired
    private EventMapper mapper;

    @GetMapping
    public Collection<EventOutDTO> findAll(@RequestParam(name = "reminderLimit", required = false) Integer limit)
            throws ProcessingException {
        if (limit != null) {
            return mapper.fromEventToDTOs(service.filterByReminder(limit));
        }
        return mapper.fromEventToDTOs(service.findAll());

    }

    @GetMapping("/{id}")
    public EventOutPartDTO findById(@PathVariable("id") Long id, @RequestParam(name = "full") String full) throws ProcessingException, IncorrectFullParameterException {
        Event event = service.findById(id);
        if (event != null) {
            if (Objects.equals(full, "no")) {
                return mapper.fromEventToPartDTO(event);
            } else if (Objects.equals(full, "yes")) {
                return mapper.fromEventToDTO(event);
            } else if (full != null && !full.isEmpty()) {
                throw new IncorrectFullParameterException();
            }

        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such record");
    }

    @PostMapping("")
    public EventOutDTO save(@RequestBody @Valid EventInDTO inEvent) throws ProcessingException {
        Event event = service.save(mapper.fromEventDTOToEvent(inEvent));
        return mapper.fromEventToDTO(event);
    }

    @PutMapping("/{id}")
    public EventOutDTO update(@RequestBody @Valid EventInDTO inEvent, @PathVariable("id") Long id)
            throws ProcessingException {
        try {
            Event event = mapper.fromEventDTOToEvent(inEvent);
            event.setId(id);
            event = service.update(event);
            return mapper.fromEventToDTO(event);
        } catch (NoElementToUpdateException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such record", exception);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws ProcessingException {
        try {
            Event event = service.findById(id);
            service.delete(event);
        } catch (NoElementToDeleteException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such record", exception);
        }
    }

}
