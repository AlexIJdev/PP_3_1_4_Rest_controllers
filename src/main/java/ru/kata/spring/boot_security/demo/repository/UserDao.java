package ru.kata.spring.boot_security.demo.repository;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserById(int id);

    List<User> showAllUsers();

    void createNewUser(User user);

    void updateUser(User user);

    void removeUserById(int id);
}
