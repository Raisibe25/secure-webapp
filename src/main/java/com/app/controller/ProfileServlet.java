package com.app.controller;

import com.app.dao.UserDAO;
import com.app.model.User;
import com.app.service.ValidationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private final ValidationService vs = new ValidationService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User sessionUser = (User) (session != null ? session.getAttribute("user") : null);
        if (sessionUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String email = vs.sanitize(req.getParameter("email"));
        String first = vs.sanitize(req.getParameter("firstName"));
        String last = vs.sanitize(req.getParameter("lastName"));
        String phone = vs.sanitize(req.getParameter("phone"));
        String prefs = vs.sanitize(req.getParameter("preferencesJson"));

        sessionUser.setEmail(email);
        sessionUser.setFirstName(first);
        sessionUser.setLastName(last);
        sessionUser.setPhone(phone);
        sessionUser.setPreferencesJson(prefs);

        try {
            userDAO.updateProfile(sessionUser);
            req.setAttribute("message", "Profile updated.");
            req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
