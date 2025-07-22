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
  <jsp:include page="sidebar.jsp"/>

  <div id="content-wrapper" class="d-flex flex-column">
    <div id="content">
      <jsp:include page="topbar.jsp"/>

      <div class="container-fluid">
        <h1 class="h3 mb-2 text-gray-800">Đơn Hàng</h1>


        <div class="card shadow mb-4">
          <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Danh sách đơn hàng</h6>
          </div>
          <div class="card-body">
            <div class="table-responsive" style="overflow-x: auto;">
              <table class="table table-bordered" id="orderTable" width="100%" cellspacing="0" style="min-width: 1200px;">
                <thead>
                <tr>
                  <th style="min-width: 80px;">ID</th>
                  <th style="min-width: 150px;">Khách hàng</th>
                  <th style="min-width: 200px;">Email</th>
                  <th style="min-width: 120px;">Tổng tiền</th>
                  <th style="min-width: 150px;">Loại đơn hàng</th>
                  <th style="min-width: 120px;">Trạng thái</th>
                  <th style="min-width: 150px;">Ngày tạo</th>
                  <th style="min-width: 200px;">Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="order" items="${orders}">
                  <tr>
                    <td>#${order.id}</td>
                    <td>${order.user.fullName}</td>
                    <td>${order.user.email}</td>
                    <td>
                      <fmt:formatNumber value="${order.totalAmount}" type="currency"
                                        currencySymbol="VND "/>
                    </td>
                    <td>
                      <c:choose>
                        <c:when test="${order.orderType == 'COURSE_PURCHASE'}">
                          <span class="badge badge-info">Mua khóa học</span>
                        </c:when>
                        <%-- SỬA Ở ĐÂY: Thêm "and not empty order.plan" --%>
                        <c:when test="${order.orderType == 'PLAN_PURCHASE' and not empty order.plan}">
                          <span class="badge badge-primary">Mua Gói: ${order.plan.name}</span>
                        </c:when>
                        <c:otherwise>
                          <span class="badge badge-secondary">${order.orderType}</span>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <td>
                      <c:choose>
                        <c:when test="${order.status == 'completed'}">
                          <span class="badge badge-success">Đã hoàn thành</span>
                        </c:when>
                        <c:when test="${order.status == 'pending'}">
                          <span class="badge badge-warning">Đang xử lý</span>
                        </c:when>
                        <c:when test="${order.status == 'cancelled'}">
                          <span class="badge badge-danger">Đã hủy</span>
                        </c:when>
                        <c:when test="${order.status == 'processing'}">
                          <span class="badge badge-info">Đang giao hàng</span>
                        </c:when>
                        <c:otherwise>
                          <span class="badge badge-secondary">${order.status}</span>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <td>
                      <fmt:formatDate value="${order.createdAtAsDate}"
                                      pattern="dd/MM/yyyy HH:mm"/>
                    </td>
                    <td>
                      <!-- View -->
                      <button class="btn btn-sm btn-info" data-toggle="modal"
                              data-target="#viewModal${order.id}">
                        <i class="fas fa-eye"></i> Xem
                      </button>
                      <!-- Edit -->
                      <button class="btn btn-sm btn-primary" data-toggle="modal"
                              data-target="#editModal${order.id}">
                        <i class="fas fa-edit"></i> Sửa
                      </button>
                      <!-- Delete -->
                      <form action="${pageContext.request.contextPath}/admin/order/delete"
                            method="post" style="display:inline;"
                            onsubmit="return confirm('Xác nhận xóa đơn hàng này?');">
                        <input type="hidden" name="orderId" value="${order.id}"/>
                        <button type="submit" class="btn btn-sm btn-danger">
                          <i class="fas fa-trash"></i> Xóa
                        </button>
                      </form>
                    </td>
                  </tr>

                  <!-- View Modal -->
                  <div class="modal fade" id="viewModal${order.id}" tabindex="-1" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                      <div class="modal-content">
                        <div class="modal-header">
                          <h5 class="modal-title">Chi tiết đơn hàng #${order.id}</h5>
                          <button type="button" class="close" data-dismiss="modal">
                            <span>&times;</span>
                          </button>
                        </div>
                        <div class="modal-body">
                          <div class="row">
                            <div class="col-md-6">
                              <p><strong>ID:</strong> #${order.id}</p>
                              <p><strong>Khách hàng:</strong> ${order.user.fullName}</p>
                              <p><strong>Email:</strong> ${order.user.email}</p>
                              <p><strong>Tổng tiền:</strong>
                                <fmt:formatNumber value="${order.totalAmount}"
                                                  type="currency" currencySymbol="VND "/>
                              </p>
                            </div>
                            <div class="col-md-6">
                              <p><strong>Trạng thái:</strong>
                                <c:choose>
                                  <c:when test="${order.status == 'completed'}">
                                    <span class="badge badge-success">Đã hoàn thành</span>
                                  </c:when>
                                  <c:when test="${order.status == 'pending'}">
                                    <span class="badge badge-warning">Đang xử lý</span>
                                  </c:when>
                                  <c:when test="${order.status == 'cancelled'}">
                                    <span class="badge badge-danger">Đã hủy</span>
                                  </c:when>
                                  <c:when test="${order.status == 'processing'}">
                                    <span class="badge badge-info">Đang giao hàng</span>
                                  </c:when>
                                  <c:otherwise>
                                    <span class="badge badge-secondary">${order.status}</span>
                                  </c:otherwise>
                                </c:choose>
                              </p>
                              <p><strong>Ngày tạo:</strong>
                                <fmt:formatDate value="${order.createdAtAsDate}"
                                                pattern="dd/MM/yyyy HH:mm:ss"/>
                              </p>
                            </div>
                          </div>
                        </div>
                        <div class="modal-footer">
                          <button type="button" class="btn btn-secondary"
                                  data-dismiss="modal">Đóng
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>

                  <!-- Edit Modal -->
                  <div class="modal fade" id="editModal${order.id}" tabindex="-1" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                      <div class="modal-content">
                        <form method="post"
                              action="${pageContext.request.contextPath}/admin/order/update">
                          <div class="modal-header">
                            <h5 class="modal-title">Sửa đơn hàng #${order.id}</h5>
                            <button type="button" class="close" data-dismiss="modal">
                              <span>&times;</span>
                            </button>
                          </div>
                          <div class="modal-body">
                            <input type="hidden" name="orderId" value="${order.id}"/>

                            <div class="row">
                              <div class="col-md-6">
                                <div class="form-group">
                                  <label>ID đơn hàng</label>
                                  <input type="text" class="form-control"
                                         value="#${order.id}" readonly/>
                                </div>

                                <div class="form-group">
                                  <label>Khách hàng</label>
                                  <input type="text" class="form-control"
                                         value="${order.user.fullName}" readonly/>
                                </div>

                                <div class="form-group">
                                  <label for="totalAmount${order.id}">Tổng tiền <span
                                          class="text-danger">*</span></label>
                                  <input type="number" id="totalAmount${order.id}"
                                         name="totalAmount" class="form-control"
                                         value="${order.totalAmount}" required min="0"
                                         step="0.01"/>
                                </div>
                              </div>
                              <div class="col-md-6">
                                <div class="form-group">
                                  <label for="status${order.id}">Trạng thái <span
                                          class="text-danger">*</span></label>
                                  <select id="status${order.id}" name="status"
                                          class="form-control" required>
                                    <option value="pending" ${order.status == 'pending' ? 'selected' : ''}>
                                      Đang xử lý
                                    </option>
                                    <option value="processing" ${order.status == 'processing' ? 'selected' : ''}>
                                      Đang giao hàng
                                    </option>
                                    <option value="completed" ${order.status == 'completed' ? 'selected' : ''}>
                                      Đã hoàn thành
                                    </option>
                                    <option value="cancelled" ${order.status == 'cancelled' ? 'selected' : ''}>
                                      Đã hủy
                                    </option>
                                  </select>
                                </div>

                                <div class="form-group">
                                  <label>Email khách hàng</label>
                                  <input type="email" class="form-control"
                                         value="${order.user.email}" readonly/>
                                </div>

                                <div class="form-group">
                                  <label>Ngày tạo</label>
                                  <input type="text" class="form-control"
                                         value="<fmt:formatDate value='${order.createdAtAsDate}'
                                                                                 pattern='dd/MM/yyyy HH:mm:ss'/>" readonly/>
                                </div>
                              </div>
                            </div>
                          </div>
                          <div class="modal-footer">
                            <button type="submit" class="btn btn-primary">
                              <i class="fas fa-save"></i> Lưu thay đổi
                            </button>
                            <button type="button" class="btn btn-secondary"
                                    data-dismiss="modal">Hủy
                            </button>
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

    <jsp:include page="footer.jsp"/>
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

