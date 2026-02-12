package org.example.smart_parking_260219.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.service.ParkingService;
import org.example.smart_parking_260219.service.ParkingSpotService;

import java.io.IOException;

@WebServlet(name = "parkingOutputController", value = "/parking/output")
@Log4j2
public class ParkingOutputController extends HttpServlet {
    private final ParkingService parkingService = ParkingService.INSTANCE;
    private final ParkingSpotService parkingSpotService = ParkingSpotService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/exit/exit.jsp");
        String spaceId = (String) req.getAttribute("id");
        String carNum = req.getParameter("carNum");

        req.setAttribute("carNum", carNum);
        req.setAttribute("id", spaceId);
        req.getRequestDispatcher("/exit/exit_serch_list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.info("/parking/output post...");
        String CarNum = req.getParameter("carNum");
        String spaceId = (String) req.getAttribute("id");

        log.info(CarNum);
        if (parkingService.getParkingByCarNum(CarNum) != null) {
            req.setAttribute("id", spaceId);
            req.setAttribute("carNum", CarNum);
            req.getRequestDispatcher("/exit/exit_serch_list.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("/exit/exit.jsp?fail=false");
        }


    }
}

