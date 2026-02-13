package org.example.smart_parking_260219.controller.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.MemberDTO;
import org.example.smart_parking_260219.service.MemberService;

import java.io.IOException;
import java.util.List;

@Log4j2
@WebServlet(name = "memberDetailController", value = "/member/member_detail")
public class MemberDetailController extends HttpServlet {
    private final MemberService memberService = MemberService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/member/detail get..");

        try {
            String carNum = req.getParameter("carNum");

            if (carNum == null || carNum.trim().isEmpty()) {
                resp.sendRedirect("/member/member_list?error=missing");
                return;
            }

            MemberDTO member = memberService.getOneMember(carNum.trim());

            if (member == null) {
                resp.sendRedirect("/member/member_list?error=notFound");
                return;
            }

            req.setAttribute("member", member);
            req.getRequestDispatcher("/WEB-INF/member/member_detail.jsp").forward(req, resp);

        } catch (Exception e) {
            log.error("회원 조회 중 오류 발생", e);
            resp.sendRedirect("/member/member_list?error=fail");
        }
    }
}
