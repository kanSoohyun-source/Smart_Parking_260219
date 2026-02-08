package org.example.smart_parking_260219.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.CarParkDTO;
import org.example.smart_parking_260219.dto.ParkingDTO;
import org.example.smart_parking_260219.service.CarParkService;
import org.example.smart_parking_260219.service.ParkingService;

import java.io.IOException;

@WebServlet(name = "parkingInputController", value = "/parking/input")
@Log4j2
public class parkingInputController extends HttpServlet {
    private final ParkingService parkingService = ParkingService.INSTANCE;
    private final CarParkService carParkService = CarParkService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/entry/entry.jsp");

        req.getRequestDispatcher("webapp/entry/entry.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/parking/input post...");
        String carNum = req.getParameter("carNum");
        String memberId = req.getParameter("memberId");
        String spaceId = req.getParameter("spaceId");
        String carType = req.getParameter("carType");
        CarParkDTO carParkDTO = CarParkDTO.builder()
                .carNum(carNum)
                .space(spaceId)
                .build();
        log.info("carParkDTO: {}", carParkDTO);
        carParkService.modifyInputCarPark(carParkDTO);

        ParkingDTO parkingDTO = ParkingDTO.builder()
                .memberId(Integer.parseInt(memberId))
                .carNum(carNum)
                .spaceId(spaceId)
                .carType(Integer.parseInt(carType))
                .build();
        log.info("parkingDTO: {}", parkingDTO);
        parkingService.addParking(parkingDTO);

        resp.sendRedirect("/todo/dashboard");
    }
}
