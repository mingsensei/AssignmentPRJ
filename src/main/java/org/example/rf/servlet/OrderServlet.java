package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.rf.model.Course;
import org.example.rf.model.Order;
import org.example.rf.model.OrderItem;
import org.example.rf.model.User;
import org.example.rf.service.OrderItemService;
import org.example.rf.service.OrderService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@WebServlet("/order/*")
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final OrderItemService orderItemService = new OrderItemService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = getAction(request);

        switch (pathInfo) {
            case "/history":
                showOrderHistory(request, response);
                break;
            case "/view":
                showOrderDetail(request, response);
                break;
        }
    }

    private void showOrderDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Long orderId = Long.parseLong(request.getParameter("id"));

            List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(orderId);

            if (orderId >= 1) {
                // 3. Gửi đối tượng order đã có đủ dữ liệu sang JSP
                request.setAttribute("orderItems", orderItems);
                request.getRequestDispatcher("/order-detail.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Order not found or access denied.");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not retrieve order details.");
        }
    }

    private void showOrderHistory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            List<Order> orderList = orderService.getOrdersByUserId(user.getId());
            request.setAttribute("orderList", orderList);
            request.getRequestDispatcher("/order-history.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not retrieve order history.");
        }
    }

    private String getAction(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            return "/";
        }
        return pathInfo;
    }
}