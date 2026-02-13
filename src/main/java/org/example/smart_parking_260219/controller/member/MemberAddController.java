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
import java.time.LocalDate;

@Log4j2
@WebServlet(name = "memberAddController", value = "/member/member_add")
public class MemberAddController extends HttpServlet {
    private final MemberService memberService = MemberService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("==== member_add doGet 진입 ====");
        req.getRequestDispatcher("/WEB-INF/member/member_add.jsp").forward(req, resp);
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/member/add..");

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String carNum = req.getParameter("carNum");
            int carType = Integer.parseInt(req.getParameter("carType"));
            String name = req.getParameter("name");
            String phone = req.getParameter("phone");
            boolean subscribed = Boolean.parseBoolean(req.getParameter("subscribed"));
            LocalDate startDate = LocalDate.parse(req.getParameter("startDate"));
            LocalDate endDate = LocalDate.parse(req.getParameter("endDate"));
            int subscribedFee = req.getIntHeader("subscribedFee");

            MemberDTO memberDTO = MemberDTO.builder()
                    .carNum(carNum)
                    .carType(carType)
                    .name(name)
                    .phone(phone)
                    .subscribed(true)   // 항상 true
                    .startDate(startDate)
                    .endDate(endDate)
                    .subscribedFee(subscribedFee)
                    .build();

            memberService.addMember(memberDTO);
            log.info("월정액 회원 등록 완료: {} ({} ~ {})", carNum, startDate, endDate);

            out.println("<script>");
            out.println("alert('월정액 회원 등록이 완료되었습니다.');");
            out.println("location.href='/member/member_list';");
            out.println("</script>");

        } catch (Exception e) {
            log.error("회원 등록 중 오류 발생", e);
            out.println("<script>");
            out.println("alert('등록에 실패했습니다: " + e.getMessage() + "');");
            out.println("history.back();");
            out.println("</script>");
        } finally {
            out.close();
        }
    }
}

