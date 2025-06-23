<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Lịch Sử Giao Dịch</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/index.css" />
    <style>
        .transaction-container {
            max-width: 800px;
            margin: 40px auto;
            padding: 20px;
        }
        .transaction-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .transaction-table th,
        .transaction-table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        .transaction-table th {
            background-color: #f8f9fa;
            font-weight: 600;
        }
        .transaction-table tr:hover {
            background-color: #f5f5f5;
        }
        .status {
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 14px;
        }
        .status-success {
            background-color: #d4edda;
            color: #155724;
        }
        .status-pending {
            background-color: #fff3cd;
            color: #856404;
        }
    </style>
</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="content-wrapper">
        <div class="transaction-container">
            <h2>Lịch Sử Giao Dịch</h2>
            
            <table class="transaction-table">
                <thead>
                    <tr>
                        <th>Mã Giao Dịch</th>
                        <th>Ngày</th>
                        <th>Số Tiền</th>
                        <th>Trạng Thái</th>
                        <th>Mô Tả</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Sample transaction data - Replace with actual data from database -->
                    <tr>
                        <td>TX001</td>
                        <td>2024-03-20 15:30</td>
                        <td>500,000 VNĐ</td>
                        <td><span class="status status-success">Thành công</span></td>
                        <td>Thanh toán khóa học AI</td>
                    </tr>
                    <tr>
                        <td>TX002</td>
                        <td>2024-03-19 10:15</td>
                        <td>300,000 VNĐ</td>
                        <td><span class="status status-pending">Đang xử lý</span></td>
                        <td>Thanh toán khóa học Python</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <%@ include file="footer.jsp" %>
</body>
</html> 