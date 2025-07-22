package org.example.rf.servlet.admin;

import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.rf.dao.OrderDAO;
import org.example.rf.dao.OrderItemDAO;
import org.example.rf.util.JPAUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashBoardServlet extends HttpServlet {

    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;

    @Override
    public void init() throws ServletException {
        this.orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        EntityManager em = JPAUtil.getEntityManager();
        try {
            this.orderItemDAO = new OrderItemDAO(em);
            // orderDAO đã được khởi tạo trong init()

            // === TÍNH TOÁN DỮ LIỆU CHO CÁC CARD ===

            // 1. Doanh thu từ Course
            BigDecimal courseRevenue = orderItemDAO.calculateTotalRevenue();
            // 2. Doanh thu từ Plan
            BigDecimal planRevenue = orderDAO.calculateTotalRevenueFromPlans();
            // 3. TỔNG DOANH THU
            BigDecimal totalRevenue = courseRevenue.add(planRevenue);

            // 4. Số khóa học đã bán
            long coursesSold = orderItemDAO.countAllSoldItems();

            // 5. SỐ GÓI PLAN ĐÃ BÁN (Mới)
            long plansSold = orderDAO.countCompletedPlanOrders();

            // 6. Tổng số khách hàng
            long totalParticipants = orderDAO.countTotalParticipants();

            // Gửi tất cả dữ liệu sang JSP
            req.setAttribute("totalRevenue", totalRevenue);
            req.setAttribute("coursesSold", coursesSold);
            req.setAttribute("plansSold", plansSold); // Dữ liệu mới
            req.setAttribute("totalParticipants", totalParticipants);


            // === PHẦN XỬ LÝ CHO CHART (GIỮ NGUYÊN) ===
            Gson gson = new Gson();
            int currentYear = LocalDate.now().getYear();
            List<Object[]> monthlyDataFromDB = orderItemDAO.getMonthlyRevenueForYear(currentYear);

            BigDecimal[] monthlyRevenues = new BigDecimal[12];
            Arrays.fill(monthlyRevenues, BigDecimal.ZERO);

            for (Object[] result : monthlyDataFromDB) {
                Integer month = (Integer) result[0];
                BigDecimal revenue = (BigDecimal) result[1];
                if (month != null && revenue != null) {
                    monthlyRevenues[month - 1] = revenue;
                }
            }

            String areaChartLabelsJson = gson.toJson(new String[]{
                    "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
                    "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
            });
            String areaChartDataJson = gson.toJson(monthlyRevenues);

            req.setAttribute("areaChartLabels", areaChartLabelsJson);
            req.setAttribute("areaChartData", areaChartDataJson);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Lỗi khi tải dữ liệu thống kê.");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        req.getRequestDispatcher("/view/admin/dashboard.jsp").forward(req, resp);
    }
}