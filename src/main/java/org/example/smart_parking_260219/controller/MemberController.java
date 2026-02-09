//package org.example.smart_parking_260219.controller;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import lombok.SneakyThrows;
//import lombok.extern.log4j.Log4j2;
//import org.example.smart_parking_260219.dto.MemberDTO;
//import org.example.smart_parking_260219.service.MemberService;
//
//import java.io.IOException;
//
//@Log4j2
//@WebServlet(name = "memberController"
//        , urlPatterns = {"/member/add_member.jsp", "/member/member_list.jsp", "/member/member_search.jsp"})
//public class MemberController extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
//    }
//
//    @SneakyThrows
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        MemberService memberService = MemberService.Instance;
//
//        String requestURI = req.getRequestURI(); // 요청 URI
//        String contextPath = req.getContextPath(); // 컨텍스트 경로
//        String command = requestURI.substring(contextPath.length()); // 요청 URI에서 컨텍스트 경로를 제거한 명령어
//
//        log.info("requestURI: {}", requestURI);
//        log.info("contextPath: {}", contextPath);
//        log.info("command: {}", command);// 파일 경로에서 이름을 불러오기 위한 명령어
//
//        HttpSession session = req.getSession();
//
//        switch (command) {
//
//            case "/member/addMemberProcess" -> {
//                log.info("회원 가입 처리");
//
////                private int memberId;
////                private int subscriptionId;
////                private String carNum;
////                private int carType;
////                private String name;
////                private String phone;
////                private boolean subscribed;
//
//                int memberId = Integer.parseInt(req.getParameter("memberId"));
//                int subscriptionId = Integer.parseInt(req.getParameter("subscriptionId"));
//                String carNum = req.getParameter("carNum");
//                int carType = Integer.parseInt(req.getParameter("carType"));
//                String name = req.getParameter("name");
//                String phone = req.getParameter("phone");
//                boolean subscribed = Boolean.parseBoolean(req.getParameter("subscribed"));
//
//                MemberDTO memberDTO = new MemberDTO();
//                memberDTO.setMemberId(memberId);
//                memberDTO.setSubscriptionId(subscriptionId);
//                memberDTO.setCarNum(carNum);
//                memberDTO.setCarType(carType);
//                memberDTO.setName(name);
//                memberDTO.setPhone(phone);
//                memberDTO.setSubscribed(subscribed);
//
//                memberService.addMember(memberDTO);
//
//                resp.sendRedirect("/member/add_member.jsp");
//            }
//
//        }
//
//    }
//}
