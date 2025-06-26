<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="header.jsp" %>
<style>
    .pricing-box {
        -webkit-box-shadow: 0px 5px 30px -10px rgba(0, 0, 0, 0.1);
        box-shadow: 0px 5px 30px -10px rgba(0, 0, 0, 0.1);
        padding: 35px 50px;
        border-radius: 20px;
        position: relative;
    }
    .pricing-box .plan {
        font-size: 34px;
    }
    .pricing-badge {
        position: absolute;
        top: 0;
        z-index: 999;
        right: 0;
        width: 100%;
        display: block;
        font-size: 15px;
        padding: 0;
        overflow: hidden;
        height: 100px;
    }
    .pricing-badge .badge {
        float: right;
        -webkit-transform: rotate(45deg);
        transform: rotate(45deg);
        right: -67px;
        top: 17px;
        position: relative;
        text-align: center;
        width: 200px;
        font-size: 13px;
        margin: 0;
        padding: 7px 10px;
        font-weight: 500;
        color: #ffffff;
        background: #fb7179;
    }
    .mb-2, .my-2 {
        margin-bottom: .5rem!important;
    }
    p {
        line-height: 1.7;
    }
</style>
<div class="main-content d-flex flex-column min-vh-100">
<section class="section" id="pricing">
    <div class="container">
        <div class="row mt-5 pt-4">
            <div class="col-lg-4">
                <div class="pricing-box mt-4">
                    <c:if test="${currentPlanName eq 'Free'}">
                        <div class="pricing-badge">
                            <span class="badge">Current Plan</span>
                        </div>
                    </c:if>

                    <i class="mdi mdi-account h1"></i>
                    <h4 class="f-20">Starter</h4>
                    <div class="mt-4 pt-2">
                        <p class="mb-2 f-18">Features</p>
                        <p class="mb-2"><i class="mdi mdi-checkbox-marked-circle text-success f-18 mr-2"></i><b>5 Free</b> AI Exam</p>
                        <p class="mb-2"><i class="mdi mdi-checkbox-marked-circle text-success f-18 mr-2"></i><b>Limited</b> AI Chatbot function</p>
                        <p class="mb-2"><i class="mdi mdi-close-circle text-danger f-18 mr-2"></i><b>No</b> Mentor's support</p>
                        <p class="mb-2"><i class="mdi mdi-close-circle text-danger f-18 mr-2"></i><b>1</b> Blogs | Q&A</p>
                    </div>
                    <p class="mt-4 pt-2 text-muted"></p>
                    <div class="pricing-plan mt-4 pt-2">
                        <h4 class="text-muted"><s> 0 VND</s> <span class="plan pl-3 text-dark">Free</span></h4>
                        <p class="text-muted mb-0">Per Month</p>
                    </div>
                    <div class="mt-4 pt-3">
                        <button class="btn btn-secondary btn-rounded" disabled>Already Subscribed</button>
                    </div>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="pricing-box mt-4">
                    <c:if test="${currentPlanName eq 'Personal'}">
                        <div class="pricing-badge">
                            <span class="badge">Current Plan</span>
                        </div>
                    </c:if>

                    <i class="mdi mdi-account-multiple h1 text-primary"></i>
                    <h4 class="f-20 text-primary">Personal</h4>
                    <div class="mt-4 pt-2">
                        <p class="mb-2 f-18">Features</p>
                        <p class="mb-2"><i class="mdi mdi-checkbox-marked-circle text-success f-18 mr-2"></i><b>30</b> AI Exam</p>
                        <p class="mb-2"><i class="mdi mdi-checkbox-marked-circle text-success f-18 mr-2"></i><b>Full</b> AI Chatbiot function</p>
                        <p class="mb-2"><i class="mdi mdi-checkbox-marked-circle text-success f-18 mr-2"></i><b>Mentor's</b> Support</p>
                        <p class="mb-2"><i class="mdi mdi-checkbox-marked-circle text-success f-18 mr-2"></i><b>10</b> Blogs | Q&A</p>
                    </div>
                    <p class="mt-4 pt-2 text-muted"></p>
                    <div class="pricing-plan mt-4 pt-2">
                        <h4 class="text-muted"><s> 299.000 VND</s> <span class="plan pl-3 text-dark">199.000VND</span></h4>
                        <p class="text-muted mb-0">Per Month</p>
                    </div>
                    <div class="mt-4 pt-3">
                        <c:choose>
                            <c:when test="${plan.name eq currentPlanName}">
                                <button class="btn btn-secondary btn-rounded" disabled>Already Subscribed</button>
                            </c:when>

                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/payment?action=payplan&planName=Personal&planId=2"
                                   class="btn btn-primary btn-rounded">
                                    Purchase Now
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>

                </div>
            </div>
            <div class="col-lg-4">
                <div class="pricing-box mt-4">
                    <c:if test="${currentPlanName eq 'Ultimate'}">
                        <div class="pricing-badge">
                            <span class="badge">Current Plan</span>
                        </div>
                    </c:if>

                    <i class="mdi mdi-account-multiple-plus h1"></i>
                    <h4 class="f-20">Ultimate</h4>
                    <div class="mt-4 pt-2">
                        <p class="mb-2 f-18">Features</p>
                        <p class="mb-2"><i class="mdi mdi-checkbox-marked-circle text-success f-18 mr-2"></i><b>Unlimited</b> AI Exam</p>
                        <p class="mb-2"><i class="mdi mdi-checkbox-marked-circle text-success f-18 mr-2"></i><b>Full </b> AI Chatbot function</p>
                        <p class="mb-2"><i class="mdi mdi-checkbox-marked-circle text-success f-18 mr-2"></i><b>Mentor's</b> Suport</p>
                        <p class="mb-2"><i class="mdi mdi-checkbox-marked-circle text-success f-18 mr-2"></i><b>Unlimited</b> Blogs | Q&A</p>
                    </div>
                    <p class="mt-4 pt-2 text-muted"></p>
                    <div class="pricing-plan mt-4 pt-2">
                        <h4 class="text-muted"><s> 599.000 VND</s> <span class="plan pl-3 text-dark">399.000 VND</span></h4>
                        <p class="text-muted mb-0">Per Month</p>
                    </div>
                    <div class="mt-4 pt-3">
                        <c:choose>
                            <c:when test="${plan.name eq currentPlanName}">
                                <button class="btn btn-secondary btn-rounded" disabled>Already Subscribed</button>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/payment?action=payplan&planName=Ultimate&planId=2"
                                   class="btn btn-primary btn-rounded">
                                    Purchase Now
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>

                </div>
            </div>
        </div>
    </div>
</section>
</div>
<script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/js/bootstrap.bundle.min.js"></script>
<%@ include file="footer.jsp" %>