<%--
  Created by IntelliJ IDEA.
  User: macos
  Date: 5/6/25
  Time: 15:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
    <title>Cart</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/cart.css">
    <script>
        var contextPath = '<%= request.getContextPath() %>';
    </script>
</head>
<%@ include file="header.jsp" %>

<body>
<%@ include file="menu.jsp" %>
<div class="container px-3 my-5 clearfix">
    <div class="card" style="border-radius: 10px">
        <div class="card-header" style="background-color: #3399FF; border-top-left-radius: 10px; border-top-right-radius: 10px;">
            <h2 style="margin-left: 20px; color: whitesmoke;"></h2>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered m-0">
                    <thead>
                    <tr>
                        <th class="text-center py-3 px-4" style="min-width: 400px;">Product Name &amp; Details</th>
                        <th class="text-right py-3 px-4" style="width: 100px;">Price</th>
                        <th class="text-center py-3 px-4" style="width: 120px;">Quantity</th>
                        <th class="text-right py-3 px-4" style="width: 100px;">Total</th>
                        <th class="text-center align-middle py-3 px-0" style="width: 40px;"><a href="#" class="shop-tooltip float-none text-light"><i class="ion-md-trash"></i></a></th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:forEach var="item" items="${orderItems}">
                        <tr>
                            <td class="p-4">
                                <div class="media align-items-center">
                                    <img src="${pageContext.request.contextPath}/images/course${item.course.id}.webp"
                                         class="d-block ui-w-40 ui-bordered mr-4"
                                         alt=""
                                         style="width: 40px; height: 40px; object-fit: cover">

                                    <div class="media-body">
                                        <a href="#" class="d-block text-dark">${item.course.name}</a>
                                    </div>
                                </div>
                            </td>
                            <td class="text-right font-weight-semibold align-middle p-4">$${item.price}</td>
                            <td class="align-middle p-4"><input type="text" class="form-control text-center" value="1" min="1" max="1"></td>
                            <td class="text-right font-weight-semibold align-middle p-4">$<c:out value="${item.price}" /></td>
                            <td class="text-center align-middle px-0">
                                <a href="#" class="remove-item shop-tooltip close float-none text-danger" data-id="${item.id}" title="Remove">×</a>
                            </td>

                        </tr>
                    </c:forEach>

                    </tbody>
                </table>
            </div>

            <div class="d-flex flex-wrap justify-content-between align-items-center pb-4">
                <div class="mt-4"></div>
                <div class="d-flex">
                    <div class="text-right mt-4 mr-5">
                        <label class="text-muted font-weight-normal m-0">Discount</label>
                        <div class="text-large"><strong>0</strong></div>
                    </div>
                    <div class="text-right mt-4">
                        <label class="text-muted font-weight-normal m-0">Total price</label>
                        <div class="text-large">
                            <strong>
                                <c:out value="${total}" />
                            </strong>
                        </div>
                    </div>
                </div>
            </div>

            <div class="float-right">
                <c:set var="order" value="${requestScope.order}" />
                <a   href="<%= request.getContextPath() %>/category" >
                    <button type="button" class="btn btn-lg btn-default md-btn-flat mt-2 mr-3">Back to shopping</button>
                </a>
                <a href="<%= request.getContextPath() %>/payment?action=pay&order=${order.id}&amount=${total}">
                    <button type="button" class="btn btn-lg btn-primary mt-2">Checkout</button>
                </a>
            </div>

        </div>
    </div>
</div>
</body>
<!-- Load jQuery trước -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- Sau đó mới load cart.js -->
<script src="<%= request.getContextPath() %>/js/cart.js"></script>
<%@ include file="footer.jsp" %>
