package edu.bbte.idde.teim2310.spring.dataaccess.jpa;

import edu.bbte.idde.teim2310.spring.dataaccess.UserDao;
import edu.bbte.idde.teim2310.spring.model.User;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Profile("jpa")
@Repository
public interface JpaUserDao extends UserDao, JpaRepository<User, Long> {

    @Override
    @Modifying
    @Query("""
        UPDATE User u 
        SET u.name = :#{#user.name}, 
            u.email = :#{#user.email}
        WHERE u.id = :#{#user.id}
        """)
    @Transactional
    void update(@Param("user") User user);
}
