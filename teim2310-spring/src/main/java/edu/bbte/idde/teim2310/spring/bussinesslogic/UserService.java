package edu.bbte.idde.teim2310.spring.bussinesslogic;

import edu.bbte.idde.teim2310.spring.dataaccess.UserDao;
import edu.bbte.idde.teim2310.spring.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Profile("jpa")
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User save(User user) {
        return userDao.save(user);
    }

    public Collection<User> findAll() {
        return userDao.findAll();
    }

    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    public User update(User user) {
        userDao.update(user);
        return userDao.findById(user.getId()).orElse(null);
    }

    public void delete(User user) {
        userDao.delete(user);
    }

}
