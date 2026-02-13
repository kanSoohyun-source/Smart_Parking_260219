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
            log.info("회원 조회 - 입력된 번호: {}", carNum);

            // 파라미터 검증
            if (carNum == null || carNum.trim().isEmpty()) {
                log.error("차량번호 파라미터 누락");
                resp.sendRedirect("/member/member_search.jsp?error=missing");
                return;
            }

            carNum = carNum.trim();

            // 뒤 4자리만 입력한 경우
            if (carNum.length() == 4) {
                log.info("뒤 4자리로 조회: {}", carNum);

                // 뒤 4자리로 조회 (여러 건 가능)
//                List<MemberDTO> matchedMembers = memberService.getCarNum(carNum);

//                if (matchedMembers == null || matchedMembers.isEmpty()) {
//                    log.warn("조회 결과 없음");
//                    resp.sendRedirect("/member/member_search.jsp?error=notFound");
//                    return;
//                }

                // 1건이면 바로 상세 페이지로
//                if (matchedMembers.size() == 1) {
//                    log.info("조회 결과 1건 - 상세 페이지로 이동");
//                    MemberDTO member = matchedMembers.get(0);
//
//                    req.setAttribute("member", member);
//                    req.getRequestDispatcher("/WEB-INF/member/member_detail.jsp").forward(req, resp);
//                    return;
//                }

                // 여러 건이면 선택 페이지로
//                log.info("조회 결과 {}건 - 선택 페이지로 이동", matchedMembers.size());
//                req.setAttribute("matchedMembers", matchedMembers);
                req.getRequestDispatcher("/WEB-INF/member/member_select.jsp").forward(req, resp);
                return;
            }

            // 전체 차량번호를 입력한 경우
            log.info("전체 차량번호로 조회: {}", carNum);

            MemberDTO member = memberService.getOneMember(carNum);

            if (member == null) {
                log.warn("조회 결과 없음");
                resp.sendRedirect("/member/member_search.jsp?error=notFound");
                return;
            }

            log.info("조회 결과 1건 - 상세 페이지로 이동");
            req.setAttribute("member", member);
            req.getRequestDispatcher("/WEB-INF/member/member_detail.jsp").forward(req, resp);

        } catch (Exception e) {
            log.error("회원 조회 중 오류 발생", e);
            resp.sendRedirect("/member/member_search.jsp?error=fail");
        }
    }
}
