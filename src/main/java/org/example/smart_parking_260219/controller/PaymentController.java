package org.example.smart_parking_260219.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.PaymentDTO;
import org.example.smart_parking_260219.service.FeePolicyService;
import org.example.smart_parking_260219.service.ParkingService;
import org.example.smart_parking_260219.service.PaymentService;

import java.io.IOException;

@Log4j2
@WebServlet(name = "paymentController", value = "/payment/payment")

public class PaymentController extends HttpServlet {
    private final PaymentService paymentService = PaymentService.INSTANCE;
    private final ParkingService parkingService = ParkingService.INSTANCE;
    private final FeePolicyService feePolicyService = FeePolicyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/payment get...");

        // /WEB-INF -> jsp 파일 위치 옮긴 다음 추가
        req.getRequestDispatcher("/WEB-INF/view/payment/payment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/payment post start...");

        try {
            String carNum = req.getParameter("carNum");
            int paymentType = Integer.parseInt(req.getParameter("paymentType"));
            int calculatedFee = Integer.parseInt(req.getParameter("calculatedFee"));
            int discountAmount = Integer.parseInt(req.getParameter("discountAmount"));
            int finalFee = Integer.parseInt(req.getParameter("finalFee"));

            // [중요] 상태 변경 전에 미리 ID를 확보해야 합니다.
            var parkingDTO = parkingService.getParkingByCarNum(carNum);
            if (parkingDTO == null) {
                log.error("해당 차량의 주차 기록을 찾을 수 없습니다: " + carNum);
                resp.sendRedirect(req.getContextPath() + "/dashboard");
                return;
            }

            log.info("parkingDTO, " + parkingDTO);

            PaymentDTO paymentDTO = PaymentDTO.builder()
                    .carNum(carNum)
                    .parkingId(parkingDTO.getParkingId())
                    .policyId(feePolicyService.getPolicy().getPolicyId())
                    .paymentType(paymentType)
                    .calculatedFee(calculatedFee)
                    .discountAmount(discountAmount)
                    .finalFee(finalFee)
                    .build();

            log.info("paymentDTO, " + paymentDTO);

            // 순서 주의: 결제 내역을 먼저 넣고, 주차 상태를 나중에 바꿉니다.
            paymentService.addPayment(paymentDTO);
            parkingService.modifyParking(carNum);

            log.info("Payment and Parking update success!");

            // 이동할 때 ContextPath를 포함한 올바른 URL로 이동
            resp.sendRedirect(req.getContextPath() + "/dashboard"); // 대시보드 URL로 수정

        } catch (Exception e) {
            log.error("결제 처리 중 에러 발생: " + e.getMessage());
            throw new ServletException(e);
        }
    }
}
