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

@WebServlet("/input")
@Log4j2
public class ParkingInputController extends HttpServlet {
    private final ParkingService parkingService = ParkingService.INSTANCE;
    private final ParkingSpotService parkingSpotService = ParkingSpotService.INSTANCE;
    private final MemberService memberService = MemberService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/entry/entry.jsp");
        String carNum = req.getParameter("carNum");

        req.setAttribute("carNum", carNum);

        req.getRequestDispatcher("/WEB-INF/view/entry/entry.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.info("/parking/input post...");

        // 차 번호 입력 (번호가 비어있거나 길이 초과 시 경고 창 출력)
        String carNum = req.getParameter("carNum");
        if (carNum.isEmpty() || carNum.length() > 8) {
            req.getRequestDispatcher("/WEB-INF/view/entry/entry.jsp?fail=over").forward(req, resp);
            return;
        }
        log.info(carNum);

        // 주차 구역 입력 (입력한 차 번호로 회원인지 비회원인지를 분간)
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
        log.info(parkingSpotService.getParkingSpotBySpaceId(spaceId).getEmpty());

        // 선택한 주차 구역이 빈 공간인지를 확인
        if (parkingSpotService.getParkingSpotBySpaceId(spaceId).getEmpty()) {
            req.getRequestDispatcher("/WEB-INF/view/entry/entry.jsp?fail=false").forward(req, resp);
            return;
        }

        // 차 번호가 비어 있거나 이전에 주차된 차량의 정산이 정상 처리 되었을때
        if (parkingService.getParkingByCarNum(carNum) == null || parkingService.getParkingByCarNum(carNum).isPaid()) {
            // 주차 공간 갱신
            ParkingSpotDTO parkingSpotDTO = ParkingSpotDTO.builder()
                    .carNum(carNum)
                    .spaceId(spaceId)
                    .build();
            log.info("parkingSpotDTO: {}", parkingSpotDTO);
            parkingSpotService.modifyInputParkingSpot(parkingSpotDTO);

            // 주차 기록 추가
            ParkingDTO parkingDTO = ParkingDTO.builder()
                    .memberId(memberDTO.getMemberId())
                    .carNum(carNum)
                    .spaceId(spaceId)
                    .carType(memberDTO.getCarType())
                    .phone(memberDTO.getPhone())
                    .build();
            log.info("parkingDTO: {}", parkingDTO);
            parkingService.addParking(parkingDTO);

            // 입차 정상 처리
            req.getRequestDispatcher("/WEB-INF/view/dashboard/dashboard.jsp").forward(req, resp);
        } else {
            // 해당 주차 공간의 주차가 불가능할 때
            req.getRequestDispatcher("/WEB-INF/view/entry/dashboard.jsp?fail=false").forward(req, resp);
        }
    }
}
