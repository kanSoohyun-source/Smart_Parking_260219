<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 26. 1. 28.
  Time: 오후 9:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>요금 부과 정책</title>
    <link rel="stylesheet" href="/CSS/style.css">
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
    <!-- Content -->
    <div class="row">
        <div class="col-md-8">
            <!-- 요금 정책 저장 폼 -->
            <form name="frmFeePolicy" action="/policy/add" method="post" class="form-horizontal">
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">무료 회차(분)</label>
                    <div class="col-sm-6">
                        <input type="number" name="gracePeriod" class="form-control" value="10" min="0" required>
                    </div>
                </div>

                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">기본 시간(분)</label>
                    <div class="col-sm-6">
                        <input type="number" name="defaultTime" class="form-control" value="30" min="1" required>
                    </div>
                </div>

                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">기본 요금(원)</label>
                    <div class="col-sm-6">
                        <input type="number" name="defaultFee" class="form-control" value="2000" min="0" required>
                    </div>
                </div>

                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">추가 시간(분)</label>
                    <div class="col-sm-6">
                        <input type="number" name="extraTime" class="form-control" value="10" min="1" required>
                    </div>
                </div>

                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">추가 요금(원)</label>
                    <div class="col-sm-6">
                        <input type="number" name="extraFee" class="form-control" value="1000" min="0" required>
                    </div>
                </div>

                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">월정액(원)</label>
                    <div class="col-sm-6">
                        <input type="number" name="subscribedFee" class="form-control" value="15000" min="0"
                               required>
                    </div>
                </div>

                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">일일 최대(원)</label>
                    <div class="col-sm-6">
                        <input type="number" name="maxDailyFee" class="form-control" value="50000" min="0" required>
                    </div>
                </div>

                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">경차 할인율(0~100)</label>
                    <div class="col-sm-6">
                        <input type="number" name="lightDiscount" class="form-control" value="30" min="0" max="100"
                               step="1" required>
                        <small class="form-text text-muted">예: 30% 할인</small>
                    </div>
                </div>

                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">장애인 할인율(0~100)</label>
                    <div class="col-sm-6">
                        <input type="number" name="disabledDiscount" class="form-control" value="50" min="0"
                               max="100" step="1" required>
                        <small class="form-text text-muted">예: 50% 할인</small>
                    </div>
                </div>

                <!-- 새 정책은 활성으로 저장 -->
                <input type="hidden" name="isActive" value="true">

                <div class="form-group row">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" onclick="registerMember()">저장</button>
                    </div>
                </div>
            </form>

        </div>
    </div>
</div>
<script src="../JS/menu.js"></script>
</body>
</html>
