package ru.kata.spring.boot_security.demo.util;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

public class AuthException extends InternalAuthenticationServiceException {
    public AuthException(String message) {
        super(message);
    }
}
