<%
    String uri1 = request.getRequestURI();
    boolean skipFooter = uri1.contains("login.jsp") || uri1.contains("register.jsp");

    if (!skipFooter) {
%>

<footer class="footer-59391">
    <div class="container1">
        <div class="row mb-5">
            <div class="col-md-4">
                <div class="site-logo">
                    <a href="#">Edu AI</a>
                </div>
            </div>
            <div class="col-md-8 text-md-right">
                <ul class="list-unstyled social-icons">
                    <li><a href="#" class="fb"><span class="icon-facebook"></span></a></li>
                    <li><a href="#" class="tw"><span class="icon-twitter"></span></a></li>
                    <li><a href="#" class="in"><span class="icon-instagram"></span></a></li>
                    <li><a href="#" class="be"><span class="icon-behance"></span></a></li>
                    <li><a href="#" class="dr"><span class="icon-dribbble"></span></a></li>
                    <li><a href="#" class="yt"><span class="icon-play"></span></a></li>
                </ul>
            </div>
        </div>

        <div class="row mb-5">
            <div class="col-md-6 ">
                <ul class="nav-links list-unstyled nav-left">
                    <li><a href="#">Privacy</a></li>
                    <li><a href="#">Policy</a></li>
                </ul>
            </div>
            <div class="col-md-6 text-md-right">
                <ul class="nav-links list-unstyled nav-right">
                    <li><a href="#">Home</a></li>
                    <li><a href="#">Our works</a></li>
                    <li><a href="#">About</a></li>
                    <li><a href="#">Blog</a></li>
                    <li><a href="#">Contact</a></li>
                </ul>
            </div>
        </div>
        <div class="row">
            <div class="col ">
                <div class="copyright">
                    <p><small>Copyright 2019. All Rights Reserved.</small></p>
                </div>
            </div>
        </div>

    </div>
</footer>

<script src="<%= request.getContextPath() %>/js/jquery-3.3.1.min.js"></script>
<script src="<%= request.getContextPath() %>/js/popper.min.js"></script>
<script src="<%= request.getContextPath() %>/js/bootstrap.min.js"></script>

<%
    }
%>
