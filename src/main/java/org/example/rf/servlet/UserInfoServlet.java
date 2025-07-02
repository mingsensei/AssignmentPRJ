package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.example.rf.model.Plan;
import org.example.rf.model.User;
import org.example.rf.model.UserSubscription;
import org.example.rf.service.*;

import java.io.IOException;

@WebServlet("/user-info")
public class UserInfoServlet extends HttpServlet {
    private final PlanService planService = new PlanService();
    private final UserSubscriptionService subscriptionService = new UserSubscriptionService();
    private final TestAttemptService testAttemptService = new TestAttemptService();
    private final UserPostService userPostService = new UserPostService();

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        // Khởi tạo UserService (tùy cách bạn implement)
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("user");
        request.setAttribute("user", user);

        // ➤ Lấy subscription hiện tại
        UserSubscription currentSub = subscriptionService.findActiveSub(user.getId());

        if (currentSub != null) {
            Plan currentPlan = planService.getPlanById(currentSub.getPlanId());
            long examCount = testAttemptService.getAllTestAttempts().stream()
                    .filter(t -> t.getUserId().equals(user.getId()))
                    .count();
            long postCount = userPostService.getAllPosts().stream()
                    .filter(p -> p.getUserId().equals(user.getId()))
                    .count();

            request.setAttribute("currentPlan", currentPlan);
            request.setAttribute("currentSubscription", currentSub);
            request.setAttribute("examCount", examCount);
            request.setAttribute("postCount", postCount);
        }

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

        if ("updateInfo".equals(action)) {
            updateUserInfo(request, response, user);
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
            request.getRequestDispatcher("/user_info.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mật khẩu mới và xác nhận
        if (newPass == null || !newPass.equals(confirmPass)) {
            request.setAttribute("error", "Mật khẩu mới và xác nhận không khớp");
            request.getRequestDispatcher("/user_info.jsp").forward(request, response);
            return;
        }

        // Cập nhật mật khẩu mới
        userService.updatePassword(user.getId(), newPass);

        // Có thể cập nhật lại thông tin user trong session nếu cần

        request.setAttribute("message", "Cập nhật mật khẩu thành công");
        request.getRequestDispatcher("/user_info.jsp").forward(request, response);
    }
    private void updateUserInfo(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String phone = request.getParameter("phone");
        String userName = request.getParameter("userName");

        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPhone(phone);
        user.setUserName(userName);

        boolean success = userService.updateUser(user);

        if (success) {
            request.getSession().setAttribute("user", user);
            request.setAttribute("message", "Cập nhật thông tin thành công");
        } else {
            request.setAttribute("error", "Cập nhật thông tin thất bại");
        }
        request.setAttribute("user", user);
        request.getRequestDispatcher("user_info.jsp").forward(request, response);
    }

}
