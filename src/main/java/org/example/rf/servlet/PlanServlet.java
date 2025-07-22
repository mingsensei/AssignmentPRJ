package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.rf.model.Plan;
import org.example.rf.model.User;
import org.example.rf.model.UserSubscription;
import org.example.rf.service.PlanService;
import org.example.rf.service.UserSubscriptionService;

import java.io.IOException;
import java.util.List;

@WebServlet("/plan-pricing")
public class PlanServlet extends HttpServlet {

    private final PlanService planService = new PlanService();
    private final UserSubscriptionService userSubscriptionService = new UserSubscriptionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy tất cả các gói plan có sẵn để hiển thị
        List<Plan> planList = planService.getAllPlans();

        // 2. Mặc định plan hiện tại là "Free"
        String currentPlanName = "Free";

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            UserSubscription activeSub = userSubscriptionService.findActiveSub(user.getId());

            if (activeSub != null) {
                // ĐÃ SỬA: Dùng planId từ activeSub để lấy thông tin Plan đầy đủ
                // Đây là cách làm đúng với model UserSubscription hiện tại của bạn.
                Plan currentPlan = planService.getPlanById(activeSub.getPlanId());
                if (currentPlan != null) {
                    currentPlanName = currentPlan.getName();
                }
            }
        }

        // 3. Gửi dữ liệu sang JSP
        request.setAttribute("currentPlanName", currentPlanName);
        request.setAttribute("plans", planList);
        request.getRequestDispatcher("/plan.jsp").forward(request, response);
    }
}