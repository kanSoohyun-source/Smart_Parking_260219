package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.MemberDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class MemberServiceTest {

    private MemberService memberService;

    @BeforeEach
    public void ready() {
        memberService = MemberService.Instance;
    }

    @Test
    public void addTest() throws SQLException {
        MemberDTO memberDTO = MemberDTO.builder()
                .carNum("service")
                .carType(1)
                .name("service")
                .phone("service1234")
                .subscribed(true)
                .build();
        memberService.addMember(memberDTO);
    }

    @Test
    public void getAllTest() throws SQLException {
        var members = memberService.getAllMember();

        members.forEach(it -> log.info(it));
    }

    @Test
    public void getOneTest() throws SQLException{
        String carNum = "service";

        MemberDTO memberDTO = memberService.getOneMember(carNum);
        log.info(memberDTO);
        Assertions.assertNotNull(memberDTO);
    }

    @Test
    public void modify() throws SQLException {
        MemberDTO memberDTO = MemberDTO.builder()
                .carType(3)
                .name("update")
                .phone("update1234")
                .subscribed(false)
                .carNum("service")
                .build();
        memberService.modifyMember(memberDTO);

    }

    @Test
    public void remove() throws SQLException {
        String carNum = "service";

        memberService.removeMember(carNum);
    }



}