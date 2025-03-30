package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserDao;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(userDao.getUserByUsername(username)).get();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(userDao.getUserById(id)).get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> showAllUsers() {
        return userDao.showAllUsers();
    }

    @Override
    @Transactional
    public void createNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.createNewUser(user);
    }

    @Override
    @Transactional
    public void updateEntireUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.updateUser(user);
    }

    @Override
    @Transactional
    public void updateUserPart(User user) {
        user.setPassword(userDao.getUserById(user.getId()).get().getPassword());
        userDao.updateUser(user);
    }

    @Override
    @Transactional
    public void removeUserById(int id) {
        userDao.removeUserById(id);
    }
}
