package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.example.rf.model.User;
import org.example.rf.service.UserService;
import org.example.rf.util.HashPassword;
import org.example.rf.util.InputValidator;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

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
        String rePassword = request.getParameter("re_password");

        // Validate input
        if (InputValidator.isEmpty(firstName) || InputValidator.isEmpty(lastName)) {
            request.setAttribute("error", "Họ và tên không được để trống.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (!InputValidator.isAlphabetic(firstName) || !InputValidator.isAlphabetic(lastName)) {
            request.setAttribute("error", "Họ và tên chỉ được chứa chữ cái.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (!InputValidator.isValidEmail(email)) {
            request.setAttribute("error", "Email không hợp lệ.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (!InputValidator.isStrongPassword(password)) {
            request.setAttribute("error", "Mật khẩu yếu. Cần ít nhất 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (!InputValidator.match(password, rePassword)) {
            request.setAttribute("error", "Mật khẩu nhập lại không khớp.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra email đã tồn tại
        if (userService.getUserByEmail(email) != null) {
            request.setAttribute("error", "Email đã được sử dụng!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Format lại tên trước khi lưu
        String fullFirstName = InputValidator.formatName(firstName);
        String fullLastName = InputValidator.formatName(lastName);

        // Tạo user
        User user = new User();
        user.setFirstName(fullFirstName);
        user.setLastName(fullLastName);
        user.setEmail(email);
        user.setPassword(HashPassword.hashPassword(password));
        user.setRole(User.Role.STUDENT);

        try {
            userService.createUser(user);
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đăng ký thất bại. Vui lòng thử lại!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        userService.close();
        super.destroy();
    }
}
