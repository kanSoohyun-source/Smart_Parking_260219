<%@ page import="org.example.smart_parking_260219.dto.ParkingDTO" %>
<%@ page import="org.example.smart_parking_260219.dto.PaymentDTO" %>
<%@ page import="org.example.smart_parking_260219.service.ParkingService" %>
<%@ page import="org.example.smart_parking_260219.service.PaymentService" %>
<%@ page import="org.example.smart_parking_260219.util.MapperUtil" %>
<%@ page import="org.example.smart_parking_260219.service.FeePolicyService" %>
<%@ page import="org.example.smart_parking_260219.vo.FeePolicyVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 1. Controller가 setAttribute로 보낸 값 확인
    String carNum = (String) request.getAttribute("carNum");

    // 2. 만약 null이면 이전 페이지에서 넘어온 Parameter 확인
    if(carNum == null) {
        carNum = request.getParameter("carNum");
    }

    // 3. carNum이 확실히 있을 때만 Service 호출 (NPE 방지)
    ParkingDTO parkingDTO = null;
    PaymentDTO paymentDTO = null;

    int calculatedFee = 0;
    int discountAmount = 0;
    int finalFee = 0;

    if(carNum != null && !carNum.isEmpty()) {
        parkingDTO = ParkingService.INSTANCE.getParkingByCarNum(carNum);
        if(parkingDTO != null) {
            calculatedFee = PaymentService.INSTANCE.calculateFeeLogic(parkingDTO);
            discountAmount = PaymentService.INSTANCE.calculateDiscountLogic(calculatedFee, parkingDTO.getCarType(),
                    MapperUtil.INSTANCE.getInstance().map(FeePolicyService.getInstance().getPolicy(), FeePolicyVO.class));
            finalFee = calculatedFee - discountAmount;
        }
    }
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
<form class="main-content">
    <!-- Content -->
    <div id="register" class="page">
        <h2>정산</h2>
        <form name="payment" action="../payment/payment" method="post">
            <div class="form-group">
                <label>차량 번호</label>
                <input type="text" name ="carNum" id="carNum" placeholder="차량번호 8자리" maxlength="8" value="<%=carNum%>">
            </div>
            <div class="form-group">
                <label>결제 타입</label>
                <div class="radio-group">
                    <label class="radio-item"><input type="radio" name="paymentType" value="1" checked>카드</label>
                    <label class="radio-item"><input type="radio" name="paymentType" value="2">현금</label>
                    <label class="radio-item"><input type="radio" name="paymentType" value="3">월정액</label>
                </div>
            </div>
            <div class="form-group">
                <label>할인 전 요금</label>
                <input type="text" name="calculatedFee" id="calculatedFee" placeholder="할인 전 요금" value="<%=calculatedFee%>">
            </div>
            <div class="form-group">
                <label>할인액</label>
                <input type="text" name="discountAmount" id="discountAmount" placeholder="할인액" value="<%=discountAmount%>">
            </div>
            <div class="form-group">
                <label>총 주차 요금</label>
                <input type="text" name="finalFee" id="finalFee" placeholder="총 주차 요금" value="<%=finalFee%>">
            </div>
            <button type="submit" class="btn btn-primary">확인</button>
        </form>
    </div>
</form>
<script src="../JS/menu.js"></script>
<script src="../JS/function.js"></script>
</body>
</html>