<script>
  $(document).ready(function () {
    // Initialize DataTable for orders
    $('#orderTable').DataTable({
      "language": {
        "url": "//cdn.datatables.net/plug-ins/1.10.24/i18n/Vietnamese.json"
      },
      "pageLength": 25,
      "order": [[0, "desc"]],
      "scrollX": true,  // Enable horizontal scroll
      "columnDefs": [
        {
          "targets": [3], // Total amount column
          "className": "text-right"
        },
        {
          "targets": [6], // Actions column
          "orderable": false,
          "width": "200px"
        }
      ],
      "fixedColumns": {
        "leftColumns": 1  // Fix first column when scrolling
      }
    });

    // Auto-format currency input
    $('input[name="totalAmount"]').on('input', function() {
      var value = $(this).val();
      if (value && !isNaN(value)) {
        // Format number with thousands separator
        var formatted = parseFloat(value).toLocaleString('vi-VN');
        // Don't update input value to avoid cursor position issues
      }
    });

    // Status change confirmation for completed orders
    $('select[name="status"]').on('change', function() {
      var newStatus = $(this).val();
      var orderId = $(this).closest('form').find('input[name="orderId"]').val();

      if (newStatus === 'cancelled') {
        if (!confirm('Xác nhận hủy đơn hàng #' + orderId + '?')) {
          $(this).val($(this).data('original-value') || 'pending');
          return false;
        }
      } else if (newStatus === 'completed') {
        if (!confirm('Xác nhận hoàn thành đơn hàng #' + orderId + '?')) {
          $(this).val($(this).data('original-value') || 'pending');
          return false;
        }
      }
    });

    // Store original values
    $('select[name="status"]').each(function() {
      $(this).data('original-value', $(this).val());
    });
  });
</script>
</body>
</html>