package org.example.rf.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.rf.model.User;

import java.io.IOException;

import static org.example.rf.model.User.Role.ADMIN;

@WebFilter("/admin/*")
public class RoleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        if (user.getRole() != ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        chain.doFilter(request, response);
    }
}

