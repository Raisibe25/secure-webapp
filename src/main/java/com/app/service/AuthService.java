package com.app.service;

import com.app.dao.UserDAO;
import com.app.model.User;
import com.app.security.PasswordUtil;

import java.util.Optional;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public Optional<User> authenticate(String username, String password) throws Exception {
        Optional<com.app.model.User> opt = userDAO.findByUsername(username);
        if (opt.isPresent() && PasswordUtil.verify(password, opt.get().getPasswordHash())) {
            return opt;
        }
        return Optional.empty();
    }
}