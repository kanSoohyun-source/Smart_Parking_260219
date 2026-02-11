//package org.example.smart_parking_260219.controller;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.log4j.Log4j2;
//import org.example.smart_parking_260219.HelloServlet;
//import org.example.smart_parking_260219.dto.SubscribeDTO;
//import org.example.smart_parking_260219.service.FeePolicyService;
//import org.example.smart_parking_260219.service.MemberService;
//
//import java.io.IOException;
//import java.time.LocalDate;
//
//@Log4j2
//@WebServlet(name = "addSubscribed", value = "/subscribe/add_subscribe")
//public class AddSubscribedFeeController extends HelloServlet {
//    private final SubscribeService subscribeService = SubscribeService.INSTANCE;
//    private final MemberService memberService = MemberService.Instance;
//    private final FeePolicyService feePolicyService = FeePolicyService.getInstance();
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        log.info("=== /subscribe/add_subscribe POST 요청 시작 ===");
//
//        try {
//            // 파라미터 받기
//            String carNum = req.getParameter("carNum");
//            String startDateStr = req.getParameter("startDate");
//            String endDateStr = req.getParameter("endDate");
//
//            log.info("받은 파라미터 - carNum: {}, startDate: {}, endDate: {}",
//                    carNum, startDateStr, endDateStr);
//
//            // 파라미터 검증
//            if (carNum == null || carNum.trim().isEmpty() ||
//                    startDateStr == null || startDateStr.trim().isEmpty() ||
//                    endDateStr == null || endDateStr.trim().isEmpty()) {
//
//                log.error("필수 파라미터 누락");
//                resp.sendRedirect("/subscribe/add_subscribed.jsp?error=missing");
//                return;
//            }
//
//            // 차량번호 존재 확인
//            boolean memberExists = memberService.isCarNumExists(carNum);
//            if (!memberExists) {
//                log.error("존재하지 않는 차량번호: {}", carNum);
//                resp.sendRedirect("/subscribe/add_subscribed.jsp?error=carNotFound");
//                return;
//            }
//
//            // 날짜 변환
//            LocalDate startDate = LocalDate.parse(startDateStr);
//            LocalDate endDate = LocalDate.parse(endDateStr);
//
//            // 날짜 유효성 검증
//            if (!startDate.isBefore(endDate)) {
//                log.error("종료일이 시작일보다 이전이거나 같음");
//                resp.sendRedirect("/subscribe/add_subscribed.jsp?error=invalidDate");
//                return;
//            }
//
//            //  fee_policy 테이블에서 월정액 요금 조회
//            Integer subscribeFee = feePolicyService.getSubscribeFee();
//            log.info("조회된 월정액 요금: {}", subscribeFee);
//
//            // DTO 생성
//            SubscribeDTO subscribeDTO = SubscribeDTO.builder()
//                    .carNum(carNum)
//                    .startDate(startDate)
//                    .endDate(endDate)
//                    .status(true)
//                    .paymentAmount(subscribeFee)  // 하드코딩 대신 조회한 값 사용
//                    .build();
//
//            // 서비스 호출
//            subscribeService.addSubscribe(subscribeDTO);
//
//            log.info("월정액 가입 완료: {}", carNum);
//
//            // 성공 시 리다이렉트
//            resp.sendRedirect("/subscribe/add_subscribed.jsp?success=true");
//
//        } catch (Exception e) {
//            log.error("월정액 가입 처리 중 오류 발생", e);
//            resp.sendRedirect("/subscribe/add_subscribed.jsp?error=fail");
//        }
//
//        log.info("=== /subscribe/add_subscribe POST 요청 종료 ===");
//    }
//}
//
