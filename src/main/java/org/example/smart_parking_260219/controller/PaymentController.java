package org.example.smart_parking_260219.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dao.FeePolicyDAO;
import org.example.smart_parking_260219.dao.ParkingDAO;
import org.example.smart_parking_260219.dao.ParkingDAOImpl;
import org.example.smart_parking_260219.service.PaymentService;

import java.io.IOException;

@Log4j2
@WebServlet(name = "paymentController", value = "/payment/payment")

public class PaymentController extends HttpServlet {
    private final PaymentService paymentService = PaymentService.INSTANCE;
    private final ParkingDAO parkingDAO = new ParkingDAOImpl();
    private final FeePolicyDAO feePolicyDAO = new FeePolicyDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/payment get...");

        // /WEB-INF -> jsp 파일 위치 옮긴 다음 추가
        req.getRequestDispatcher("/payment/payment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/payment post...");

    }
}
