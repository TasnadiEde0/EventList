package edu.bbte.idde.teim2310.spring.controller;

import edu.bbte.idde.teim2310.spring.bussinesslogic.EventService;
import edu.bbte.idde.teim2310.spring.bussinesslogic.UserService;
import edu.bbte.idde.teim2310.spring.dto.in.UserInDTO;
import edu.bbte.idde.teim2310.spring.dto.mapper.UserEventMapper;
import edu.bbte.idde.teim2310.spring.dto.out.UserOutDTO;
import edu.bbte.idde.teim2310.spring.exception.ProcessingException;
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

@Slf4j
@Profile("jpa")
@RestController
@Controller
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserEventMapper userEventMapper;

    @GetMapping
    public Collection<UserOutDTO> findAll() {
        return userEventMapper.fromUserToDTOs(userService.findAll());
    }

    @GetMapping("/{id}")
    public UserOutDTO findById(@PathVariable("id") Long id) {
        User user = userService.findById(id).orElse(null);
        if (user != null) {
            return userEventMapper.fromUserToDTO(user);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such record");
    }

    @PostMapping("")
    public UserOutDTO save(@RequestBody @Valid UserInDTO inUser)  {
        User user = userService.save(userEventMapper.fromUserDTOToUser(inUser));
        return userEventMapper.fromUserToDTO(user);
    }

    @PutMapping("/{id}")
    public UserOutDTO update(@RequestBody @Valid UserInDTO inUser, @PathVariable("id") Long id)
            throws ProcessingException {
        User user = userEventMapper.fromUserDTOToUser(inUser);
        user.setId(id);
        user = userService.update(user);
        return userEventMapper.fromUserToDTO(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        User user = userService.findById(id).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such user");
        }

        userService.delete(user);
    }

}
