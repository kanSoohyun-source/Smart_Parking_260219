package org.example.smart_parking_260219.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.MemberDTO;
import org.example.smart_parking_260219.service.MemberService;

import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
@WebServlet(name = "memberSearchController", value = "/member/member_search")
public class MemberSearchController extends HttpServlet {
    private final MemberService memberService = MemberService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String carNum = req.getParameter("carNum");
            log.info("회원 조회 - 입력된 번호: " + carNum);

            MemberDTO member;

            // 4자리인 경우 뒤 4자리로 조회
            if (carNum != null && carNum.length() == 4) {
                member = memberService.getOneMember(carNum);
            } else {
                // 전체 차량번호로 조회
                member = memberService.getOneMember(carNum);
            }

            if (member == null) {
                out.println("<script>");
                out.println("alert('해당 차량번호를 가진 회원을 찾을 수 없습니다.');");
                out.println("history.back();");
                out.println("</script>");
                return;
            }

            req.setAttribute("member", member);
            req.getRequestDispatcher("/member/member_search.jsp").forward(req, resp);

        } catch (Exception e) {
            log.error("회원 상세 조회 중 오류 발생", e);

            out.println("<script>");
            out.println("alert('회원 조회 중 오류가 발생했습니다: " + e.getMessage() + "');");
            out.println("history.back();");
            out.println("</script>");
        } finally {
            out.close();
        }
    }
}
