package org.example.smart_parking_260219.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.MemberDTO;
import org.example.smart_parking_260219.dto.SubscribeDTO;
import org.example.smart_parking_260219.service.MemberService;
import org.example.smart_parking_260219.service.SubscribeService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@WebServlet(name = "memberDetailController", value = "/member/member_detail")
public class MemberDetailController extends HttpServlet {
    private final MemberService memberService = MemberService.INSTANCE;
    private final SubscribeService subscribeService = SubscribeService.INSTANCE; // SubscribeService 추가

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String carNum = req.getParameter("carNum");
            log.info("회원 상세 조회 차량번호: " + carNum);

            MemberDTO member = null;
            List<MemberDTO> matchedMembers = new ArrayList<>();

            // 4자리인 경우 뒤 4자리로 조회
            if (carNum != null && carNum.length() == 4 && carNum.matches("\\d{4}")) {
                log.info("뒤 4자리로 조회: " + carNum);
                List<MemberDTO> allMembers = memberService.getAllMember();

                for (MemberDTO m : allMembers) {
                    if (m.getCarNum() != null && m.getCarNum().endsWith(carNum)) {
                        matchedMembers.add(m);
                        log.info("매칭된 차량번호: " + m.getCarNum());
                    }
                }

                // 중복 체크
                if (matchedMembers.isEmpty()) {
                    out.println("<script>");
                    out.println("alert('해당 차량번호를 가진 회원을 찾을 수 없습니다.');");
                    out.println("history.back();");
                    out.println("</script>");
                    return;
                } else if (matchedMembers.size() > 1) {
                    // 여러 명인 경우 선택 페이지로 이동
                    req.setAttribute("matchedMembers", matchedMembers);
                    req.getRequestDispatcher("/member/member_select.jsp").forward(req, resp);
                    return;
                } else {
                    // 1명인 경우
                    member = matchedMembers.get(0);
                }
            } else {
                // 전체 차량번호로 조회
                log.info("전체 차량번호로 조회: " + carNum);
                member = memberService.getOneMember(carNum);
            }

            if (member == null) {
                out.println("<script>");
                out.println("alert('해당 차량번호를 가진 회원을 찾을 수 없습니다.');");
                out.println("history.back();");
                out.println("</script>");
                return;
            }

            // 구독 정보 조회 (memberId로 조회)
            SubscribeDTO subscribeDTO = null;
            try {
                subscribeDTO = subscribeService.getOneSubscribe(member.getMemberId());
            } catch (Exception e) {
                log.warn("구독 정보 조회 실패 (구독하지 않은 회원일 수 있음): " + e.getMessage());
            }

            req.setAttribute("member", member);
            req.setAttribute("subscribe", subscribeDTO);

            req.getRequestDispatcher("/member/member_detail.jsp").forward(req, resp);

        } catch (Exception e) {
            log.error("회원 상세 조회 중 오류 발생", e);

            out.println("<script>");
            out.println("alert('회원 조회 중 오류가 발생했습니다.');");
            out.println("location.href='/member/member_list';");
            out.println("</script>");
        } finally {
            out.close();
        }
    }
}
