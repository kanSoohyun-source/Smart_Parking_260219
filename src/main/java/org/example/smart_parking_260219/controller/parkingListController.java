package org.example.smart_parking_260219.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.ParkingDTO;
import org.example.smart_parking_260219.service.ParkingService;

import java.io.IOException;
import java.util.List;

@Log4j2
@WebServlet(name = "parkingGetController", value = "/parking/get")
public class parkingListController extends HttpServlet {
    private final ParkingService parkingService = ParkingService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("parking get...");
        String num = req.getParameter("car_num");
        ParkingDTO parkingDTO = parkingService.getParking(num);
        req.setAttribute("dto", parkingDTO);
        req.getRequestDispatcher("webapp/member/member_search.jsp").forward(req, resp);
    }
}
