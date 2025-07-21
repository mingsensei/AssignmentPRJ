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
            Gson gson = new Gson(); // Khai báo Gson một lần ở đầu

            // =================== PHẦN 1: THỐNG KÊ CÁC CARD ===================
            BigDecimal totalRevenue = orderItemDAO.calculateTotalRevenue();
            long coursesSold = orderItemDAO.countAllSoldItems();
            long totalParticipants = orderDAO.countTotalParticipants();

            req.setAttribute("totalRevenue", totalRevenue);
            req.setAttribute("coursesSold", coursesSold);
            req.setAttribute("totalParticipants", totalParticipants);

            // =================== PHẦN 2: DỮ LIỆU CHO AREA CHART (DOANH THU THEO THÁNG) ===================
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

            // =================== PHẦN 3: DỮ LIỆU CHO PIE CHART (TOP 5 KHÓA HỌC) ===================
            List<Object[]> revenuePerCourse = orderItemDAO.getRevenuePerCourse();

            List<String> pieLabelsList = new ArrayList<>();
            List<BigDecimal> pieDataList = new ArrayList<>();
            BigDecimal otherRevenue = BigDecimal.ZERO;

            for (int i = 0; i < revenuePerCourse.size(); i++) {
                Object[] result = revenuePerCourse.get(i);
                String courseName = (String) result[0];
                BigDecimal revenue = (BigDecimal) result[1];

                if (i < 5) {
                    pieLabelsList.add(courseName);
                    pieDataList.add(revenue);
                } else {
                    otherRevenue = otherRevenue.add(revenue);
                }
            }

            if (otherRevenue.compareTo(BigDecimal.ZERO) > 0) {
                pieLabelsList.add("Các khóa học khác");
                pieDataList.add(otherRevenue);
            }

            String pieChartLabelsJson = gson.toJson(pieLabelsList);
            String pieChartDataJson = gson.toJson(pieDataList);

            req.setAttribute("pieChartLabels", pieChartLabelsJson);
            req.setAttribute("pieChartData", pieChartDataJson);

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