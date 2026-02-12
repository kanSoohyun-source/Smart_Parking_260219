package org.example.smart_parking_260219.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.MemberDTO;
import org.example.smart_parking_260219.dto.ParkingDTO;
import org.example.smart_parking_260219.dto.ParkingSpotDTO;
import org.example.smart_parking_260219.service.MemberService;
import org.example.smart_parking_260219.service.ParkingService;
import org.example.smart_parking_260219.service.ParkingSpotService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "parkingInputController", value = "/parking/input")
@Log4j2
public class ParkingInputController extends HttpServlet {
    private final ParkingService parkingService = ParkingService.INSTANCE;
    private final ParkingSpotService parkingSpotService = ParkingSpotService.INSTANCE;
    private final MemberService memberService = MemberService.Instance;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/entry/entry.jsp");
        String carNum = req.getParameter("carNum");

        req.setAttribute("carNum", carNum);

        req.getRequestDispatcher("webapp/entry/entry.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.info("/parking/input post...");
        String carNum = req.getParameter("carNum");
        log.info(carNum);
        String spaceId = req.getParameter("spaceId");
        MemberDTO memberDTO;
        try {
            memberDTO = memberService.getOneMember(carNum);
        } catch (SQLException e) {
            memberDTO = null;
        }
        if (memberDTO == null) {
            req.setAttribute("carNum", carNum);
            req.setAttribute("id", spaceId);
            req.getRequestDispatcher("/entry/add_non_member.jsp").forward(req, resp);
            return;
        }
        log.info(parkingSpotService.getParkingSpotBySpaceId(spaceId).getEmpty());
        if (parkingSpotService.getParkingSpotBySpaceId(spaceId).getEmpty()) {
            resp.sendRedirect("/entry/entry.jsp?fail=false");
            return;
        }

        if (parkingService.getParkingByCarNum(carNum) == null || parkingService.getParkingByCarNum(carNum).isPaid()) {
            ParkingSpotDTO parkingSpotDTO = ParkingSpotDTO.builder()
                    .carNum(carNum)
                    .spaceId(spaceId)
                    .build();
            log.info("parkingSpotDTO: {}", parkingSpotDTO);
            parkingSpotService.modifyInputParkingSpot(parkingSpotDTO);

            ParkingDTO parkingDTO = ParkingDTO.builder()
                    .memberId(memberDTO.getMemberId())
                    .carNum(carNum)
                    .spaceId(spaceId)
                    .carType(memberDTO.getCarType())
                    .build();
            log.info("parkingDTO: {}", parkingDTO);
            parkingService.addParking(parkingDTO);

            resp.sendRedirect("/dashboard/dashboard.jsp");
        } else {
            resp.sendRedirect("/dashboard/dashboard.jsp?fail=false");
        }

    }
}
