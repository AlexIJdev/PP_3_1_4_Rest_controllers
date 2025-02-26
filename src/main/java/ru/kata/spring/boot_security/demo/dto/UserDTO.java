package ru.kata.spring.boot_security.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public class UserDTO {
    @NotEmpty(message = "First Name should not be empty!")
    @Size(min = 2, max = 30, message = "First Name should be between 2 and 30 characters!")
    private String firstName;

    @NotEmpty(message = "Last Name should not be empty!")
    @Size(min = 2, max = 50, message = "Last Name should be between 2 and 50 characters!")
    private String lastName;

    @Min(value = 0, message = "Age should be greater than 0!")
    private int age;

    @NotEmpty(message = "Email should not be empty!")
    @Email(message = "Email should be valid!")
    private String email;

    private String password;

    private List<Role> roles;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
