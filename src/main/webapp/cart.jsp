<%--
  Created by IntelliJ IDEA.
  User: macos
  Date: 5/6/25
  Time: 15:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cart</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/cart.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css">
</head>
<%@ include file="header.jsp" %>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>

</head>
<body>

<div class="container px-3 my-5 clearfix">
    <div class="card" style="border-radius: 10px">
        <div class="card-header" style="background-color: #3399FF; border-top-left-radius: 10px; border-top-right-radius: 10px;">
            <h2 style="margin-left: 20px; color: whitesmoke;">Cart</h2>
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
                                    <img src="${item.imageUrl}" class="d-block ui-w-40 ui-bordered mr-4" alt="">
                                    <div class="media-body">
                                        <a href="#" class="d-block text-dark">${item.name}</a>
                                    </div>
                                </div>
                            </td>
                            <td class="text-right font-weight-semibold align-middle p-4">$${item.price}</td>
                            <td class="align-middle p-4"><input type="text" class="form-control text-center" value="1"></td>
                            <td class="text-right font-weight-semibold align-middle p-4">$<c:out value="${item.price * item.quantity}" /></td>
                            <td class="text-center align-middle px-0">
                                <a href="#" class="remove-item shop-tooltip close float-none text-danger" data-index="${status.index}" title="Remove">×</a>
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
                                $<c:set var="total" value="0" scope="page" />
                                <c:forEach var="item" items="${orderItems}">
                                    <c:set var="total" value="${total + (item.price * item.quantity)}" />
                                </c:forEach>
                                <c:out value="${total}" />
                            </strong>
                        </div>
                    </div>
                </div>
            </div>

            <div class="float-right">
                <button type="button" class="btn btn-lg btn-default md-btn-flat mt-2 mr-3">Back to shopping</button>
                <button type="button" class="btn btn-lg btn-primary mt-2">Checkout</button>
            </div>

        </div>
    </div>
</div>
</body>
<script src="<%= request.getContextPath() %>/js/cart.js"></script>
<%@ include file="footer.jsp" %>
</html>
