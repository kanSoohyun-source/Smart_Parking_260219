package org.example.smart_parking_260219.controller.subscribe;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.HelloServlet;
import org.example.smart_parking_260219.dto.MemberDTO;
import org.example.smart_parking_260219.dto.SubscribeDTO;
import org.example.smart_parking_260219.service.MemberService;
import org.example.smart_parking_260219.service.SubscribeService;

import java.io.IOException;
import java.time.LocalDate;

@Log4j2
@WebServlet(name = "addSubscribed", value = "/subscribe/subscribe_add")
public class SubscribeAdd extends HttpServlet {
    private final SubscribeService subscribeService = SubscribeService.INSTANCE;
    private final MemberService memberService = MemberService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String error = req.getParameter("error");
        String success = req.getParameter("success");
        req.setAttribute("error", error);
        req.setAttribute("success", success);
        req.getRequestDispatcher("/WEB-INF/subscribe/subscribe_add.jsp").forward(req, resp);
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/member/subscribe_add");
        String carNum = req.getParameter("carNum");
        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");

        // ✅ 파라미터 검증
        if (carNum == null || carNum.trim().isEmpty()) {
            resp.sendRedirect("/subscribe/subscribe_add?error=missing");
            return;
        }

        // ✅ 차량번호 존재 여부 검증
        MemberDTO member = memberService.getOneMember(carNum.trim());
        if (member == null) {
            log.warn("등록되지 않은 차량번호: {}", carNum);
            resp.sendRedirect("/subscribe/subscribe_add?error=notMember");
            return;
        }

        // ✅ 이미 구독 중인지 확인
        SubscribeDTO existSubscribe = subscribeService.getOneSubscribe(carNum.trim());
        if (existSubscribe != null && existSubscribe.isStatus()) {
            log.warn("이미 구독 중인 차량번호: {}", carNum);
            resp.sendRedirect("/subscribe/subscribe_add?error=alreadySubscribed");
            return;
        }

        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            SubscribeDTO subscribeDTO = SubscribeDTO.builder()
                    .carNum(carNum.trim())
                    .startDate(startDate)
                    .endDate(endDate)
                    .status(true)
                    .build();

            subscribeService.addSubscribe(subscribeDTO);
            log.info("월정액 가입 완료: {}", carNum);

            resp.sendRedirect("/subscribe/subscribe_list");

        } catch (Exception e) {
            log.error("월정액 가입 중 오류 발생", e);
            resp.sendRedirect("/subscribe/subscribe_add?error=fail");
        }
    }


}
