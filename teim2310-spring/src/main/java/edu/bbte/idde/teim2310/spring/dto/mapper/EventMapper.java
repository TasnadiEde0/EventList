package edu.bbte.idde.teim2310.spring.dto.mapper;

import edu.bbte.idde.teim2310.spring.dto.in.EventInDTO;
import edu.bbte.idde.teim2310.spring.dto.out.EventOutDTO;
import edu.bbte.idde.teim2310.spring.dto.out.EventOutPartDTO;
import edu.bbte.idde.teim2310.spring.model.Event;
import org.mapstruct.*;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class EventMapper {
    @Mapping(target = "id", ignore = true)
    public abstract Event fromEventDTOToEvent(EventInDTO in);

    @Mapping(target = "userId", source = "user.id", nullValuePropertyMappingStrategy
            = NullValuePropertyMappingStrategy.SET_TO_NULL)
    public abstract EventOutDTO fromEventToDTO(Event e);

    public abstract EventOutPartDTO fromEventToPartDTO(Event e);

    @IterableMapping(elementTargetType = EventOutDTO.class)
    public abstract Collection<EventOutDTO> fromEventToDTOs(Iterable<Event> events);

}
