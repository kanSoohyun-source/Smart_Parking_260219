<%@ page import="org.example.smart_parking_260219.dto.ParkingDTO" %>
<%@ page import="org.example.smart_parking_260219.service.ParkingService" %>
<%@ page import="org.example.smart_parking_260219.service.PaymentService" %>
<%@ page import="org.example.smart_parking_260219.util.MapperUtil" %>
<%@ page import="org.example.smart_parking_260219.service.FeePolicyService" %>
<%@ page import="org.example.smart_parking_260219.vo.FeePolicyVO" %>
<%@ page import="org.example.smart_parking_260219.dto.PaymentDTO" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="org.apache.logging.log4j.LogManager" %>
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
        if (parkingDTO != null) {
            // 주차 정보가 있을 때만 실행
            paymentDTO = PaymentService.INSTANCE.getPayment(parkingDTO.getParkingId());
        }

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
            <button type="button" class="btn btn-primary" onclick="showReceipt()">확인</button>
        </form>
    </div>
    <div id="printArea" style="display: none;">
        <div style="width: 400px; padding: 15px; border: 2px solid #0000FF; color: #0000FF; font-family: 'Malgun Gothic', sans-serif; background-color: #fff;">

            <div style="display: flex; justify-content: space-between; align-items: flex-end; border-bottom: 2px solid #0000FF; padding-bottom: 5px;">
                <h1 style="margin: 0; font-size: 24px; letter-spacing: 10px;">영 수 증</h1>
                <span style="font-size: 12px;">(공급받는자용)</span>
            </div>

            <table style="width: 100%; border-collapse: collapse; margin-top: 10px; font-size: 12px; border: 1px solid #0000FF;">
                <tr>
                    <td rowspan="4" style="width: 20px; border-right: 1px solid #0000FF; text-align: center; writing-mode: vertical-lr;">공 급 자</td>
                    <td style="width: 80px; border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 3px; text-align: center;">사업자번호</td>
                    <td colspan="3" style="border-bottom: 1px solid #0000FF; padding: 3px; text-align: center; color: #000; font-weight: bold;">123-45-67890</td>
                </tr>
                <tr>
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 3px; text-align: center;">상 호</td>
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 3px; color: #000;">스마트 주차장</td>
                    <td style="width: 30px; border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 3px; text-align: center;">성 명</td>
                    <td style="border-bottom: 1px solid #0000FF; padding: 3px; text-align: right; color: #000;">홍길동 (인)</td>
                </tr>
                <tr>
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 3px; text-align: center;">주 소</td>
                    <td colspan="3" style="border-bottom: 1px solid #0000FF; padding: 3px; color: #000;">대구광역시 중구 중앙대로 123</td>
                </tr>
                <tr>
                    <td style="border-right: 1px solid #0000FF; padding: 3px; text-align: center;">업 태</td>
                    <td style="border-right: 1px solid #0000FF; padding: 3px; color: #000;">서비스</td>
                    <td style="border-right: 1px solid #0000FF; padding: 3px; text-align: center;">종 목</td>
                    <td style="padding: 3px; color: #000;">주차장업</td>
                </tr>
            </table>

            <table style="width: 100%; border-collapse: collapse; margin-top: 10px; border: 1px solid #0000FF; font-size: 13px;">
                <tr>
                    <td style="width: 80px; border-right: 1px solid #0000FF; background-color: #f0f4ff; text-align: center; padding: 5px;">작성일</td>
                    <td style="width: 150px; border-right: 1px solid #0000FF; text-align: center; color: #000;"><%=paymentDTO.getPaymentDate()%></td>
                    <td style="width: 80px; border-right: 1px solid #0000FF; background-color: #f0f4ff; text-align: center; padding: 5px;">공급가액</td>
                    <td style="text-align: right; padding-right: 5px; color: #000; font-weight: bold;"><span id="p-finalFee"></span></td>
                </tr>
                <tr>
                    <td colspan="4" style="padding: 5px; text-align: center; font-weight: bold; border-top: 1px solid #0000FF;">
                        위 금액을 영수(청구)함.
                    </td>
                </tr>
            </table>

            <table style="width: 100%; border-collapse: collapse; margin-top: 10px; border: 1px solid #0000FF; font-size: 12px; text-align: center;">
                <thead style="background-color: #f0f4ff;">
                <tr>
                    <th style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 5px;">항 목</th>
                    <th style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 5px;">내 용</th>
                    <th style="border-bottom: 1px solid #0000FF; padding: 5px;">금 액</th>
                </tr>
                </thead>
                <tbody style="color: #000;">
                <tr>
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 5px;">차량번호</td>
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 5px;"><span id="p-carNum"></span></td>
                    <td style="border-bottom: 1px solid #0000FF; padding: 5px;">-</td>
                </tr>
                <tr>
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 5px;">주차시간</td>
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 5px;"><span id="p-totalTime"></span></td>
                    <td style="border-bottom: 1px solid #0000FF; padding: 5px;"><span id="p-calcFee"></span></td>
                </tr>
                <tr>
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 5px;">할인액</td>
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 5px;">-</td>
                    <td style="border-bottom: 1px solid #0000FF; padding: 5px;">-<span id="p-discount"></span></td>
                </tr>
                <% for(int i=0; i<3; i++) { %>
                <tr>
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 5px; height: 20px;"></td>
                    <td style="border-right: 1px solid #0000FF; border-bottom: 1px solid #0000FF; padding: 5px;"></td>
                    <td style="border-bottom: 1px solid #0000FF; padding: 5px;"></td>
                </tr>
                <% } %>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="2" style="border-right: 1px solid #0000FF; padding: 5px; background-color: #f0f4ff; font-weight: bold;">합 계</td>
                    <td style="padding: 5px; font-weight: bold; color: #000;"><span id="p-finalFee-total"></span></td>
                </tr>
                </tfoot>
            </table>
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
