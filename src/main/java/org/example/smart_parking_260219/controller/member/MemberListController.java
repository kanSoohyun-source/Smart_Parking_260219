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

        /* 페이징 처리 */
        // 페이지 번호 가져오기 (기본값: 1)
        String pageParam = req.getParameter("page");
        int currentPage = 1;
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        // 페이지당 항목 수
        int itemsPerPage = 10;

        // 전체 회원 목록 조회
        List<MemberDTO> allMembers = memberService.getAllMember();
        List<MemberDTO> subscribedMembers = new ArrayList<>();
        for (MemberDTO m : allMembers) {
            if (m.isSubscribed()) subscribedMembers.add(m);
        }

        int totalItems = subscribedMembers.size();
        int totalPages = (totalItems > 0) ? (int) Math.ceil((double) totalItems / itemsPerPage) : 1;

        if (currentPage < 1) currentPage = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        List<MemberDTO> pagedMembers = (startIndex < totalItems)
                ? subscribedMembers.subList(startIndex, endIndex)
                : new ArrayList<>();

        int startNo = totalItems - (currentPage - 1) * itemsPerPage;

        req.setAttribute("dtoList", pagedMembers);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalItems", totalItems);
        req.setAttribute("startNo", startNo);

        req.getRequestDispatcher("/WEB-INF/member/member_list.jsp").forward(req, resp);
    }
}

