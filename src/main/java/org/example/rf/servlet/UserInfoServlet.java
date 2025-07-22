package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.example.rf.model.Plan;
import org.example.rf.model.User;
import org.example.rf.model.UserSubscription;
import org.example.rf.service.*;

import org.example.rf.util.InputValidator;

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("user");

        if ("updateInfo".equals(action)) {
            updateUserInfo(request, response, user);
        } else if ("updatePassword".equals(action)) {
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

        if (!userService.checkPassword(user.getId(), currentPass)) {
            request.setAttribute("error", "Mật khẩu hiện tại không đúng.");
            request.getRequestDispatcher("/user_info.jsp").forward(request, response);
            return;
        }

        if (InputValidator.isEmpty(newPass) || !InputValidator.match(newPass, confirmPass)) {
            request.setAttribute("error", "Mật khẩu mới và xác nhận không khớp.");
            request.getRequestDispatcher("/user_info.jsp").forward(request, response);
            return;
        }

        if (!InputValidator.isStrongPassword(newPass)) {
            request.setAttribute("error", "Mật khẩu quá yếu. Cần ít nhất 8 ký tự, gồm chữ hoa, thường, số và ký tự đặc biệt.");
            request.getRequestDispatcher("/user_info.jsp").forward(request, response);
            return;
        }

        userService.updatePassword(user.getId(), newPass);
        request.setAttribute("message", "Cập nhật mật khẩu thành công.");
        request.getRequestDispatcher("/user_info.jsp").forward(request, response);
    }

    private void updateUserInfo(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String phone = request.getParameter("phone");
        String userName = request.getParameter("userName");

        // Validate
        if (InputValidator.isEmpty(firstName) || InputValidator.isEmpty(lastName) || InputValidator.isEmpty(userName)) {
            request.setAttribute("error", "Họ, tên và tên người dùng không được để trống.");
            request.setAttribute("user", user);
            request.getRequestDispatcher("user_info.jsp").forward(request, response);
            return;
        }

        if (!InputValidator.isAlphabetic(firstName) || !InputValidator.isAlphabetic(lastName)) {
            request.setAttribute("error", "Họ và tên chỉ được chứa chữ cái.");
            request.setAttribute("user", user);
            request.getRequestDispatcher("user_info.jsp").forward(request, response);
            return;
        }

        if (!InputValidator.isValidPhone(phone)) {
            request.setAttribute("error", "Số điện thoại không hợp lệ.");
            request.setAttribute("user", user);
            request.getRequestDispatcher("user_info.jsp").forward(request, response);
            return;
        }

        // Format lại tên
        user.setLastName(InputValidator.formatName(lastName));
        user.setFirstName(InputValidator.formatName(firstName));
        user.setPhone(phone);
        user.setUserName(userName);

        boolean success = userService.updateUser(user);

        if (success) {
            request.getSession().setAttribute("user", user);
            request.setAttribute("message", "Cập nhật thông tin thành công.");
        } else {
            request.setAttribute("error", "Cập nhật thông tin thất bại.");
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("user_info.jsp").forward(request, response);
    }
}
