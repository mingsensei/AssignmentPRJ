package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.rf.model.Cart;
import org.example.rf.model.User;
import org.example.rf.service.PaymentService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
    PaymentService paymentService = new PaymentService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if ("pay".equals(action)) {
            Long id = Long.parseLong(request.getParameter("order"));
            BigDecimal amount = new BigDecimal(request.getParameter("amount"));
            amount = amount.divide(new BigDecimal(10));
            int rounded = amount.setScale(0, RoundingMode.HALF_UP).intValue(); // → 359
            // Create message with user ID and order ID
            String message = user.getId() + "_" + id+ "_" + amount;
            String amountString = Integer.toString(rounded);
            request.setAttribute("totalAmount", amountString);
            request.setAttribute("message", message);
            request.setAttribute("bankId", "mbbank");
            request.setAttribute("accountNo", "0773304009");
            request.setAttribute("template", "compact2");
            request.setAttribute("accountName", "DUONG HONG MINH");

            request.getRequestDispatcher("/payment.jsp").forward(request, response);
        } else if ("view".equals(action)) {
            // Lấy lịch sử thanh toán của user
            java.util.List<org.example.rf.model.Payment> paymentList = paymentService.getPaymentsByUserId(user.getId());
            request.setAttribute("paymentList", paymentList);
            System.out.println("PaymentList size: " + paymentList.size());
            request.getRequestDispatcher("/paymentview.jsp").forward(request, response);
        } else if ("payplan".equals(action)) {
            Long planId = Long.parseLong(request.getParameter("planId"));
            String planName = request.getParameter("planName");

            // Giá tiền theo planName (hoặc bạn có thể lấy từ DB nếu có lưu giá plan)
            BigDecimal amount;
            switch (planName) {
                case "Personal":
                    amount = new BigDecimal("199000");
                    break;
                case "Ultimate":
                    amount = new BigDecimal("399000");
                    break;
                default:
                    amount = new BigDecimal("0"); // Free hoặc lỗi
            }

            BigDecimal adjustedAmount = amount.divide(new BigDecimal(10));
            int rounded = adjustedAmount.setScale(0, RoundingMode.HALF_UP).intValue();

            String message = user.getId() + "_plan_" + planId + "_" + amount.intValue(); // ví dụ: 123_plan_2_199000

            request.setAttribute("totalAmount", String.valueOf(rounded));
            request.setAttribute("message", message);
            request.setAttribute("bankId", "mbbank");
            request.setAttribute("accountNo", "0773304009");
            request.setAttribute("template", "compact2");
            request.setAttribute("accountName", "DUONG HONG MINH");
            request.setAttribute("planName", planName);
            request.setAttribute("planId", planId);

            request.getRequestDispatcher("/payment.jsp").forward(request, response);
        }
        else {
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}