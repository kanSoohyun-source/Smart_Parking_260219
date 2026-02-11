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
        req.getRequestDispatcher("/payment/payment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/payment post...");

        String carNum = req.getParameter("carNum");
        int paymentType = Integer.parseInt(req.getParameter("paymentType"));
        int calculatedFee = Integer.parseInt(req.getParameter("calculatedFee"));
        int discountAmount = Integer.parseInt(req.getParameter("discountAmount"));
        int finalFee = Integer.parseInt(req.getParameter("finalFee"));

        PaymentDTO paymentDTO = PaymentDTO.builder()
                .carNum(carNum)
                .parkingId(parkingService.getParkingByCarNum(carNum).getParkingId())
                .policyId(feePolicyService.getPolicy().getPolicyId())
                .paymentType(paymentType)
                .calculatedFee(calculatedFee)
                .discountAmount(discountAmount)
                .finalFee(finalFee).build();

        try {
            paymentService.addPayment(paymentDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        resp.sendRedirect("/dashboard/dashboard.jsp");
    }
}
