
package com.app.controller;

import com.app.dao.UserDAO;
import com.app.model.User;
import com.app.security.PasswordUtil;
import com.app.service.ValidationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private final ValidationService vs = new ValidationService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = vs.sanitize(req.getParameter("username"));
        String email = vs.sanitize(req.getParameter("email"));
        String password = req.getParameter("password");

        List<String> errors = new ArrayList<>(vs.validateRegistration(username, email, password));
        try {
            Optional<User> existing = userDAO.findByUsername(username);
            if (existing.isPresent()) errors.add("Username already exists.");
        } catch (Exception e) {
            log("Register lookup error: " + e.getMessage(), e);
            errors.add("Server error. Try again.");
        }
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
            return;
        }

        try {
            User u = new User();
            u.setUsername(username);
            u.setEmail(email);
            u.setPasswordHash(PasswordUtil.hash(password));
            u.setRole("USER");
            u.setPhone("");
            u.setPreferencesJson("{}");
            userDAO.create(u);
            resp.sendRedirect(req.getContextPath() + "/login?registered=1");
        } catch (Exception e) {
            log("Register error: " + e.getMessage(), e);
            errors.add("Server error. Please try again later.");
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
        }
    }
}