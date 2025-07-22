<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Admin Dashboard</title>
    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">
</head>
<body id="page-top">
<div id="wrapper">
    <jsp:include page="/view/admin/sidebar.jsp" />

    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <jsp:include page="/view/admin/topbar.jsp" />

            <div class="container-fluid">
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">Dashboard</h1>
                    <a href="#" class="d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm"><i class="fas fa-download fa-sm text-white-50"></i> Generate Report</a>
                </div>

                <div class="row">
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-primary shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">Tổng Doanh Thu</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            <fmt:setLocale value="vi_VN"/>
                                            <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫"/>
                                        </div>
                                    </div>
                                    <div class="col-auto"><i class="fas fa-dollar-sign fa-2x text-gray-300"></i></div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-success shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-success text-uppercase mb-1">Khóa Học Đã Bán</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${coursesSold}</div>
                                    </div>
                                    <div class="col-auto"><i class="fas fa-book-open fa-2x text-gray-300"></i></div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-info shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-info text-uppercase mb-1">Gói Plan Đã Bán</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${plansSold}</div>
                                    </div>
                                    <div class="col-auto"><i class="fas fa-rocket fa-2x text-gray-300"></i></div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-warning shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">Tổng Số Khách Hàng</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${totalParticipants}</div>
                                    </div>
                                    <div class="col-auto"><i class="fas fa-users fa-2x text-gray-300"></i></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="row">
                    <div class="col-xl-8 col-lg-7">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Tổng Quan Doanh Thu</h6>
                            </div>
                            <div class="card-body">
                                <div class="chart-area"><canvas id="myAreaChart"></canvas></div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
        <jsp:include page="/view/admin/footer.jsp" />
    </div>
</div>
<a class="scroll-to-top rounded" href="#page-top"><i class="fas fa-angle-up"></i></a>
<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/chart.js/Chart.min.js"></script>
<script type="text/javascript">
    const areaChartLabels = ${areaChartLabels};
    const areaChartData = ${areaChartData};
</script>
<script src="${pageContext.request.contextPath}/js/demo/chart-area-demo.js"></script>
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>
</body>
</html>