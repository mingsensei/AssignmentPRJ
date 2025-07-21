<%--
  Created by IntelliJ IDEA.
  User: macos
  Date: 21/07/2025
  Time: 12:00 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<head>
    <title>Shopping Cart</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/cart.css">
    <script>
        // Thiết lập contextPath cho JavaScript sử dụng
        var contextPath = '<%= request.getContextPath() %>';
    </script>
</head>
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<body>
<div class="main-content d-flex flex-column min-vh-100">
    <div class="container px-3 my-5 clearfix">
        <div class="card" style="border-radius: 10px">
            <div class="card-header" style="background-color: #3399FF; border-top-left-radius: 10px; border-top-right-radius: 10px;">
                <h2 style="margin-left: 20px; color: whitesmoke;">Shopping Cart 🛒</h2>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-bordered m-0">
                        <thead>
                        <tr>
                            <th class="text-center py-3 px-4" style="min-width: 400px;">Course Name &amp; Details</th>
                            <th class="text-right py-3 px-4" style="width: 100px;">Price</th>
                            <th class="text-center py-3 px-4" style="width: 120px;">Quantity</th>
                            <th class="text-right py-3 px-4" style="width: 100px;">Total</th>
                            <th class="text-center align-middle py-3 px-0" style="width: 40px;">
                                <i class="ion-md-trash"></i>
                            </th>
                        </tr>
                        </thead>
                        <tbody>

                        <%-- SỬA 1: Thay thế 'orderItems' bằng 'cartItems' --%>
                        <c:choose>
                            <c:when test="${empty cartItems}">
                                <tr>
                                    <td colspan="5" class="text-center py-5">
                                        <div style="padding: 30px; background-color: #E6F2FF; border: 2px dashed #3399FF; border-radius: 10px;">
                                            <h4 style="color: #3399FF; font-weight: 600; font-size: 1.5rem; margin: 0;">
                                                Your cart is empty!
                                            </h4>
                                            <p style="color: #666; margin-top: 8px;">Start adding courses to see them here.</p>
                                        </div>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <%-- SỬA 2: Lặp qua 'cartItems' và dùng biến 'course' thay vì 'item' --%>
                                <c:forEach var="course" items="${cartItems}">
                                    <%-- Thêm id cho thẻ <tr> để JavaScript có thể xóa nó khỏi giao diện --%>
                                    <tr id="cart-row-${course.id}">
                                        <td class="p-4">
                                            <div class="media align-items-center">
                                                    <%-- SỬA 3: Truy cập trực tiếp vào thuộc tính của 'course' --%>
                                                <img src="${pageContext.request.contextPath}/images/course${course.id}.webp"
                                                     class="d-block ui-w-40 ui-bordered mr-4"
                                                     alt=""
                                                     style="width: 40px; height: 40px; object-fit: cover">
                                                <div class="media-body">
                                                    <a href="#" class="d-block text-dark">${course.name}</a>
                                                </div>
                                            </div>
                                        </td>
                                        <td class="text-right font-weight-semibold align-middle p-4"><fmt:formatNumber value="${course.price}" type="currency" currencySymbol="₫"/></td>
                                        <td class="align-middle p-4"><input type="text" class="form-control text-center" value="1" readonly></td>
                                        <td class="text-right font-weight-semibold align-middle p-4"><fmt:formatNumber value="${course.price}" type="currency" currencySymbol="₫"/></td>
                                        <td class="text-center align-middle px-0">
                                                <%-- SỬA 4: data-id giờ là course.id để gửi cho AJAX --%>
                                            <a href="#" class="remove-item shop-tooltip close float-none text-danger" data-id="${course.id}" title="Remove">×</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>

                <div class="d-flex flex-wrap justify-content-between align-items-center pb-4">
                    <%-- SỬA 5: Dùng 'cartItems' để kiểm tra --%>
                    <c:if test="${not empty cartItems}">
                        <div class="mt-4"></div>
                        <div class="d-flex">
                            <div class="text-right mt-4 mr-5">
                                <label class="text-muted font-weight-normal m-0">Discount</label>
                                <div class="text-large"><strong>0</strong></div>
                            </div>
                            <div class="text-right mt-4">
                                <label class="text-muted font-weight-normal m-0">Total price</label>
                                <div class="text-large">
                                        <%-- SỬA 6: Dùng 'totalAmount' và định dạng tiền tệ --%>
                                    <strong id="cart-total"><fmt:formatNumber value="${totalAmount}" type="currency" currencySymbol="₫"/></strong>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </div>

                <div class="float-right">
                    <a href="<%= request.getContextPath() %>/category">
                        <button type="button" class="btn btn-lg btn-default md-btn-flat mt-2 mr-3">Back to shopping</button>
                    </a>

                    <%-- SỬA 7: Logic nút Checkout --%>
                    <c:choose>
                        <c:when test="${empty cartItems}">
                            <button type="button" class="btn btn-lg btn-secondary mt-2" disabled title="Your cart is empty">
                                Checkout
                            </button>
                        </c:when>
                        <c:otherwise>
                            <%--
                                Giờ đây, Checkout sẽ submit một form đến '/checkout'
                                Servlet này sẽ xử lý việc tạo Order và gọi VNPAY
                            --%>
                            <form action="${pageContext.request.contextPath}/vn_pay/ajax" method="POST" style="display: inline;">
                                <input type="hidden" name="totalBill" value="${totalAmount}" />
                                <button type="submit" class="btn btn-lg btn-primary mt-2">Checkout</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
    <%@ include file="footer.jsp" %>
</div>
</body>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="<%= request.getContextPath() %>/js/cart.js"></script>