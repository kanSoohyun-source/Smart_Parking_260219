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
                .subscriptionId(22)
                .carNum("service")
                .carType(1)
                .name("service")
                .phone("phoneService")
                .subscribed(true)
                .build();
        memberService.addMember(memberDTO);
    }

    @Test
    public void getAllTest() throws SQLException {
        var members = memberService.getAllMember();

//        for (MemberDTO memberDTO : members) {
//            log.info(memberDTO);
//        }

        members.forEach(it -> log.info(it));
    }

    @Test
    public void getOneTest() throws SQLException{
        String carNum = "11110";

        MemberDTO memberDTO = memberService.getOneMember(carNum);
        log.info(memberDTO);
        Assertions.assertNotNull(memberDTO);
    }



}