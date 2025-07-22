<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/header.jsp" %>
<style>
    /* ... Giữ nguyên CSS của bạn ... */
    .pricing-box { -webkit-box-shadow: 0 5px 30px -10px rgba(0,0,0,0.1); box-shadow: 0 5px 30px -10px rgba(0,0,0,0.1); padding: 35px 50px; border-radius: 20px; position: relative; height: 100%; display: flex; flex-direction: column; }
    .pricing-box .plan { font-size: 34px; }
    .pricing-badge { position: absolute; top: 0; z-index: 999; right: 0; width: 100%; display: block; font-size: 15px; padding: 0; overflow: hidden; height: 100px; }
    .pricing-badge .badge { float: right; -webkit-transform: rotate(45deg); transform: rotate(45deg); right: -67px; top: 17px; position: relative; text-align: center; width: 200px; font-size: 13px; margin: 0; padding: 7px 10px; font-weight: 500; color: #ffffff; background: #fb7179; }
    .pricing-box .features { flex-grow: 1; }
</style>

<div class="main-content d-flex flex-column min-vh-100">
    <section class="section" id="pricing">
        <div class="container">
            <div class="row justify-content-center mt-5 pt-4">


                <c:forEach var="plan" items="${plans}">
                    <div class="col-lg-4 d-flex align-items-stretch mb-4">
                        <div class="pricing-box">

                            <c:if test="${currentPlanName eq plan.name}">
                                <div class="pricing-badge">
                                    <span class="badge">Current Plan</span>
                                </div>
                            </c:if>

                                <%-- ĐÃ SỬA: Dùng plan.name để quyết định màu sắc --%>
                            <h4 class="f-20 ${plan.name ne 'Free' ? 'text-primary' : ''}">${plan.name}</h4>

                            <div class="mt-4 pt-2 features">
                                <p class="mb-2 f-18">Features</p>
                                <p class="mb-2"><i class="mdi mdi-checkbox-marked-circle text-success f-18 mr-2"></i><b>${plan.maxTestAttempts == -1 ? 'Unlimited' : plan.maxTestAttempts}</b> AI Exam</p>
                                <p class="mb-2"><i class="mdi mdi-checkbox-marked-circle text-success f-18 mr-2"></i><b>${plan.maxPosts == -1 || plan.maxPosts > 10 ? 'Full' : 'Limited'}</b> AI Chatbot function</p>

                                    <%-- ĐÃ SỬA: Dùng plan.name để quyết định tính năng --%>
                                <p class="mb-2"><i class="mdi ${plan.name eq 'Free' ? 'mdi-close-circle text-danger' : 'mdi-checkbox-marked-circle text-success'} f-18 mr-2"></i><b>${plan.name eq 'Free' ? 'No' : 'Mentor\'s'}</b> Support</p>
                                <p class="mb-2"><i class="mdi mdi-checkbox-marked-circle text-success f-18 mr-2"></i><b>${plan.maxPosts == -1 ? 'Unlimited' : plan.maxPosts}</b> Blogs | Q&A</p>
                            </div>

                            <div class="pricing-plan mt-4 pt-2">
                                <h4 class="plan pl-3 text-dark"><fmt:formatNumber value="${plan.price}" type="currency" currencySymbol="VND"/></h4>
                                <p class="text-muted mb-0">Per Month</p>
                            </div>

                            <div class="mt-4 pt-3">
                                <c:choose>
                                    <c:when test="${currentPlanName eq plan.name}">
                                        <button class="btn btn-secondary btn-rounded" disabled>Already Subscribed</button>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-primary btn-rounded" onclick="purchasePlan(${plan.id})">
                                            Purchase Now
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:forEach>

            </div>
        </div>
    </section>
</div>

<script>
    function purchasePlan(planId) {
        // Tạo một form ẩn để gửi yêu cầu thanh toán
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '${pageContext.request.contextPath}/vn_pay/ajax';

        // Input ẩn cho paymentType
        const paymentTypeInput = document.createElement('input');
        paymentTypeInput.type = 'hidden';
        paymentTypeInput.name = 'paymentType';
        paymentTypeInput.value = 'plan';
        form.appendChild(paymentTypeInput);

        // Input ẩn cho planId
        const planIdInput = document.createElement('input');
        planIdInput.type = 'hidden';
        planIdInput.name = 'planId';
        planIdInput.value = planId;
        form.appendChild(planIdInput);

        document.body.appendChild(form);
        form.submit();
        document.body.removeChild(form);
    }
</script>

<%@ include file="/footer.jsp" %>