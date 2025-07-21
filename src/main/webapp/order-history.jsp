<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Order History</title>
    <link rel="stylesheet" href="css/payment.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet"/>
</head>
<%@ include file="header.jsp" %>
<body>
<%@ include file="menu.jsp" %>
<div class="content_wrapper d-flex flex-column min-vh-100">
    <div class="main-content d-flex flex-column">
        <div class="container py-4">
            <h2 class="mb-4 text-primary fw-bold">Order History</h2>

            <div class="table-responsive shadow-sm rounded-4 overflow-hidden bg-white">
                <table class="table table-hover align-middle mb-0">
                    <thead class="bg-primary text-white">
                    <tr>
                        <th scope="col">Order ID</th>
                        <th scope="col">Date</th>
                        <th scope="col">Total Amount</th>
                        <th scope="col">Status</th>
                        <th scope="col">Items</th>
                        <th scope="col">Details</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty orderList}">
                            <c:forEach var="order" items="${orderList}">
                                <tr>
                                    <td class="fw-bold">#${order.id}</td>
                                    <td>
                                        <i class="bi bi-calendar2-check text-primary me-1"></i>
                                            <%-- SỬA Ở ĐÂY: Gọi phương thức getCreatedAtAsDate() --%>
                                        <fmt:formatDate value="${order.createdAtAsDate}" pattern="dd-MM-yyyy HH:mm"/>
                                    </td>
                                    <td class="fw-semibold text-primary">
                                        <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫"/>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${order.status eq 'Completed'}">
                                                <span class="badge bg-success px-3 py-2 fs-6 rounded-pill">
                                                    <i class="bi bi-check-circle me-1"></i>Paid
                                                 </span>
                                            </c:when>
                                            <c:when test="${order.status eq 'Pending'}">
                                                <span class="badge bg-warning text-dark px-3 py-2 fs-6 rounded-pill">
                                                    <i class="bi bi-hourglass-split me-1"></i>Pending
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-danger px-3 py-2 fs-6 rounded-pill">
                                                    <i class="bi bi-x-circle me-1"></i>Cancelled
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>
                                        <a href="${pageContext.request.contextPath}/order/view?id=${order.id}" class="btn btn-sm btn-outline-primary">
                                            <i class="bi bi-eye"></i> View
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="6" class="text-center py-5">
                                    <div style="padding: 30px; background-color: #E6F7FF; border: 2px dashed #3399FF; border-radius: 12px;">
                                        <h4 style="color: #3399FF; font-weight: 600;">
                                            <i class="bi bi-emoji-frown me-2"></i>No order history found
                                        </h4>
                                        <p style="color: #666;">Looks like you haven't made any transactions yet.</p>
                                        <a href="${pageContext.request.contextPath}/category">
                                            <button class="btn btn-primary mt-3 px-4 py-2">Start Shopping</button>
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <%@ include file="footer.jsp" %>
</div>

</body>
<script src="<%= request.getContextPath() %>/js/cart.js"></script>