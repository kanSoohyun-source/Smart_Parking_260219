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
            log.error("수정 페이지 오류", e);
            resp.sendRedirect("/member/member_list?error=fail");
        }
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/member/modify post");

        String carNum = req.getParameter("carNum");

        try {
            String memberIdStr = req.getParameter("memberId");
            int memberId = (memberIdStr != null && !memberIdStr.isEmpty())
                    ? Integer.parseInt(memberIdStr) : 0;

            String name = req.getParameter("name");
            String phone = req.getParameter("phone");
            LocalDate startDate = LocalDate.parse(req.getParameter("startDate"));
            LocalDate endDate = LocalDate.parse(req.getParameter("endDate"));

            MemberDTO memberDTO = MemberDTO.builder()
                    .carNum(carNum)
                    .memberId(memberId)
                    .carType(2)
                    .name(name)
                    .phone(phone)
                    .subscribed(true)
                    .startDate(startDate)
                    .endDate(endDate)
                    .subscribedFee(100000) // 기본값 10만원
                    .build();

            memberService.modifyMember(memberDTO);
            log.info("회원 수정 완료: {} ({} ~ {})", carNum, startDate, endDate);


            resp.sendRedirect("/member/member_detail?carNum=" + carNum + "&success=modify");

        } catch (Exception e) {
            log.error("회원 수정 중 오류", e);
            resp.sendRedirect("/member/member_detail?carNum=" + carNum + "&error=modifyFail");
        }
    }
}
