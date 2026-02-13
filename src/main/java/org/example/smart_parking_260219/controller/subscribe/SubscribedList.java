package org.example.smart_parking_260219.controller.subscribe;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.MemberDTO;
import org.example.smart_parking_260219.dto.SubscribeDTO;
import org.example.smart_parking_260219.service.MemberService;
import org.example.smart_parking_260219.service.SubscribeService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@WebServlet(name = "ListSubscribed", value = "/subscribe/subscribe_list")
public class SubscribedList extends HttpServlet {
    private final SubscribeService subscribeService = SubscribeService.INSTANCE;
    private final MemberService memberService = MemberService.INSTANCE;

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("=== subscribed list 시작 ===");

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
        List<SubscribeDTO> subscriber = subscribeService.getAllSubscribe();
        int totalItems = subscriber.size();
        int totalPages = (totalItems > 0) ? (int) Math.ceil((double) totalItems / itemsPerPage) : 1;
        // 차량 번호로 회원 이름을 가져오기 위한 컨트롤러
        Map<String, String> members = memberService.getAllMember().stream()
                .collect(Collectors.toMap(MemberDTO::getCarNum, MemberDTO::getName));

        /* 페이징 처리 */
        // 페이지 번호 유효성 검사
        if (currentPage < 1) currentPage = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        // 현재 페이지에 해당하는 데이터만 추출
        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        List<SubscribeDTO> paged = (startIndex < totalItems)
                ? subscriber.subList(startIndex, endIndex)
                : new ArrayList<>();

        // ✅ 시작 번호 계산 (역순)
        // 예: 총 25명, 1페이지 → startNo = 25
        //     총 25명, 2페이지 → startNo = 15
        //     총 25명, 3페이지 → startNo = 5
        int startNo = totalItems - (currentPage - 1) * itemsPerPage;

        // JSP에 데이터 전달
        req.setAttribute("members", members);
        req.setAttribute("dtoList", paged);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalItems", totalItems);
        req.setAttribute("startNo", startNo);

        log.info("회원 목록" + members);
        log.info("총 회원 수: " + totalItems);
        log.info("현재 페이지: " + currentPage + " / " + totalPages);
        log.info("시작 인덱스: " + startIndex + ", 끝 인덱스: " + endIndex);
        log.info("시작 번호: " + startNo);

        req.getRequestDispatcher("/WEB-INF/subscribe/subscribe_list.jsp").forward(req, resp);
    }
}
