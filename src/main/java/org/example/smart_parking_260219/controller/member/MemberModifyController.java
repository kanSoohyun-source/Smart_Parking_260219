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
import java.time.LocalDateTime;

@Log4j2
@WebServlet(name = "memberModifyController", value = "/member/member_modify")
public class MemberModifyController extends HttpServlet {
    private final MemberService memberService = MemberService.INSTANCE;

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("member_modify");

        String carNum = req.getParameter("carNum");
        MemberDTO memberDTO = memberService.getOneMember(carNum);
        req.setAttribute("member", memberDTO);

        req.getRequestDispatcher("/member/member_modify.jsp");
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/member/modify post");

        String carNum = req.getParameter("carNum");
        int memberId = Integer.parseInt(req.getParameter("memberId"));
        int carType = Integer.parseInt(req.getParameter("carType"));
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        LocalDateTime createDate = LocalDateTime.parse(req.getParameter("createDate"));
        boolean subscribed = Boolean.parseBoolean(req.getParameter("subscribed"));

        MemberDTO memberDTO = MemberDTO.builder()
                .carNum(carNum)
                .memberId(memberId)
                .carType(carType)
                .name(name)
                .phone(phone)
                .createDate(createDate)
                .subscribed(subscribed)
                .build();
        memberService.modifyMember(memberDTO);

        resp.sendRedirect("/member/modify_member?carNum=" + carNum);

    }
}
