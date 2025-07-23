//package org.example.rf.config.filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.example.rf.model.User;
//
//import java.io.IOException;
//import java.util.regex.Pattern;
//
//@WebFilter("/*")
//public class AuthenticationFilter implements Filter {
//
//    // Các đường dẫn không yêu cầu đăng nhập
//    private static final Pattern PUBLIC_PATHS = Pattern.compile(
//            "^(/|/home|/login|/register|/category|/course(\\?courseid=\\d+)?|/forgot-password|/reset-password)$"
//    );
//
//    // Các phần mở rộng tệp tĩnh cần được bỏ qua
//    private static final String[] STATIC_EXTENSIONS = {
//            ".css", ".js", ".png", ".jpg", ".jpeg", ".gif", ".svg",
//            ".woff", ".woff2", ".ttf", ".eot", ".ico", ".webp"
//    };
//
//    private boolean isStaticResource(String path) {
//        for (String ext : STATIC_EXTENSIONS) {
//            if (path.endsWith(ext)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse resp = (HttpServletResponse) response;
//
//        String path = req.getRequestURI().substring(req.getContextPath().length());
//
//        // Bỏ qua nếu là tài nguyên tĩnh
//        if (isStaticResource(path)) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        HttpSession session = req.getSession(false);
//        User user = (session != null) ? (User) session.getAttribute("user") : null;
//
//        boolean isLoggedIn = (user != null);
//        boolean isPublic = PUBLIC_PATHS.matcher(path).matches();
//
//        // Nếu chưa đăng nhập và không phải trang public thì chuyển hướng về /login
//        if (!isLoggedIn && !isPublic) {
//            resp.sendRedirect(req.getContextPath() + "/login");
//            return;
//        }
//
//        // Cho phép request tiếp tục
//        chain.doFilter(request, response);
//    }
//}
