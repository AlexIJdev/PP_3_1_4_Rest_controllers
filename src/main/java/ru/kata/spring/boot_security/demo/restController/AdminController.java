package ru.kata.spring.boot_security.demo.restController;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.UserDetailsImpl;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UpdateValidator;
import ru.kata.spring.boot_security.demo.util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.util.UserNotUpdatedException;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserValidator userValidator;

    private final UpdateValidator updateValidator;

    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public AdminController(UserValidator userValidator, UpdateValidator updateValidator, UserService userService, RoleService roleService) {
        this.userValidator = userValidator;
        this.updateValidator = updateValidator;
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/info")
    public ResponseEntity<User> adminPage(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new ResponseEntity<>(userDetails.getUser(), HttpStatus.OK);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<User>> showAllUsers() {
        return new ResponseEntity<>(userService.showAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/allRoles")
    public ResponseEntity<List<Role>> showAllRoles() {
        return new ResponseEntity<>(roleService.showAllRoles(), HttpStatus.OK);
    }

    @GetMapping("/allUsers/{id}")
    public ResponseEntity<User> showUserById(@PathVariable int id) {
        return userService.getUserById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/new")
    public ResponseEntity<User> createNewUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        changeUsersRoles(user);
        if (bindingResult.hasErrors()) {
            throw new UserNotCreatedException(getErrorMessage(bindingResult).toString());
        }
        userService.createNewUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUserOption(@RequestBody @Valid User user, BindingResult bindingResult) {
        changeUsersRoles(user);
        updateValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new UserNotUpdatedException(getErrorMessage(bindingResult).toString());
        }
        if (user.getPassword().isEmpty()) {
            userService.updateUserPart(user);
        } else {
            userService.updateEntireUser(user);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<User> removeUser(@RequestBody User user) {
        userService.removeUserById(user.getId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private void changeUsersRoles(User user) {
        List<Role> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            if (role.getName().equals("ROLE_ADMIN")) {
                roles.add(roleService.getRole(1));
            }
            if (role.getName().equals("ROLE_USER")) {
                roles.add(roleService.getRole(2));
            }
        }
        user.setRoles(roles);
    }

    private StringBuilder getErrorMessage(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrorList) {
            if (fieldError.equals(fieldErrorList.get(fieldErrorList.size() - 1))) {
                errorMessage.append(fieldError.getDefaultMessage());
            } else {
                errorMessage.append(fieldError.getDefaultMessage()).append(".");
            }
        }
        return errorMessage;
    }
}
