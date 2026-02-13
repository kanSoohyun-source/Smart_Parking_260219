package org.example.smart_parking_260219.controller.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.MemberDTO;
import org.example.smart_parking_260219.service.MemberService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Log4j2
@WebServlet(name = "memberModifyController", value = "/member/member_modify")
public class MemberModifyController extends HttpServlet {
    private final MemberService memberService = MemberService.INSTANCE;

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("member_modify");
        try {
            String carNum = req.getParameter("carNum");

            if (carNum == null || carNum.trim().isEmpty()) {
                resp.sendRedirect("/member/member_list?error=missing");
                return;
            }

            MemberDTO member = memberService.getOneMember(carNum);

            if (member == null) {
                resp.sendRedirect("/member/member_list?error=notFound");
                return;
            }

            req.setAttribute("member", member);
            req.getRequestDispatcher("/WEB-INF/member/member_modify.jsp").forward(req, resp);

        } catch (Exception e) {
            log.error("회원 수정 페이지 조회 중 오류 발생", e);
            resp.sendRedirect("/member/member_list?error=fail");
        }
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/member/modify post");

        try {
            String carNum = req.getParameter("carNum");
            int memberId = Integer.parseInt(req.getParameter("memberId"));
            int carType = Integer.parseInt(req.getParameter("carType"));
            String name = req.getParameter("name");
            String phone = req.getParameter("phone");
            String subscribeAction = req.getParameter("subscribeAction");

            // 회원 정보 수정
            MemberDTO memberDTO = MemberDTO.builder()
                    .carNum(carNum)
                    .memberId(memberId)
                    .carType(carType)
                    .name(name)
                    .phone(phone)
                    .build();
            memberService.modifyMember(memberDTO);
            log.info("회원 정보 수정 완료: {}", carNum);

            // 월정액 처리
            log.info("월정액 액션: {}", subscribeAction);

            if ("add".equals(subscribeAction) || "extend".equals(subscribeAction)) {

                String startDateStr = req.getParameter("startDate");
                String endDateStr = req.getParameter("endDate");

                LocalDate startDate = LocalDate.parse(startDateStr);
                LocalDate endDate = LocalDate.parse(endDateStr);

                resp.sendRedirect("/member/member_list?success=modify");
            }

        } catch (Exception e) {
            log.error("회원 수정 중 오류 발생", e);
            String carNum = req.getParameter("carNum");
            resp.sendRedirect("/member/member_modify?carNum=" + carNum + "&error=modifyFail");
        }
    }
}
