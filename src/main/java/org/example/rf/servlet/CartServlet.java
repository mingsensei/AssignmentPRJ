package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.rf.model.OrderItem;
import org.example.rf.model.User;
import org.example.rf.service.OrderItemService;
import org.example.rf.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private BigDecimal total = new BigDecimal(0);
    private OrderService orderService = new OrderService();
    private OrderItemService orderItemService ;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                orderItemService = new OrderItemService();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        // Lấy danh sách sản phẩm từ session
        List<OrderItem> orderItems = orderService.getOrderItemsByUserId(user.getId());

        // Nếu chưa có thì tạo mới (optional)
        if (orderItems == null) {
            orderItems = new java.util.ArrayList<>();
        }

        // Tính tổng tiền
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            total = total.add(item.getPrice());
        }

        // Đưa dữ liệu sang JSP
        request.setAttribute("orderItems", orderItems);
        request.setAttribute("total", total);

        // Forward tới trang cart.jsp
        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        orderItemService = new OrderItemService();
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Lấy ID của item cần xóa từ request
            Long itemId = Long.parseLong(request.getParameter("id"));
            
            // Lấy thông tin user từ session
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            // Xóa item khỏi giỏ hàng
            OrderItemService orderItemService = new OrderItemService();
            orderItemService.deleteOrderItem(itemId);
            
            // Lấy danh sách item mới và tính tổng tiền
            List<OrderItem> orderItems = orderService.getOrderItemsByUserId(user.getId());
            BigDecimal newTotal = BigDecimal.ZERO;
            for (OrderItem item : orderItems) {
                newTotal = newTotal.add(item.getPrice());
            }
            
            // Gửi response về client
            out.print("{\"success\": true, \"newTotal\": " + newTotal + ", \"message\": \"Xóa sản phẩm thành công!\"}");
        } catch (Exception e) {
            out.print("{\"success\": false, \"error\": \"Có lỗi xảy ra khi xóa sản phẩm: " + e.getMessage() + "\"}");
        }
        out.flush();
    }
}
