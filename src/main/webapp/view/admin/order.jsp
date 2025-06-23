<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Order Management</title>
  <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css?family=Nunito:200,300,400,700" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.css" rel="stylesheet">
</head>
<body id="page-top">
<div id="wrapper">
  <jsp:include page="sidebar.jsp" />

  <div id="content-wrapper" class="d-flex flex-column">
    <div id="content">
      <jsp:include page="topbar.jsp" />

      <div class="container-fluid">
        <h1 class="h3 mb-2 text-gray-800">Đơn Hàng</h1>

        <div class="card shadow mb-4">
          <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Danh sách đơn hàng</h6>
          </div>
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                <thead>
                <tr>
                  <th>ID</th>
                  <th>Người dùng</th>
                  <th>Ngày đặt</th>
                  <th>Tổng tiền</th>
                  <th>Trạng thái</th>
                  <th>Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="o" items="${orders}">
                  <tr>
                    <td>${o.id}</td>
                    <td>${o.user.userName}</td>
                    <td><fmt:formatDate value="${o.createdAtAsDate}" pattern="dd/MM/yyyy" /></td>
                    <td><fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="₫" /></td>
                    <td>
                      <c:choose>
                        <c:when test="${o.status == 'active'}">
                          <span class="badge badge-success">Đã thanh toán</span>
                        </c:when>
                        <c:when test="${o.status == 'pending'}">
                          <span class="badge badge-warning">Chờ xử lý</span>
                        </c:when>
                        <c:otherwise>
                          <span class="badge badge-danger">Đã hủy</span>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <td>
                      <!-- View -->
                      <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#viewModal${o.id}">Xem</button>
                      <!-- Edit -->
                      <button class="btn btn-sm btn-primary" data-toggle="modal" data-target="#editModal${o.id}">Sửa</button>
                      <!-- Delete -->
                      <form action="${pageContext.request.contextPath}/admin/order/delete" method="post" style="display:inline;" onsubmit="return confirm('Xác nhận xóa đơn hàng này?');">
                        <input type="hidden" name="orderId" value="${o.id}" />
                        <button type="submit" class="btn btn-sm btn-danger">Xóa</button>
                      </form>
                    </td>
                  </tr>

                  <!-- View Modal -->
                  <div class="modal fade" id="viewModal${o.id}" tabindex="-1" role="dialog">
                    <div class="modal-dialog" role="document">
                      <div class="modal-content">
                        <div class="modal-header">
                          <h5 class="modal-title">Chi tiết đơn hàng #${o.id}</h5>
                          <button type="button" class="close" data-dismiss="modal">
                            <span>&times;</span>
                          </button>
                        </div>
                        <div class="modal-body">
                          <p><strong>Người dùng:</strong> ${o.user.userName}</p>
                          <p><strong>Ngày đặt:</strong> <fmt:formatDate value="${o.createdAtAsDate}" pattern="dd/MM/yyyy" /></p>
                          <p><strong>Tổng tiền:</strong> <fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="₫" /></p>
                          <p><strong>Trạng thái:</strong> ${o.status}</p>
                        </div>
                        <div class="modal-footer">
                          <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                        </div>
                      </div>
                    </div>
                  </div>

                  <!-- Edit Modal -->
                  <div class="modal fade" id="editModal${o.id}" tabindex="-1" role="dialog">
                    <div class="modal-dialog" role="document">
                      <div class="modal-content">
                        <form method="post" action="${pageContext.request.contextPath}/admin/order/update">
                          <div class="modal-header">
                            <h5 class="modal-title">Sửa đơn hàng #${o.id}</h5>
                            <button type="button" class="close" data-dismiss="modal">
                              <span>&times;</span>
                            </button>
                          </div>
                          <div class="modal-body">
                            <input type="hidden" name="orderId" value="${o.id}" />

                            <div class="form-group">
                              <label>Người dùng</label>
                              <input type="text" class="form-control" value="${o.user.userName}" readonly />
                            </div>

                            <div class="form-group">
                              <label>Ngày đặt</label>
                              <input type="text" class="form-control" value="<fmt:formatDate value='${o.createdAtAsDate}' pattern='dd/MM/yyyy' />" readonly />
                            </div>

                            <div class="form-group">
                              <label>Tổng tiền</label>
                              <input type="number" step="0.01" name="totalAmount" class="form-control" value="${o.totalAmount}" required />
                            </div>

                            <div class="form-group">
                              <label>Trạng thái</label>
                              <select name="status" class="form-control" required>
                                <option value="pending" ${o.status == 'pending' ? 'selected' : ''}>Chờ xử lý</option>
                                <option value="active" ${o.status == 'active' ? 'selected' : ''}>Đã thanh toán</option>
                                <option value="cancel" ${o.status == 'cancel' ? 'selected' : ''}>Đã hủy</option>
                              </select>
                            </div>
                          </div>
                          <div class="modal-footer">
                            <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                          </div>
                        </form>
                      </div>
                    </div>
                  </div>
                </c:forEach>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <jsp:include page="footer.jsp" />
  </div>
</div>

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/datatables/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.js"></script>
<script src="${pageContext.request.contextPath}/js/demo/datatables-demo.js"></script>
</body>
</html>
