package org.example.rf.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.rf.model.User;
import org.example.rf.service.UserService;
import org.example.rf.util.HashPassword;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/admin/user/*"})
public class AdminUserServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<User> users = userService.getAllUsers();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/view/admin/user.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/user");
            return;
        }

        switch (path) {
            case "/update":
                updateUser(req, resp);
                break;
            case "/delete":
                deleteUser(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/admin/user");
        }
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Long userId = Long.parseLong(req.getParameter("userId"));
            String email = req.getParameter("email");
            String phone = req.getParameter("phone");
            String role = req.getParameter("role");
            String password = req.getParameter("password");

            User user = userService.getUserById(userId);
            if (user != null) {
                user.setEmail(email);
                user.setPhone(phone);
                user.setRole(User.Role.valueOf(role.toUpperCase()));
                if (password != null && !password.isEmpty()) {
                    user.setPassword(HashPassword.hashPassword(password));
                }
                userService.updateUser(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        resp.sendRedirect(req.getContextPath() + "/admin/user");
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Long userId = Long.parseLong(req.getParameter("userId"));
            userService.deleteUser(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.sendRedirect(req.getContextPath() + "/admin/user");
    }

    @Override
    public void destroy() {
        userService.close();
        super.destroy();
    }
}
