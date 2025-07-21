package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.rf.model.Course;
import org.example.rf.model.User;
import org.example.rf.service.CourseService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/cart/*")
public class CartServlet extends HttpServlet {

    private final CourseService courseService = new CourseService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = getAction(request);

        if (pathInfo.equals("/") || pathInfo.isEmpty()) {
            showCartPage(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = getAction(request);

        switch (pathInfo) {
            case "/add":
                addCourseToCart(request, response);
                break;
            case "/remove":
                removeCourseFromCart(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                break;
        }
    }

    private void showCartPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<Long, Course> cart = (Map<Long, Course>) session.getAttribute("cart");

        if (cart == null) {
            cart = new HashMap<>();
        }

        Collection<Course> cartItems = cart.values();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Course item : cartItems) {
            totalAmount = totalAmount.add(new BigDecimal(item.getPrice()));
        }

        request.setAttribute("cartItems", cartItems);
        request.setAttribute("totalAmount", totalAmount);

        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    private void addCourseToCart(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
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

            Map<Long, Course> cart = (Map<Long, Course>) session.getAttribute("cart");
            if (cart == null) {
                cart = new HashMap<>();
            }

            cart.put(course.getId(), course);
            session.setAttribute("cart", cart);

            // Chuyển hướng đến trang hiển thị giỏ hàng sau khi thêm
            response.sendRedirect(request.getContextPath() + "/cart");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid course ID");
        }
    }

    private void removeCourseFromCart(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Long courseId = Long.parseLong(request.getParameter("courseId"));
            HttpSession session = request.getSession();

            Map<Long, Course> cart = (Map<Long, Course>) session.getAttribute("cart");

            if (cart != null && cart.containsKey(courseId)) {
                cart.remove(courseId);
                session.setAttribute("cart", cart); // Cập nhật lại session
            }

            BigDecimal newTotal = BigDecimal.ZERO;
            if (cart != null) {
                for (Course item : cart.values()) {
                    newTotal = newTotal.add(new BigDecimal(item.getPrice()));
                }
            }

            out.print("{\"success\": true, \"newTotal\": " + newTotal + "}");

        } catch (Exception e) {
            out.print("{\"success\": false, \"error\": \"Lỗi: " + e.getMessage() + "\"}");
        }
        out.flush();
    }

    private String getAction(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            return "/";
        }
        return pathInfo;
    }
}