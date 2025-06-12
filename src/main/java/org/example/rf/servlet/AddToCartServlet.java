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
import org.example.rf.service.CourseService;
import org.example.rf.service.OrderItemService;
import org.example.rf.service.OrderService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@WebServlet("/add-to-cart")
public class AddToCartServlet extends HttpServlet {
    private CourseService courseService;
    private OrderService orderService;
    private OrderItemService orderItemService;

    @Override
    public void init() throws ServletException {
        courseService = new CourseService();
        orderService = new OrderService();
        orderItemService = new OrderItemService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Long courseId = Long.parseLong(request.getParameter("courseId"));
            Course course = courseService.getCourseById(courseId);

            if (course == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Course not found");
                return;
            }

            // Tìm order pending của user
            Order pendingOrder = orderService.findPendingOrderByUserId(user.getId());

            // Tạo order mới nếu chưa có
            if (pendingOrder == null) {
                pendingOrder = new Order();
                pendingOrder.setUser(user);
                pendingOrder.setStatus("pending");
                pendingOrder.setCreatedAt(LocalDateTime.now());
                pendingOrder.setTotalAmount(BigDecimal.ZERO);
                pendingOrder = orderService.createOrder(pendingOrder);
            }

            // Kiểm tra xem course đã có trong order chưa
            if (!orderItemService.existsByOrderIdAndCourseId(pendingOrder.getId(), courseId)) {
                // Tạo order item mới
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(pendingOrder);
                orderItem.setCourse(course);
                orderItem.setPrice(new BigDecimal(course.getPrice().toString()));
                orderItemService.createOrderItem(orderItem);

                // Cập nhật tổng tiền của order
                pendingOrder.setTotalAmount(orderService.calculateTotalAmount(pendingOrder.getId()));
                orderService.updateOrder(pendingOrder);
            }

            // Chuyển hướng đến trang giỏ hàng
            response.sendRedirect(request.getContextPath() + "/cart");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid course ID");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error adding course to cart");
        }
    }
} 