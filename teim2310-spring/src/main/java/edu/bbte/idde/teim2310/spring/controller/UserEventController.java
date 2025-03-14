package edu.bbte.idde.teim2310.spring.controller;

import edu.bbte.idde.teim2310.spring.bussinesslogic.EventService;
import edu.bbte.idde.teim2310.spring.bussinesslogic.UserService;
import edu.bbte.idde.teim2310.spring.dto.in.EventInDTO;
import edu.bbte.idde.teim2310.spring.dto.mapper.UserEventMapper;
import edu.bbte.idde.teim2310.spring.dto.out.EventOutDTO;
import edu.bbte.idde.teim2310.spring.exception.NoElementToDeleteException;
import edu.bbte.idde.teim2310.spring.exception.ProcessingException;
import edu.bbte.idde.teim2310.spring.model.Event;
import edu.bbte.idde.teim2310.spring.model.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Objects;

@Slf4j
@Profile("jpa")
@RestController
@Controller
@RequestMapping("/api/users/{id}/events")
public class UserEventController {
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserEventMapper userEventMapper;

    @GetMapping("")
    public Collection<EventOutDTO> findAllByUser(@PathVariable("id") Long userId) throws ProcessingException {
        User user = userService.findById(userId).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such user");
        }

        return userEventMapper.fromEventToDTOs(user.getEvents());
    }

    @PostMapping("")
    public EventOutDTO createByUser(@PathVariable("id") Long userId, @RequestBody @Valid EventInDTO inEvent)
            throws ProcessingException {
        User user = userService.findById(userId).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such user");
        }

        Event event = userEventMapper.fromEventDTOToEvent(inEvent);
        event.setUser(user);

        event = eventService.save(event);

        return userEventMapper.fromEventToDTO(event);
    }

    @DeleteMapping("/{event_id}")
    public void deleteByUser(@PathVariable("id") Long userId, @PathVariable("event_id") Long eventId)
            throws ProcessingException {
        try {
            User user = userService.findById(userId).orElse(null);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such user");
            }
            Event event = eventService.findById(eventId);
            if (event == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such event");
            }
            if (Objects.equals(user.getId(), event.getUser().getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event not associated with user");
            }

            eventService.delete(event);
        } catch (NoElementToDeleteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such record", e);
        }
    }

}
