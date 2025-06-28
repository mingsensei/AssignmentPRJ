package org.example.rf.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.rf.model.User;
import org.example.rf.model.UserSubscription;
import org.example.rf.model.Plan;
import org.example.rf.service.UserSubscriptionService;
import org.example.rf.service.PlanService;

import java.io.IOException;

@WebFilter("/*") // Áp dụng cho mọi request
public class PlanUpdateFilter implements Filter {

    private final UserSubscriptionService userSubService = new UserSubscriptionService();
    private final PlanService planService = new PlanService();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        String path = request.getRequestURI();

        // Bỏ qua các tài nguyên tĩnh
        if (path.endsWith(".webp") || path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".png") ||
                path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".gif") ||
                path.endsWith(".svg") || path.endsWith(".woff2") || path.endsWith(".ttf")) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = request.getSession(false);

        if (session != null) {
            User user = (User) session.getAttribute("user");

            if (user != null) {
                UserSubscription activeSub = userSubService.findActiveSub(user.getId());

                if (activeSub != null) {
                    Plan currentPlan = planService.getPlanById(activeSub.getPlanId());
                    session.setAttribute("currentPlan", currentPlan);  // cập nhật mỗi request
                } else {
                    session.setAttribute("currentPlan", null);
                }
            }
        }

        chain.doFilter(req, res);
    }

}
