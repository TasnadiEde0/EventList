package edu.bbte.idde.teim2310.spring.dataaccess;

import edu.bbte.idde.teim2310.spring.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Profile("jpa")
@Repository
public interface UserDao {
    User save(User user);

    Collection<User> findAll();

    Optional<User> findById(Long id);

    void update(User user);

    void delete(User user);
}
