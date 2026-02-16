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
import java.util.ArrayList;
import java.util.List;

@Log4j2
@WebServlet(name = "memberController", value = "/member/member_list")
public class MemberListController extends HttpServlet {
    private final MemberService memberService = MemberService.INSTANCE;

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("=== member list 시작 ===");

        // ✅ 만료된 회원 자동 처리
        memberService.expireSubscriptions();

        String pageParam = req.getParameter("page");
        int currentPage = 1;
        if (pageParam != null) {
            try { currentPage = Integer.parseInt(pageParam); }
            catch (NumberFormatException e) { currentPage = 1; }
        }

        int itemsPerPage = 10;
        List<MemberDTO> allMembers = memberService.getAllMember();

        int totalItems = allMembers.size();
        int totalPages = (totalItems > 0) ? (int) Math.ceil((double) totalItems / itemsPerPage) : 1;

        if (currentPage < 1) currentPage = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        List<MemberDTO> pagedMembers = (startIndex < totalItems)
                ? allMembers.subList(startIndex, endIndex)
                : new ArrayList<>();

        int startNo = totalItems - (currentPage - 1) * itemsPerPage;

        req.setAttribute("dtoList", pagedMembers);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalItems", totalItems);
        req.setAttribute("startNo", startNo);

        req.getRequestDispatcher("/WEB-INF/member/member_list.jsp").forward(req, resp);
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("=== member list POST - 갱신 처리 ===");

        String action = req.getParameter("action");
        String carNum = req.getParameter("carNum");

        try {
            if ("renew".equals(action)) {
                // ✅ 1개월 갱신
                memberService.renewSubscription(carNum);
                log.info("갱신 완료: {}", carNum);
                resp.sendRedirect("/member/member_list?success=renew");
            } else {
                resp.sendRedirect("/member/member_list");
            }
        } catch (Exception e) {
            log.error("갱신 처리 오류", e);
            resp.sendRedirect("/member/member_list?error=renewFail");
        }
    }
}

