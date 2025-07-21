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