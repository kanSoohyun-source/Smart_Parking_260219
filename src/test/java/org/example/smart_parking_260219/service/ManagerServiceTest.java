package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.ManagerDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class ManagerServiceTest {

    private ManagerService managerService;

    @BeforeEach
    public void ready() {
        managerService = ManagerService.getInstance();
    }

    @Test
    /* 로그인 성공 테스트 */
    public void testIsAuthSuccess() {
        String id = "test01";
        String pw = "1234";

        boolean result = managerService.isAuth(id, pw);

        log.info("인증 결과: {}", result);
        Assertions.assertTrue(result); // 결과가 true여야 성공
    }

    @Test
    /* 로그인 실패 테스트 */
    public void testIsAuthFail() {
        String id = "test01";
        String pw = "111111";

        boolean result = managerService.isAuth(id, pw);

        log.info("인증 결과(실패예상): {}", result);
        Assertions.assertFalse(result); // 결과가 false여야 성공
    }

    @Test
    public void testAddManager() {
        ManagerDTO newManager = ManagerDTO.builder()
                .managerId("service_01")
                .managerName("서비스테스터")
                .password("9999")
                .email("service@test.com")
                .active(true)
                .build();

        // 등록 실행
        managerService.addManager(newManager);

        // 검증을 위해 다시 조회
        ManagerDTO result = managerService.getManager("service_test_01");
        Assertions.assertNotNull(result);
        Assertions.assertEquals("서비스테스터", result.getManagerName());
    }

}