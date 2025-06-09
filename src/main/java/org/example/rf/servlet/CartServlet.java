package org.example.rf.servlet;

package your.package.name;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.rf.model.OrderItem;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        // Lấy danh sách sản phẩm từ session
        List<OrderItem> orderItems = (List<OrderItem>) session.getAttribute("orderItems");

        // Nếu chưa có thì tạo mới (optional)
        if (orderItems == null) {
            orderItems = new java.util.ArrayList<>();
            session.setAttribute("orderItems", orderItems);
        }

        // Tính tổng tiền
        BigDecimal total = new BigDecimal(0);
        for (OrderItem item : orderItems) {
            total +=  item.getPrice() ;
        }

        // Đưa dữ liệu sang JSP
        request.setAttribute("orderItems", orderItems);
        request.setAttribute("total", total);

        // Forward tới trang cart.jsp
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }
}
