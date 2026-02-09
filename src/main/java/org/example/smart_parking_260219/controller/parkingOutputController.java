package org.example.smart_parking_260219.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.ParkingDTO;
import org.example.smart_parking_260219.dto.ParkingSpotDTO;
import org.example.smart_parking_260219.service.ParkingService;
import org.example.smart_parking_260219.service.ParkingSpotService;

import java.io.IOException;

@WebServlet(name = "parkingOutputController", value = "/parking/output")
@Log4j2
public class parkingOutputController extends HttpServlet {
    private final ParkingService parkingService = ParkingService.INSTANCE;
    private final ParkingSpotService parkingSpotService = ParkingSpotService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/exit/exit.jsp");

        req.getRequestDispatcher("webapp/exit/exit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("/parking/output post...");
        String CarNum = req.getParameter("carNum");
        ParkingDTO parkingDTO = ParkingDTO.builder()
                .carNum(CarNum)
                .build();
        log.info("parkingDTO: {}", parkingDTO);
        parkingService.modifyParking(parkingDTO);

        ParkingSpotDTO parkingSpotDTO = ParkingSpotDTO.builder()
                .carNum(CarNum)
                .build();
        log.info("parkingSpotDTO: {}", parkingSpotDTO);
        parkingSpotService.modifyOutputParkingSpot(parkingSpotDTO);

        resp.sendRedirect("webapp/payment/payment.jsp");
    }
}

