package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.rf.model.Plan;
import org.example.rf.model.User;
import org.example.rf.model.UserSubscription;
import org.example.rf.service.PlanService;
import org.example.rf.service.UserService;
import org.example.rf.service.UserSubscriptionService;
import org.example.rf.util.HashPassword;

import java.io.IOException;
import java.time.LocalDate;

import static org.example.rf.model.User.Role.ADMIN;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final UserService userService = new UserService();
    private final UserSubscriptionService userSubService = new UserSubscriptionService();
    private final PlanService planService = new PlanService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.contains("login")) {
            request.getSession().setAttribute("redirectUrl", referer);
        }
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userService.getUserByEmail(email);
        HttpSession session = request.getSession();

        if (user != null && user.getPassword().equals(HashPassword.hashPassword(password))) {
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId().toString());
            session.setMaxInactiveInterval(60 * 60); // 1 giờ

            Cookie emailCookie = new Cookie("userEmail", email);
            emailCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
            emailCookie.setHttpOnly(true);
            emailCookie.setPath(request.getContextPath());
            response.addCookie(emailCookie);

            if (user.getRole() == ADMIN) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                return;
            }

            String redirectUrl = (String) session.getAttribute("redirectUrl");
            if (redirectUrl != null && !redirectUrl.contains("register")) {
                session.removeAttribute("redirectUrl");
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }

        } else {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            request.setAttribute("error", "Sai email hoặc mật khẩu!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        userService.close();
        super.destroy();
    }
}
