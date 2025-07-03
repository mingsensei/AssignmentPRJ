package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.rf.service.PaymentService;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/processPayment")
public class ProcessPaymentServlet extends HttpServlet {
    PaymentService paymentService = new PaymentService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        Long userId = Long.parseLong(request.getParameter("userId"));
        BigDecimal amount = new BigDecimal(request.getParameter("amount"));

        if (action == null || userId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số cần thiết.");
            return;
        }

        switch (action) {
            case "payPlan":
                Long planId = Long.parseLong(request.getParameter("planId"));
                if (planId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu planId cho hành động payPlan.");
                    return;
                }
                // TODO: Xử lý thanh toán gói dịch vụ (plan) tại đây
                paymentService.createPaymentForPlan(userId,planId,amount,"MB");
                // Redirect sau khi thanh toán
                response.sendRedirect("payment?action=view");
                break;

            case "payOrder":
                Long orderId = Long.parseLong(request.getParameter("orderId"));
                if (orderId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu orderId cho hành động payOrder.");
                    return;
                }

                // TODO: Xử lý thanh toán đơn hàng tại đây
                paymentService.createPaymentForOrder(userId,orderId,amount,"MB");
                // Redirect sau khi thanh toán
                response.sendRedirect("payment?action=view");
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
        }
    }
}
