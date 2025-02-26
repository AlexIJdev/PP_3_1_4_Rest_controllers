package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

@Component
public class UpdateValidator {

    private final UserServiceImpl userService;

    @Autowired
    public UpdateValidator(UserServiceImpl userService) {
        this.userService = userService;
    }

    public void validate(User user, Errors errors) {
        for (User chekedUser : userService.showAllUsers()) {
            if (chekedUser.getId().equals(user.getId())) {
                continue;
            }
            if (chekedUser.getEmail().equals(user.getEmail())) {
                errors.rejectValue("email", "", "A user with the same username already exists!");
            }
        }
    }
}
