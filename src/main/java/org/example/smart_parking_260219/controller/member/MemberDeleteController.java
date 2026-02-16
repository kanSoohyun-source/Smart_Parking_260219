package org.example.smart_parking_260219.controller.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.service.MemberService;

import java.io.IOException;

@Log4j2
@WebServlet(name = "memberDeleteController", value = "/member/member_delete")
public class MemberDeleteController extends HttpServlet {
    private final MemberService memberService = MemberService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("member_delete GET");

        String carNum = req.getParameter("carNum");

        if (carNum == null || carNum.trim().isEmpty()) {
            resp.sendRedirect("/member/member_list?error=missing");
            return;
        }

        try {
            memberService.removeMember(carNum.trim());
            log.info("회원 삭제 완료: {}", carNum);
            resp.sendRedirect("/member/member_list?success=delete");

        } catch (Exception e) {
            log.error("회원 삭제 오류", e);
            resp.sendRedirect("/member/member_list?error=deleteFail");
        }
    }
}
