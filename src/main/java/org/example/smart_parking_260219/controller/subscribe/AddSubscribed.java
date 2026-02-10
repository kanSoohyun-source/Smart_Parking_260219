package org.example.smart_parking_260219.controller.subscribe;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.HelloServlet;
import org.example.smart_parking_260219.dto.SubscribeDTO;
import org.example.smart_parking_260219.service.MemberService;
import org.example.smart_parking_260219.service.SubscribeService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Log4j2
@WebServlet(name = "addSubscribed", value = "/subscribe/add_subscribe")
public class AddSubscribed extends HelloServlet {
    private final SubscribeService subscribeService = SubscribeService.INSTANCE;

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/member/add_subscribe");

        // 파라미터 받기

        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");
        String carNum = req.getParameter("carNum");

        // String을 LocalDateTime으로 변환
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        SubscribeDTO subscribeDTO = SubscribeDTO.builder()
                .carNum(carNum)
                .startDate(startDate)
                .endDate(endDate)
                .status(true)
                .build();
        subscribeService.addSubscribe(subscribeDTO);

        resp.sendRedirect("/subscribe/add_subscribed.jsp?status=true");

    }


}
