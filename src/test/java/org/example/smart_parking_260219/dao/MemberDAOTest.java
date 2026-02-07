package org.example.smart_parking_260219.dao;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.vo.MemberVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@Log4j2
class MemberDAOTest {
    private final MemberDAO memberDAO = MemberDAO.getInstance();

    @Test
    public void insertTest() {
        try {
            MemberVO memberVO = MemberVO.builder()
                    .subscriptionId(222)
                    .carNum("1234")
                    .carType(1)
                    .name("test")
                    .phone("010-1111-2222")
                    .subscribed(false)
                    .build();
            memberDAO.insertMember(memberVO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void memberDummy() throws SQLException {
        for (int i = 0; i < 50; i++) {
            MemberVO memberVO = MemberVO.builder()
                    .subscriptionId(i)
                    .carNum("111" + i)
                    .carType(1)
                    .name("이름" + i)
                    .phone("111-2222-333" + i)
                    .subscribed(false)
                    .build();
            memberDAO.insertMember(memberVO);
        }
    }

    @Test
    public void selectAllTest() throws SQLException {
        var cars = memberDAO.selectAllMember();

        for (MemberVO memberVO : cars) {
            log.info(memberVO);
        }

    }

    @Test
    public void selectOneTest()throws SQLException {
        String carNum = "1110";

        MemberVO memberVO = memberDAO.selectOneMember(carNum);
        log.info(memberVO);

        Assertions.assertNotNull(memberVO);
    }

    @Test
    public void updateTest() throws SQLException {
        String carNum = "1110";

        MemberVO memberVO = MemberVO.builder()
                .carNum("1115")
                .carType(3)
                .name("update")
                .phone("111-111-111")
                .subscribed(true)
                .build();
        memberDAO.updateMember(memberVO);
        MemberVO memberVO1 = memberDAO.selectOneMember(carNum);
        if (memberVO.getCarNum().equals(memberVO1.getCarNum()) &&
                memberVO.getCarType() == (memberVO1.getCarType()) &&
                memberVO.getName().equals(memberVO1.getName()) &&
                memberVO.getPhone().equals(memberVO1.getPhone()) &&
                memberVO.isSubscribed() == memberVO1.isSubscribed()) {
            log.info("변경 선공");
        }

    }

    @Test
    public void deleteTest() throws SQLException {
        String carNum = "1110";
        memberDAO.deleteMember(carNum);
    }

}