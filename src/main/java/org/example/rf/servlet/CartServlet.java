package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.rf.model.Order;
import org.example.rf.model.OrderItem;
import org.example.rf.model.User;
import org.example.rf.service.OrderItemService;
import org.example.rf.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();
    private final OrderItemService orderItemService = new OrderItemService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Lấy danh sách sản phẩm từ đơn hàng đang chờ xử lý
        List<OrderItem> orderItems = orderService.getOrderItemsByUserId(user.getId());
        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }

        // Lấy đơn hàng đang pending nếu có
        Order order = orderService.findPendingOrderByUserId(user.getId());

        // Tính tổng tiền, nếu order == null thì để total = 0
        BigDecimal total = BigDecimal.ZERO;
        if (order != null) {
            total = orderService.calculateTotalAmount(order.getId());
        }

        // Đưa dữ liệu sang JSP
        request.setAttribute("order", order);
        request.setAttribute("orderItems", orderItems);
        request.setAttribute("total", total);

        // Forward tới trang cart.jsp
        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Long itemId = Long.parseLong(request.getParameter("id"));
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            // Xóa item
            orderItemService.deleteOrderItem(itemId);

            // Tìm lại order
            Order order = orderService.findPendingOrderByUserId(user.getId());

            BigDecimal newTotal = BigDecimal.ZERO;
            if (order != null) {
                newTotal = orderService.calculateTotalAmount(order.getId());
                order.setTotalAmount(newTotal);
                orderService.updateOrder(order);
            }

            // Response JSON
            out.print("{\"success\": true, \"newTotal\": " + newTotal + ", \"message\": \"Xóa sản phẩm thành công!\"}");
        } catch (Exception e) {
            out.print("{\"success\": false, \"error\": \"Có lỗi xảy ra khi xóa sản phẩm: " + e.getMessage() + "\"}");
        }
        out.flush();
    }
}
