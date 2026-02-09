package org.example.smart_parking_260219.dao;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.vo.ManagerVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class ManagerDAOTest {
    private ManagerDAO managerDAO;

    @BeforeEach
    public void ready() {
        managerDAO = ManagerDAO.getInstance();
    }

    @Test
    public void testInsert() {
        ManagerVO vo = ManagerVO.builder()
                .managerId("test02")
                .managerName("테스터")
                .password("5678")
                .email("test02@naver.com")
                .active(false)
                .build();

        managerDAO.insertManager(vo);
        log.info("등록 완료");
    }

    @Test
    public void testSelect() {
        String managerId = "test01";
        ManagerVO vo = managerDAO.selectManager(managerId);

        log.info("조회 결과: {}", vo);

        // 결과가 null이 아님을 검증
        Assertions.assertNotNull(vo);
        // 조회한 ID가 일치하는지 검증
        Assertions.assertEquals(managerId, vo.getManagerId());
    }

    @Test
    public void testUpdate() {
        String managerId = "test02";
        ManagerVO vo = managerDAO.selectManager(managerId);

        // 이름 변경
        if (vo != null) {
            ManagerVO updateVO = ManagerVO.builder()
                    .managerId(vo.getManagerId())
                    .managerName("수정된테스터")
                    .password(vo.getPassword())
                    .email("update@park.com")
                    .active(false)
                    .build();

            managerDAO.updateManager(updateVO);

            ManagerVO result = managerDAO.selectManager(managerId);
            log.info("수정 후 결과: {}", result);
            Assertions.assertEquals("수정된테스터", result.getManagerName());
        }
    }
}