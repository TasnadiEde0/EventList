package edu.bbte.idde.teim2310.spring.dto.mapper;

import edu.bbte.idde.teim2310.spring.dataaccess.UserDao;
import edu.bbte.idde.teim2310.spring.dto.in.EventInDTO;
import edu.bbte.idde.teim2310.spring.dto.in.UserInDTO;
import edu.bbte.idde.teim2310.spring.dto.out.EventOutDTO;
import edu.bbte.idde.teim2310.spring.dto.out.EventOutPartDTO;
import edu.bbte.idde.teim2310.spring.dto.out.UserOutDTO;
import edu.bbte.idde.teim2310.spring.model.Event;
import edu.bbte.idde.teim2310.spring.model.User;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

import java.util.Collection;

@Mapper(componentModel = "spring")
@Profile("jpa")
public abstract class UserEventMapper {
    @Autowired(required = false)
    private UserDao userDao;

    @Mapping(target = "id", ignore = true)
    public abstract Event fromEventDTOToEvent(EventInDTO in);

    @Mapping(target = "userId", source = "user.id")
    public abstract EventOutDTO fromEventToDTO(Event e);

    @IterableMapping(elementTargetType = EventOutDTO.class)
    public abstract Collection<EventOutDTO> fromEventToDTOs(Iterable<Event> events);

    @Mapping(target = "id", ignore = true)
    public abstract User fromUserDTOToUser(UserInDTO in);

    public abstract UserOutDTO fromUserToDTO(User e);

    @IterableMapping(elementTargetType = UserOutDTO.class)
    public abstract Collection<UserOutDTO> fromUserToDTOs(Iterable<User> users);

    @Named("map")
    protected User map(Long userId) {
        return userDao.findById(userId).orElse(null);
    }

}
