<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <%-- Đặt tiêu đề trang một cách an toàn --%>
    <c:set var="pageTitle" value="Order Detail"/>
    <c:if test="${not empty orderItems}">
        <c:set var="pageTitle" value="Order Detail #${orderItems[0].order.id}"/>
    </c:if>
    <title>${pageTitle}</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet"/>
    <style>
        .order-details-card {
            border-radius: 1rem;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
    </style>
</head>
<%@ include file="header.jsp" %>
<body>
<%@ include file="menu.jsp" %>
<div class="content_wrapper d-flex flex-column min-vh-100 bg-light">
    <div class="main-content d-flex flex-column">
        <div class="container py-4">

            <%-- Kiểm tra xem danh sách orderItems có tồn tại và không rỗng không --%>
            <c:choose>
                <c:when test="${not empty orderItems}">
                    <%--
                        Lấy thông tin Order chung từ item đầu tiên trong list
                        và lưu vào một biến 'order' để tiện sử dụng.
                    --%>
                    <c:set var="order" value="${orderItems[0].order}" />

                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/order/history">Order History</a></li>
                            <li class="breadcrumb-item active" aria-current="page">Order #${order.id}</li>
                        </ol>
                    </nav>

                    <div class="card order-details-card">
                        <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                            <h4 class="mb-0">Order Detail #${order.id}</h4>
                            <c:choose>
                                <c:when test="${order.status eq 'paid'}"><span class="badge bg-light text-success p-2">Paid</span></c:when>
                                <c:when test="${order.status eq 'pending_payment'}"><span class="badge bg-light text-warning p-2">Pending</span></c:when>
                                <c:otherwise><span class="badge bg-light text-danger p-2">Cancelled</span></c:otherwise>
                            </c:choose>
                        </div>
                        <div class="card-body p-4">
                            <div class="row mb-4">
                                <div class="col-md-6">
                                    <h6 class="text-muted">ORDER DATE</h6>
                                    <p><fmt:formatDate value="${order.createdAtAsDate}" pattern="dd-MM-yyyy HH:mm:ss"/></p>
                                </div>
                                <div class="col-md-6 text-md-end">
                                    <h6 class="text-muted">TOTAL AMOUNT</h6>
                                    <h4><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫"/></h4>
                                </div>
                            </div>

                            <hr>

                            <h5 class="mb-3">Items in this order</h5>
                            <ul class="list-group list-group-flush">
                                    <%-- Lặp qua danh sách orderItems mà servlet đã gửi --%>
                                <c:forEach var="item" items="${orderItems}">
                                    <li class="list-group-item d-flex align-items-center">
                                        <img src="${pageContext.request.contextPath}/images/course${item.course.id}.webp"
                                             class="rounded me-3" alt="${item.course.name}" style="width: 60px; height: 60px; object-fit: cover;">
                                        <div class="flex-grow-1">
                                            <div class="fw-bold">${item.course.name}</div>
                                            <small class="text-muted">Type: ${item.course.type}</small>
                                        </div>
                                        <div class="fw-semibold">
                                            <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="₫"/>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <%-- Hiển thị thông báo nếu không tìm thấy đơn hàng --%>
                    <div class="alert alert-warning text-center">
                        <h4><i class="bi bi-exclamation-triangle-fill"></i> Order Not Found</h4>
                        <p>The requested order could not be found or you do not have permission to view it.</p>
                        <a href="${pageContext.request.contextPath}/order/history" class="btn btn-primary">Back to Order History</a>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>
    </div>
    <%@ include file="footer.jsp" %>
</div>
</body>