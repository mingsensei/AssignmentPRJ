<%
    String uri1 = request.getRequestURI();
    boolean skipFooter = uri1.contains("login.jsp") || uri1.contains("register.jsp");

    if (!skipFooter) {
%>
<footer class="custom-footer">
    <div class="footer-container">
        <div class="footer-top">
            <div class="footer-logo">
                <a href="#">Edu AI</a>
            </div>
            <ul class="social-icons">
                <li><a href="#" class="fb">Facebook</a></li>
                <li><a href="#" class="tw">Twitter</a></li>
                <li><a href="#" class="in">Instagram</a></li>
                <li><a href="#" class="be">Behance</a></li>
                <li><a href="#" class="dr">Dribbble</a></li>
                <li><a href="#" class="yt">YouTube</a></li>
            </ul>
        </div>

        <div class="footer-middle">
            <ul class="nav-links nav-left">
                <li><a href="#">Privacy</a></li>
                <li><a href="#">Policy</a></li>
            </ul>
            <ul class="nav-links nav-right">
                <li><a href="#">Home</a></li>
                <li><a href="#">Our Works</a></li>
                <li><a href="#">About</a></li>
                <li><a href="#">Blog</a></li>
                <li><a href="#">Contact</a></li>
            </ul>
        </div>

        <div class="footer-bottom">
            <p><small>&copy; 2019 Edu AI. All Rights Reserved.</small></p>
        </div>
    </div>
</footer>
<%
    }
%>
