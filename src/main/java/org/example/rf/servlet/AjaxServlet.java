package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.rf.config.vnpay.ConfigVnpay;
import org.example.rf.model.Course;
import org.example.rf.model.Plan;
import org.example.rf.model.User;
import org.example.rf.service.OrderService;
import org.example.rf.service.PlanService; // Thêm service cho Plan

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/vn_pay/ajax")
public class AjaxServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final PlanService planService = new PlanService(); // Khởi tạo PlanService

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String paymentType = req.getParameter("paymentType");

        // Dựa vào paymentType để gọi phương thức xử lý tương ứng
        if ("plan".equals(paymentType)) {
            processPlanPayment(req, resp);
        } else {
            // Mặc định là xử lý thanh toán khóa học từ giỏ hàng
            processCoursePayment(req, resp);
        }
    }

    // Luồng 1: Xử lý thanh toán cho giỏ hàng (COURSE)
    private void processCoursePayment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("/login");
            return;
        }

        if (req.getParameter("totalBill") == null) {
            resp.sendRedirect("/cart");
            return;
        }

        double amountDouble = Double.parseDouble(req.getParameter("totalBill"));
        Map<Long, Course> cart = (Map<Long, Course>) session.getAttribute("cart");

        Long orderId = orderService.createNewOrderByVnpay(user.getId(), amountDouble, cart);
        if (orderId == null || orderId < 1) {
            resp.sendRedirect("/cart");
            return;
        }

        // Chuyển hướng đến VNPAY
        redirectToVnpay(orderId, BigDecimal.valueOf(amountDouble), req, resp);
    }

    // Luồng 2: Xử lý thanh toán cho PLAN
    private void processPlanPayment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            Long planId = Long.parseLong(req.getParameter("planId"));
            Plan plan = planService.getPlanById(planId);
            if (plan == null) {
                resp.sendRedirect(req.getContextPath() + "/plan-pricing?error=notfound");
                return;
            }

            Long orderId = orderService.createNewPlanOrder(user, plan);
            if (orderId == null || orderId < 1) {
                resp.sendRedirect(req.getContextPath() + "/plan-pricing?error=create_failed");
                return;
            }

            // Chuyển hướng đến VNPAY
            redirectToVnpay(orderId, plan.getPrice(), req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/plan-pricing?error=invalid_plan");
        }
    }

    // Phương thức chung để tạo URL và chuyển hướng VNPAY (để tránh lặp code)
    private void redirectToVnpay(Long orderId, BigDecimal amount, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amountLong = amount.multiply(BigDecimal.valueOf(100)).longValue();
        String bankCode = req.getParameter("bankCode");

        String vnp_TxnRef = orderId.toString();
        String vnp_IpAddr = ConfigVnpay.getIpAddress(req);
        String vnp_TmnCode = ConfigVnpay.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amountLong));
        vnp_Params.put("vnp_CurrCode", "VND");
        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", ConfigVnpay.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = ConfigVnpay.hmacSHA512(ConfigVnpay.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = ConfigVnpay.vnp_PayUrl + "?" + queryUrl;
        resp.sendRedirect(paymentUrl);
    }
}