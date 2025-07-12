package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.rf.model.User;
import org.example.rf.service.UserService;
import org.example.rf.util.HashPassword;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    // Khởi tạo UserService 1 lần, dùng lại cho cả servlet
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy thông tin từ form
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Kiểm tra xem email đã tồn tại chưa (có thể thêm)
        User existingUser = userService.getUserByEmail(email);
        if (existingUser != null) {
            request.setAttribute("error", "Email đã được sử dụng!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Tạo mới user và hash password
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(HashPassword.hashPassword(password));
        user.setRole(User.Role.STUDENT);

        try {
            userService.createUser(user);
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đăng ký thất bại!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        userService.close();
        super.destroy();
    }
}
