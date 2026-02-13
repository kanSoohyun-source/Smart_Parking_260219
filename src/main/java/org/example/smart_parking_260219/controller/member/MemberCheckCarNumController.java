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
import java.io.PrintWriter;

// 회원 가입시 차량 번호 중복성 검사 AJAX 컨트롤러
@Log4j2
@WebServlet(name = "memberCheckCarNumController", value = "/member/check_carnum")
public class MemberCheckCarNumController extends HttpServlet {
    private final MemberService memberService = MemberService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String carNum = req.getParameter("carNum");
            MemberDTO member = memberService.getOneMember(carNum);

            // ✅ JSON 응답
            if (member != null) {
                out.println("{\"exists\": true}");   // 이미 존재
            } else {
                out.println("{\"exists\": false}");  // 사용 가능
            }
        } catch (Exception e) {
            log.error("차량번호 중복 확인 중 오류", e);
            out.println("{\"exists\": false}");
        } finally {
            out.close();
        }
    }
}
