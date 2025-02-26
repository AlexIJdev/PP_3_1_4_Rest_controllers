package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserById(int id);

    List<User> showAllUsers();

    void createNewUser(User user);

    void updateEntireUser(User user);

    void updateUserPart(User user);

    void removeUserById(int id);
}
