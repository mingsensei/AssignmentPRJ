package org.example.rf.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.rf.model.Order;
import org.example.rf.service.OrderService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(urlPatterns = {"/admin/order/*"})
public class AdminOrderServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Order> orders = orderService.getAllOrders();
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/view/admin/order.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        switch (path) {
            case "/update":
                updateOrder(req, resp);
                break;
            case "/delete":
                deleteOrder(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/admin/order");
        }
    }

    private void updateOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long orderId = Long.parseLong(req.getParameter("orderId"));
        String newStatus = req.getParameter("status");
        String amountStr = req.getParameter("totalAmount");

        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            if (newStatus != null) {
                order.setStatus(newStatus);
            }
            if (amountStr != null) {
                try {
                    order.setTotalAmount(new BigDecimal(amountStr));
                } catch (NumberFormatException ignored) {}
            }
            orderService.updateOrder(order);
        }

        resp.sendRedirect(req.getContextPath() + "/admin/order");
    }

    private void deleteOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long orderId = Long.parseLong(req.getParameter("orderId"));
        orderService.deleteOrder(orderId);
        resp.sendRedirect(req.getContextPath() + "/admin/order");
    }
}

