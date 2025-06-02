package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.example.rf.model.User;
import org.example.rf.service.UserService;

import java.io.IOException;

@WebServlet("/user-info")
public class UserInfoServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        // Khởi tạo UserService (tùy cách bạn implement)
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy user từ session
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        // Truyền user vào request để hiển thị trên JSP
        request.setAttribute("user", user);
        request.getRequestDispatcher("user_info.jsp").forward(request, response);
    }

    // Xử lý POST (cập nhật mật khẩu và logout)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        if ("updatePassword".equals(action)) {
            updatePassword(request, response, user);
        } else if ("logout".equals(action)) {
            response.sendRedirect("logout");
        } else {
            response.sendRedirect("user-info");
        }
    }

    private void updatePassword(HttpServletRequest request, HttpServletResponse response, User user) throws IOException, ServletException {
        String currentPass = request.getParameter("currentPass");
        String newPass = request.getParameter("newPass");
        String confirmPass = request.getParameter("confirmPass");

        // Kiểm tra mật khẩu hiện tại
        if (!userService.checkPassword(user.getId(), currentPass)) {
            request.setAttribute("error", "Mật khẩu hiện tại không đúng");
            request.getRequestDispatcher("/user-info.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mật khẩu mới và xác nhận
        if (newPass == null || !newPass.equals(confirmPass)) {
            request.setAttribute("error", "Mật khẩu mới và xác nhận không khớp");
            request.getRequestDispatcher("/user-info.jsp").forward(request, response);
            return;
        }

        // Cập nhật mật khẩu mới
        userService.updatePassword(user.getId(), newPass);

        // Có thể cập nhật lại thông tin user trong session nếu cần

        request.setAttribute("message", "Cập nhật mật khẩu thành công");
        request.getRequestDispatcher("/user-info.jsp").forward(request, response);
    }
}
