package org.example.rf.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.rf.model.User;

import java.io.IOException;
import java.util.regex.Pattern;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    private static final Pattern PUBLIC_PATHS = Pattern.compile(
            "^(/|/home|/category|/course(\\?courseid=\\d+)?)$"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        boolean isLoggedIn = (user != null);
        boolean isPublic = PUBLIC_PATHS.matcher(path).matches();

        if (!isLoggedIn && !isPublic) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}

