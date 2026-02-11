<%@ page import="org.example.smart_parking_260219.dto.ParkingDTO" %>
<%@ page import="org.example.smart_parking_260219.service.ParkingService" %>
<%@ page import="org.example.smart_parking_260219.service.PaymentService" %>
<%@ page import="org.example.smart_parking_260219.util.MapperUtil" %>
<%@ page import="org.example.smart_parking_260219.service.FeePolicyService" %>
<%@ page import="org.example.smart_parking_260219.vo.FeePolicyVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String carNum = request.getParameter("carNum");

    // 2. 만약 Forward로 올 경우
    if(carNum == null || carNum.isEmpty()) {
        carNum = (String) request.getAttribute("carNum");
    }

    // 3. 데이터가 없을 때 DB 조회를 시도하면 에러가 나므로 조건문 처리
    ParkingDTO parkingDTO = null;
    int calculatedFee = 0;
    int discountAmount = 0;
    int finalFee = 0;

    if(carNum != null && !carNum.isEmpty() && !"null".equals(carNum)) {
        parkingDTO = ParkingService.INSTANCE.getParkingByCarNum(carNum);
        // DB에서 차량을 못 찾았을 경우의 처리
        if (parkingDTO == null) {
            out.println("<script>alert('현재 주차 중인 차량이 아닙니다.'); location.href='../dashboard/dashboard.jsp';</script>");
            return;
        }
        calculatedFee = PaymentService.INSTANCE.calculateFeeLogic(parkingDTO);
        discountAmount = PaymentService.INSTANCE.calculateDiscountLogic(calculatedFee, parkingDTO.getCarType(),
                MapperUtil.INSTANCE.getInstance().map(FeePolicyService.getInstance().getPolicy(), FeePolicyVO.class));
        finalFee = calculatedFee - discountAmount;
    } else {
        // 차 번호가 없으면 에러 페이지로 보내거나 메시지 출력
        out.println("<script>alert('차량 정보가 없습니다.'); history.back();</script>");
        return;
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
<div class="main-content">
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
                <label>총 주차 시간</label>
                <input type="text" id="totalParkingTime" placeholder="총 주차 시간" name="totalTime" value="<%=parkingDTO.getTotalTime()%>분">
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
</div>
<script src="../JS/menu.js"></script>
<script src="../JS/function.js"></script>
</body>
</html>
