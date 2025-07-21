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
<style>

    :root{
        --base-clr: rgb(240, 248, 255);
        --line-clr: #00a0fb;
        --hover-clr: hsl(219, 88%, 87%);
        --text-clr: #00a0fb;
        --accent-clr: #003399;
        --secondary-text-clr: #00a0fb;
    }
    *{
        margin: 0;
        padding: 0;
    }

    #sidebar{
        box-sizing: border-box;
        height: calc(40vh);
        width:180px ;
        padding: 2px 1em;
        border-radius: 5px;
        position: fixed;
        top: 120px;
        left:10px;
        align-self: start;
        transition: 300ms ease-in-out;
        overflow: hidden;
        text-wrap: nowrap;
    }
    #sidebar.close{
        padding: 6px;
        width: 50px;
    }
    #sidebar ul{
        list-style: none;
        margin-left:-50px;
    }
    #sidebar > ul > li:first-child{
        display: flex;
        justify-content: flex-end;
        margin-bottom: 16px;
        .logo{
            font-weight: 600;
        }
    }
    #sidebar ul li.active a {
        color: var(--accent-clr);
    }

    #sidebar ul li.active a svg {
        fill: var(--accent-clr);
    }


    #sidebar a, #sidebar .dropdown-btn, #sidebar .logo{
        border-radius: .5em;
        padding: .85em;
        text-decoration: none;
        color: var(--text-clr);
        display: flex;
        align-items: center;
        gap: 1em;
    }
    .dropdown-btn{
        width: 100%;
        text-align: left;
        background: none;
        border: none;
        font: inherit;
        cursor: pointer;
    }
    #sidebar svg{
        flex-shrink: 0;
        fill: var(--text-clr);
    }
    #sidebar a span, #sidebar .dropdown-btn span{
        flex-grow: 1;
    }
    #sidebar a:hover, #sidebar .dropdown-btn:hover{
        background-color: var(--hover-clr);
    }
    #sidebar .sub-menu{
        display: grid;
        grid-template-rows: 0fr;
        transition: 300ms ease-in-out;

        > div{
            overflow: hidden;
        }
    }
    #sidebar .sub-menu.show{
        grid-template-rows: 1fr;
    }
    .dropdown-btn svg{
        transition: 200ms ease;
    }
    .rotate svg:last-child{
        rotate: 180deg;
    }
    #sidebar .sub-menu a{
        padding-left: 2em;
    }
    #toggle-btn{
        margin-left: auto;
        padding: 1em;
        border: none;
        border-radius: .5em;
        background: none;
        cursor: pointer;

        svg{
            transition: rotate 150ms ease;
        }
    }
    #toggle-btn:hover{
        background-color: var(--hover-clr);
    }
    @media(max-width: 800px){

        main{
            padding: 2em 1em 60px 1em;
        }
        .container{
            border: none;
            padding: 0;
        }
        #sidebar{
            height: 60px;
            width: 100%;
            border-right: none;
            border-top: 1px solid var(--line-clr);
            padding: 0;
            position: fixed;
            top: unset;
            bottom: 0;

            > ul{
                padding: 0;
                display: grid;
                grid-auto-columns: 60px;
                grid-auto-flow: column;
                align-items: center;
                overflow-x: scroll;
            }
            ul li{
                height: 100%;
            }
            ul a, ul .dropdown-btn{
                width: 60px;
                height: 60px;
                padding: 0;
                border-radius: 0;
                justify-content: center;
            }

            ul li span, ul li:first-child, .dropdown-btn svg:last-child{
                display: none;
            }

            ul li .sub-menu.show{
                position: fixed;
                bottom: 60px;
                left: 0;
                box-sizing: border-box;
                height: 60px;
                width: 100%;
                background-color: var(--hover-clr);
                border-top: 1px solid var(--line-clr);
                display: flex;
                justify-content: center;

                > div{
                    overflow-x: auto;
                }
                li{
                    display: inline-flex;
                }
                a{
                    box-sizing: border-box;
                    padding: 1em;
                    width: auto;
                    justify-content: center;
                }
            }
        }
    }


</style>
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
                                <c:when test="${order.status eq 'Completed'}"><span class="badge bg-light text-success p-2">Completed</span></c:when>
                                <c:when test="${order.status eq 'Pending'}"><span class="badge bg-light text-warning p-2">Pending</span></c:when>
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