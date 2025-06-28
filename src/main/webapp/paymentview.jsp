<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Payment History</title>
    <link rel="stylesheet" href="css/payment.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet"/>
</head>
<%@ include file="header.jsp" %>
<body>
<%@ include file="menu.jsp" %>
<div class="main-content d-flex flex-column min-vh-100">
    <div class="container py-4"><h2 class="mb-4 text-primary fw-bold"> Payment History</h2>

        <div class="table-responsive shadow-sm rounded-4 overflow-hidden bg-white">
            <table class="table table-hover align-middle mb-0">
                <thead class="bg-primary text-white">
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Date</th>
                    <th scope="col">Amount</th>
                    <th scope="col">Status</th>
                    <th scope="col">Method</th>
                    <th scope="col">Description</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty paymentList}">
                        <c:forEach var="payment" items="${paymentList}" varStatus="loop">
                            <tr>
                                <td>${loop.index + 1}</td>
                                <td>
                                    <i class="bi bi-calendar2-check text-primary me-1"></i>
                                        ${payment.formattedPayDate}
                                </td>
                                <td class="fw-semibold text-primary">${payment.amount}VND</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${payment.status eq 'SUCCESS'}">
                                            <span class="badge bg-success px-3 py-2 fs-6 rounded-pill">
                                                <i class="bi bi-check-circle me-1"></i>${payment.status}
                                             </span>
                                        </c:when>
                                        <c:when test="${payment.status eq 'PENDING'}">
                                            <span class="badge bg-warning text-dark px-3 py-2 fs-6 rounded-pill">
                                                <i class="bi bi-hourglass-split me-1"></i>${payment.status}
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger px-3 py-2 fs-6 rounded-pill">
                                                <i class="bi bi-x-circle me-1"></i>${payment.status}
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td><i class="bi bi-bank me-1"></i>${payment.bankCode}</td>
                                <td>${payment.transactionNo}</td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="6" class="text-center text-muted py-4">
                                <i class="bi bi-emoji-frown me-2"></i>No payment history found.
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
<script src="<%= request.getContextPath() %>/js/cart.js"></script>
<%@ include file="footer.jsp" %>
