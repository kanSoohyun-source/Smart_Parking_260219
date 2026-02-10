<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String carNum = request.getParameter("regCarNum");
    // (1:카드, 2:현금, 3:월정액)
%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="../CSS/style.css">
    <link rel="stylesheet" href="../CSS/payment_style.css">
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="container main-content">
    <div id="register" class="page mt-5">
        <h2>정산</h2>
        <form name="payment" action="../payment/payment" method="post">
            <div class="form-group">
                <label>차량 번호</label>
                <input type="text" name="carNum" id="CarNum" class="form-control"
                       placeholder="차량번호 8자리" maxlength="8" value="<%=carNum%>">
            </div>
            <div class="form-group">
                <label>결제 타입</label>
                <div class="radio-group">
                    <label class="radio-item"><input type="radio" name="paymentType" value="1" checked>카드</label>
                    <label class="radio-item"><input type="radio" name="paymentType" value="2">현금</label>
                    <label class="radio-item"><input type="radio" name="paymentType" value="3">월정액</label>
                </div>
            </div>
            <div class="mt-4">
                <button type="submit" class="btn btn-primary">확인</button>
            </div>
        </form>
    </div>
</div>
    <script src="../JS/menu.js"></script>
    <script src="../JS/function.js"></script>
</body>
</html>
