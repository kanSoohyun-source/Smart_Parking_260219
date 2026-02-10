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
import java.io.PrintWriter;

@Log4j2
@WebServlet(name = "memberAddController", value = "/member/add_member")
public class MemberAddController extends HttpServlet {
    private final MemberService memberService = MemberService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/member/add_member.jsp").forward(req, resp);
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/member/add..");

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        try {
            MemberDTO memberDTO = MemberDTO.builder()
                    .carNum(req.getParameter("carNum"))
                    .carType(Integer.parseInt(req.getParameter("carType")))
                    .name(req.getParameter("name"))
                    .phone(req.getParameter("phone"))
                    .subscribed(Boolean.parseBoolean(req.getParameter("subscribed")))
                    .build();

            log.info(memberDTO);
            memberService.addMember(memberDTO);

            //  성공 시 alert 후 회원 목록으로 이동
            out.println("<script>");
            out.println("alert('회원 가입이 완료되었습니다.');");
            out.println("location.href='/member/member_list';");
            out.println("</script>");

        } catch (Exception e) {
            log.error("회원 가입 중 오류 발생", e);

            // 실패 시 alert 후 등록 폼으로 돌아가기
            out.println("<script>");
            out.println("alert('회원 가입에 실패했습니다: " + e.getMessage() + "');");
            out.println("history.back();");
            out.println("</script>");
        } finally {
            out.close();
        }
    }
}

