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

// [버그수정] /parking/input → /input
@WebServlet(name = "parkingInputController", value = "/input")
@Log4j2
public class ParkingInputController extends HttpServlet {
    private final ParkingService parkingService = ParkingService.INSTANCE;
    private final ParkingSpotService parkingSpotService = ParkingSpotService.INSTANCE;
    private final MemberService memberService = MemberService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("GET /input - 입차 페이지 이동");
        String spaceId = req.getParameter("id");
        String carNum = req.getParameter("carNum");

        req.setAttribute("id", spaceId);
        req.setAttribute("carNum", carNum);

        req.getRequestDispatcher("/WEB-INF/view/entry/entry.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.info("POST /input - 입차 처리");

        String carNum = req.getParameter("carNum");
        if (carNum == null || carNum.isEmpty() || carNum.length() > 8) {
            req.setAttribute("id", req.getParameter("spaceId"));
            req.setAttribute("fail", "over");
            req.getRequestDispatcher("/WEB-INF/view/entry/entry.jsp").forward(req, resp);
            return;
        }

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
            req.getRequestDispatcher("/WEB-INF/view/entry/add_non_member.jsp").forward(req, resp);
            return;
        }

        ParkingSpotDTO spotDTO = parkingSpotService.getParkingSpotBySpaceId(spaceId);
        log.info("spaceId: {}, empty: {}", spaceId, spotDTO.getEmpty());

        if (!spotDTO.getEmpty()) {
            req.setAttribute("id", spaceId);
            req.setAttribute("fail", "false");
            req.getRequestDispatcher("/WEB-INF/view/entry/entry.jsp").forward(req, resp);
            return;
        }

        ParkingDTO existingParking = parkingService.getParkingByCarNum(carNum);
        if (existingParking != null && !existingParking.isPaid()) {
            req.setAttribute("id", spaceId);
            req.setAttribute("fail", "false");
            req.getRequestDispatcher("/WEB-INF/view/entry/entry.jsp").forward(req, resp);
            return;
        }

        ParkingSpotDTO parkingSpotDTO = ParkingSpotDTO.builder()
                .carNum(carNum)
                .spaceId(spaceId)
                .build();
        parkingSpotService.modifyInputParkingSpot(parkingSpotDTO);

        ParkingDTO parkingDTO = ParkingDTO.builder()
                .memberId(memberDTO.getMemberId())
                .carNum(carNum)
                .spaceId(spaceId)
                .carType(memberDTO.getCarType())
                .phone(memberDTO.getPhone())
                .build();
        parkingService.addParking(parkingDTO);

        log.info("입차 정상 처리 - 대시보드로 이동");
        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }
}