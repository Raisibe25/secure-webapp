package com.app.service;

import java.util.ArrayList;
import java.util.List;

public class ValidationService {
    public List<String> validateRegistration(String username, String email, String password) {
        List<String> errors = new ArrayList<>();
        if (username == null || !username.matches("^[a-zA-Z0-9_]{3,30}$")) {
            errors.add("Invalid username.");
        }
        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            errors.add("Invalid email.");
        }
        if (password == null || password.length() < 8) {
            errors.add("Password must be at least 8 characters.");
        }
        return errors;
    }

    public String sanitize(String input) {
        return input == null ? "" : input.trim();
    }
}
