package ru.kata.spring.boot_security.demo.restController;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UpdateValidator;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @GetMapping()
    public List<User> showAllUsers(Authentication authentication, Model model) {
        Optional<User> user = userService.getUserByUsername(authentication.getName());
        user.ifPresent(value -> model.addAttribute("user", value));
        model.addAttribute("users", userService.showAllUsers());
        model.addAttribute("roles", roleService.showAllRoles());
        return userService.showAllUsers();
    }

    @PostMapping("/new")
    public String createNewUser(@ModelAttribute("user") @Valid User user,
                                BindingResult bindingResult) {
        changeUsersRoles(user);
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "redirect:/admin";
        }
        userService.createNewUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String updateUserOption(@ModelAttribute("user") @Valid User user,
                                   BindingResult bindingResult) {
        changeUsersRoles(user);
        updateValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "redirect:/admin";
        }
        if (user.getPassword().isEmpty()) {
            userService.updateUserPart(user);
        } else {
            userService.updateEntireUser(user);
        }
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String removeUser(@ModelAttribute("user") User user) {
        userService.removeUserById(user.getId());
        return "redirect:/admin";
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
}
