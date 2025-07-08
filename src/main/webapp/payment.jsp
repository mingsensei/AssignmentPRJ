<%--
  Created by IntelliJ IDEA.
  User: macos
  Date: 5/6/25
  Time: 15:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thanh Toán VietQR</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/index.css" />
    <style>
        .center-button {
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .payment-container {
            max-width: 600px;
            margin: 40px auto;
            padding: 20px;
            text-align: center;
        }
        .qr-container {
            margin: 20px 0;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background: white;
        }
        .payment-info {
            margin: 20px 0;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 8px;
        }
        .amount {
            font-size: 24px;
            color: #2c3e50;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="content-wrapper">
        <div class="payment-container">
            <h2>Thanh Toán Đơn Hàng</h2>
            
            <div class="payment-info">
                <p>Số tiền cần thanh toán:</p>
                <p class="amount">${totalAmount} VNĐ</p>
            </div>

            <div class="qr-container">
                <h3>Quét mã để thanh toán qua VietQR</h3>
                <div>
                    <img
                        id="qr-vietqr"
                        src=""
                        width="300"
                        alt="QR VietQR MB"
                    />
                </div>
            </div>

            <p>Vui lòng quét mã QR để thanh toán. Sau khi thanh toán thành công, đơn hàng của bạn sẽ được xử lý.</p>
            <div class="center-button">
                <c:choose>
                    <c:when test="${not empty planId}">
                        <a href="${pageContext.request.contextPath}/processPayment?action=payPlan&userId=${userId}&planId=${planId}&amount=${totalAmount}" class="button"
                           class="btn btn-primary btn-lg">
                            Tôi đã thực hiện thanh toán
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/processPayment?action=payOrder&userId=${userId}&orderId=${order.id}&amount=${totalAmount}"
                           class="btn btn-primary btn-lg">
                            Tôi đã thực hiện thanh toán
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <%@ include file="footer.jsp" %>

    <script>
        const bankId = "mbbank";
        const accountNo = "0773304009";
        const template = "compact2";

        const amount = "${totalAmount}"; // là số, không cần dấu nháy
        const message = "${message}";  // là chuỗi, PHẢI có dấu nháy
        const accountName = encodeURIComponent("DUONG HONG MINH");

        const qrURL = `https://img.vietqr.io/image/${bankId}-${accountNo}-${template}.png?amount=${totalAmount}&addInfo=${message}&accountName=${accountName}`;

        console.log("QR URL:", qrURL); // để kiểm tra
        document.getElementById("qr-vietqr").src = qrURL;
    </script>

</body>
</html>
