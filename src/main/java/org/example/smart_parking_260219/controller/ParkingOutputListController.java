package org.example.smart_parking_260219.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.ParkingSpotDTO;
import org.example.smart_parking_260219.service.ParkingService;
import org.example.smart_parking_260219.service.ParkingSpotService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Log4j2
@WebServlet("/list")
public class ParkingOutputListController extends HttpServlet {
    ParkingSpotService parkingSpotService = ParkingSpotService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("parking list...");
        String CarNum = req.getParameter("carNum");
        String sort = req.getParameter("sort");
        String order = req.getParameter("order");

        List<ParkingSpotDTO> dtoList = getParkingSpotDTOS(sort, order);

        req.setAttribute("dtoList", dtoList);
        req.setAttribute("sort", sort);
        req.setAttribute("order", order);

        req.setAttribute("carNum", CarNum);
        req.getRequestDispatcher("/WEB-INF/view/exit/exit_list.jsp").forward(req, resp);
    }

    private List<ParkingSpotDTO> getParkingSpotDTOS(String sort, String order) {
        List<ParkingSpotDTO> dtoList = new ArrayList<>(parkingSpotService.getAllParkingSpot());

        Comparator<ParkingSpotDTO> comparator;

        if ("time".equals(sort)) {
            comparator = Comparator.comparing(ParkingSpotDTO::getLastUpdate);
        } else {
            comparator= Comparator.comparing((ParkingSpotDTO dto) ->
                            dto.getSpaceId().replaceAll("\\d", ""))
                    .thenComparingInt(dto ->
                            Integer.parseInt(dto.getSpaceId().replaceAll("\\D", ""))
                    );
        }

        if ("desc".equals(order)) {
            dtoList.sort(comparator.reversed());
        } else {
            dtoList.sort(comparator);
        }
        return dtoList;
    }
}
