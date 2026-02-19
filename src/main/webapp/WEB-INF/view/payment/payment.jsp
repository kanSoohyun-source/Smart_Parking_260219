<%@ page import="org.example.smart_parking_260219.dto.ParkingDTO" %>
<%@ page import="org.example.smart_parking_260219.service.ParkingService" %>
<%@ page import="org.example.smart_parking_260219.service.PaymentService" %>
<%@ page import="org.example.smart_parking_260219.util.MapperUtil" %>
<%@ page import="org.example.smart_parking_260219.service.FeePolicyService" %>
<%@ page import="org.example.smart_parking_260219.vo.FeePolicyVO" %>
<%@ page import="org.example.smart_parking_260219.dto.PaymentDTO" %>
<%@ page import="static java.time.LocalTime.now" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String carNum = request.getParameter("carNum");

    // 2. 만약 Forward로 올 경우
    if(carNum == null || carNum.isEmpty()) {
        carNum = (String) request.getAttribute("carNum");
    }

    // 3. 데이터가 없을 때 DB 조회를 시도하면 에러가 나므로 조건문 처리
    ParkingDTO parkingDTO = null;
    PaymentDTO paymentDTO = null;
    int calculatedFee = 0;
    int discountAmount = 0;
    int finalFee = 0;

    if(carNum != null && !carNum.isEmpty() && !"null".equals(carNum)) {
        parkingDTO = ParkingService.INSTANCE.getParkingByCarNum(carNum.trim());
//        if (parkingDTO != null) {
//            // 주차 정보가 있을 때만 실행
//            paymentDTO = PaymentService.INSTANCE.getPayment(parkingDTO.getParkingId());
//        }

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

    long totalTime = 0;
    if (parkingDTO != null && parkingDTO.getEntryTime() != null) {
        // 현재 시간과 입차 시간의 차이 계산
        java.time.Duration duration = java.time.Duration.between(
                parkingDTO.getEntryTime(),
                java.time.LocalDateTime.now()
        );
        totalTime = duration.toMinutes();
    }
%>
<html>
<head>
    <title>Payment</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/payment_style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/modal.css">
</head>
<div id="customModal" class="modal-overlay">
    <div class="modal-content">
        <div id="modalBody" class="modal-body">
        </div>
        <div class="modal-footer">
            <button class="btn-confirm" onclick="handleConfirm()">확인</button>
            <button class="btn-cancel" onclick="closeModal()">취소</button>
        </div>
    </div>
</div>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
    <!-- Content -->
    <div id="register" class="page">
        <h2>정산</h2>
        <form name="payment" action="${pageContext.request.contextPath}/payment/payment" method="post">
            <div class="form-group">
                <label>차량 번호</label>
                <input type="text" name ="carNum" id="carNum" placeholder="차량번호 8자리" maxlength="8" value="<%=carNum%>">
            </div>
            <div class="form-group">
                <label>결제 타입</label>
                <div class="radio-group">
                    <label class="radio-item"><input type="radio" name="paymentType" value="1"
                        <% if(parkingDTO.getCarType() != 2) {out.println("checked");} %>>카드</label>
                    <label class="radio-item"><input type="radio" name="paymentType" value="2">현금</label>
                    <label class="radio-item"><input type="radio" name="paymentType" value="3"
                    <% if(parkingDTO.getCarType() == 2) {out.println("checked");} %>>월정액</label>
                </div>
            </div>
            <div class="form-group">
                <label>총 주차 시간</label>
                <input type="text" id="totalParkingTime" placeholder="총 주차 시간" name="totalTime" value="<%=totalTime%>분">
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
            <button type="button" class="btn btn-primary" onclick="showReceipt()">확인</button>
        </form>
    </div>
    <!-- 영수증 -->
    <div id="printArea" style="display: none;">
        <div style="width: 330px; padding: 5px; border: 2px solid #0000FF; color: #0000FF; font-family: 'Malgun Gothic', sans-serif; background-color: #fff; line-height: 1.0;">

            <div style="display: flex; justify-content: space-between; align-items: flex-end; border-bottom: 2px solid #0000FF; padding: 0 0 2px 0; margin-bottom: 3px;">
                <h1 style="margin: 0; font-size: 18px; letter-spacing: 5px;">영 수 증</h1>
                <span style="font-size: 9px;">(공급받는자용)</span>
            </div>

            <table style="width: 100%; border-collapse: collapse; font-size: 11px; border: 1px solid #0000FF;">
                <tr>
                    <td rowspan="4" style="width: 15px; border-right: 1px solid #0000FF; text-align: center; writing-mode: vertical-lr; font-size: 10px; padding: 0;">공급자</td>
                    <td style="width: 60px; border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; text-align: center; padding: 2px 0;">사업자번호</td>
                    <td colspan="3" style="border-bottom: 1px solid #0000FF; text-align: center; color: #000; font-weight: bold; padding: 2px 0;">123-45-67890</td>
                </tr>
                <tr>
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; text-align: center; padding: 2px 0;">상 호</td>
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding-left: 3px; color: #000;">스마트 주차장</td>
                    <td style="width: 30px; border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; text-align: center; padding: 0;">성명</td>
                    <td style="border-bottom: 1px solid #0000FF; padding-right: 3px; text-align: right; color: #000;">홍길동 (인)</td>
                </tr>
                <tr>
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; text-align: center; padding: 2px 0;">주 소</td>
                    <td colspan="3" style="border-bottom: 1px solid #0000FF; padding-left: 3px; color: #000; font-size: 10px;">대구광역시 중구 중앙대로 123</td>
                </tr>
                <tr>
                    <td style="border-right: 1px solid #0000FF; text-align: center; padding: 2px 0;">업 태</td>
                    <td style="border-right: 1px solid #0000FF; padding-left: 3px; color: #000;">서비스</td>
                    <td style="border-right: 1px solid #0000FF; text-align: center; padding: 0;">종목</td>
                    <td style="padding-left: 3px; color: #000;">주차장업</td>
                </tr>
            </table>

            <table style="width: 100%; border-collapse: collapse; margin-top: 3px; border: 1px solid #0000FF; font-size: 11px;">
                <tr style="height: 22px;">
                    <td style="width: 50px; border-right: 1px solid #0000FF; background-color: #f0f4ff; text-align: center;">작성일</td>
                    <td style="width: 100px; border-right: 1px solid #0000FF; text-align: center; color: #000; font-size: 10px;"><%=java.time.LocalDate.now()%></td>
                    <td style="width: 50px; border-right: 1px solid #0000FF; background-color: #f0f4ff; text-align: center;">합계금액</td>
                    <td style="text-align: right; padding-right: 3px; color: #000; font-weight: bold;"><span id="p-finalFee"></span></td>
                </tr>
            </table>

            <table style="width: 100%; border-collapse: collapse; margin-top: 3px; border: 1px solid #0000FF; font-size: 11px; text-align: center;">
                <thead style="background-color: #f0f4ff;">
                <tr style="height: 20px;">
                    <th style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 0;">항 목</th>
                    <th style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 0;">내 용</th>
                    <th style="border-bottom: 1px solid #0000FF; padding: 0;">금 액</th>
                </tr>
                </thead>
                <tbody style="color: #000;">
                <tr style="height: 18px;">
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 0;">차량번호</td>
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 0;"><span id="p-carNum"></span></td>
                    <td style="border-bottom: 1px solid #0000FF; padding: 0;">-</td>
                </tr>
                <tr style="height: 18px;">
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 0;">주차시간</td>
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 0;"><span id="p-totalTime"></span></td>
                    <td style="border-bottom: 1px solid #0000FF; padding: 0;"><span id="p-calcFee"></span></td>
                </tr>
                <tr style="height: 18px;">
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 0;">할인액</td>
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 0;">-</td>
                    <td style="border-bottom: 1px solid #0000FF; padding: 0;">-<span id="p-discount"></span></td>
                </tr>
                </tbody>
                <tfoot>
                <tr style="height: 22px; background-color: #f0f4ff; font-weight: bold;">
                    <td colspan="2" style="border-right: 1px solid #0000FF; text-align: center; padding: 0;">합 계 (VAT포함)</td>
                    <td style="text-align: right; padding-right: 3px; color: #000; padding: 0;"><span id="p-finalFee-total"></span></td>
                </tr>
                </tfoot>
            </table>

            <div style="text-align: center; font-size: 9px; margin-top: 3px; color: #000;">감사합니다. 또 이용해 주십시오.</div>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/JS/menu.js"></script>
<script src="${pageContext.request.contextPath}/JS/function.js"></script>

<script>
    const entryTime = "<%= (parkingDTO != null && parkingDTO.getEntryTime() != null) ? parkingDTO.getEntryTime() : "" %>";

    <%
        String exitTimeStr = "";
        if (parkingDTO != null && parkingDTO.getExitTime() != null) {
            exitTimeStr = parkingDTO.getExitTime().toString();
        } else {
            exitTimeStr = java.time.LocalDateTime.now().toString();
        }
    %>
    const exitTime = "<%= exitTimeStr %>";
</script>

<script src="${pageContext.request.contextPath}/JS/payment.js"></script>
</body>
</html>
