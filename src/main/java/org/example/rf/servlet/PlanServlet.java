package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.rf.model.Plan;
import org.example.rf.model.User;
import org.example.rf.model.UserSubscription;
import org.example.rf.service.PlanService;
import org.example.rf.service.UserSubscriptionService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/plan-pricing")
public class PlanServlet extends HttpServlet {

    private final PlanService planService = new PlanService();
    private final UserSubscriptionService userSubscriptionService = new UserSubscriptionService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Plan> planList = planService.getAllPlans();
        String currentPlanName = null;
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            UserSubscription activeSub = userSubscriptionService.findActiveSub(user.getId());
            if (activeSub != null) {
                Plan currentPlan = planService.getPlanById(activeSub.getPlanId());
                currentPlanName = currentPlan.getName();
            }
        }

        request.setAttribute("currentPlanName", currentPlanName);
        request.setAttribute("plans", planList);
        request.getRequestDispatcher("/plan.jsp").forward(request, response);
    }

    @Override
    public void destroy() {
        planService.close();
    }
}
