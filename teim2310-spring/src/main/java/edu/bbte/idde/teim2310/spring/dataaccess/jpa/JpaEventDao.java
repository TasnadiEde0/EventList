package edu.bbte.idde.teim2310.spring.dataaccess.jpa;

import edu.bbte.idde.teim2310.spring.dataaccess.EventDao;
import edu.bbte.idde.teim2310.spring.model.Event;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Profile("jpa")
@Repository
public interface JpaEventDao extends EventDao, JpaRepository<Event, Long> {

    @Modifying
    @Query("""
        UPDATE Event e 
        SET e.name = :#{#event.name}, 
            e.location = :#{#event.location}, 
            e.startDate = :#{#event.startDate}, 
            e.endDate = :#{#event.endDate}, 
            e.reminderTime = :#{#event.reminderTime} 
        WHERE e.id = :#{#event.id}
        """)
    @Transactional
    @Override
    void update(@Param("event") Event event);

    @Override
    @Query("SELECT e FROM Event e WHERE e.reminderTime <= :limit")
    Collection<Event> filterByReminder(@Param("limit") Integer limit);

}
