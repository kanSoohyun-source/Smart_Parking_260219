package org.example.smart_parking_260219.controller;

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
        int totalItems = allMembers.size();
        int totalPages = (totalItems > 0) ? (int) Math.ceil((double) totalItems / itemsPerPage) : 1;

        // 페이지 번호 유효성 검사
        if (currentPage < 1) currentPage = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        // 현재 페이지에 해당하는 데이터만 추출
        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        List<MemberDTO> pagedMembers = (startIndex < totalItems)
                ? allMembers.subList(startIndex, endIndex)
                : new ArrayList<>();

        // ✅ 시작 번호 계산 (역순)
        // 예: 총 25명, 1페이지 → startNo = 25
        //     총 25명, 2페이지 → startNo = 15
        //     총 25명, 3페이지 → startNo = 5
        int startNo = totalItems - (currentPage - 1) * itemsPerPage;

        // JSP에 데이터 전달
        req.setAttribute("dtoList", pagedMembers);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalItems", totalItems);
        req.setAttribute("startNo", startNo);

        log.info("총 회원 수: " + totalItems);
        log.info("현재 페이지: " + currentPage + " / " + totalPages);
        log.info("시작 인덱스: " + startIndex + ", 끝 인덱스: " + endIndex);
        log.info("시작 번호: " + startNo);

        req.getRequestDispatcher("/member/member_list.jsp").forward(req, resp);
    }
}

