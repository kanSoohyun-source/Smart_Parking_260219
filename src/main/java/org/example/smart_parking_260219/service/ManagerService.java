package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dao.ManagerDAO;
import org.example.smart_parking_260219.dto.ManagerDTO;
import org.example.smart_parking_260219.util.MapperUtil;
import org.example.smart_parking_260219.util.PasswordUtil;
import org.example.smart_parking_260219.vo.ManagerVO;
import org.modelmapper.ModelMapper;

import java.util.List;

@Log4j2
public enum ManagerService {
    INSTANCE;

    private final ManagerDAO managerDAO;
    private final ModelMapper modelMapper;

    ManagerService() {
        this.managerDAO = ManagerDAO.getInstance();
        this.modelMapper = MapperUtil.INSTANCE.getInstance();
    }

    /* 로그인 처리 */
    public boolean isAuth(String managerId, String password) {
        ManagerVO managerVO = managerDAO.selectOne(managerId);
        // 비밀번호 대조
        if (managerVO != null) {
            // BCrypt 검증
            boolean passwordMatch = PasswordUtil.checkPassword(password, managerVO.getPassword());
            if (passwordMatch) {
                log.info("비밀번호 검증 성공 - ID: {}", managerId);
                // 비밀번호가 맞더라도, 계정이 '활성화(active)' 상태일 때만 true를 반환하여 로그인을 허용
                return managerVO.isActive();
            } else {
                log.warn("비밀번호 불일치 - ID: {}", managerId);
            }
        }
        // 계정 없거나, 비밀번호 틀렸거나, 비활성화 상태 = false
        return false;
    }

    /* 특정 관리자 조회 */
    public ManagerDTO getManager(String managerId) {
        ManagerVO memberVo = managerDAO.selectOne(managerId);
        if (memberVo == null) return null;

        ManagerDTO managerDTO = modelMapper.map(memberVo, ManagerDTO.class);
        log.info("조회된 관리자 DTO: {}", managerDTO);
        return managerDTO;
    }

    /* 관리자 전체 목록 조회 */
    public List<ManagerDTO> getAllManagers() {
        log.info("getAllManagers... 호출");
        List<ManagerVO> voList = managerDAO.selectAll();

        return voList.stream()
                .map(vo -> modelMapper.map(vo, ManagerDTO.class))
                .toList();
    }

    /* 관리자 추가 */
    public void addManager(ManagerDTO managerDTO) {
        log.info("추가할 관리자 DTO: {}", managerDTO);
        ManagerVO memberVo = modelMapper.map(managerDTO, ManagerVO.class);
        managerDAO.insertManager(memberVo);
    }

    /* 계정 활성화/비활성화 상태 변경 */
    public void updateActiveStatus(String id, boolean active) {
        log.info("서비스: 상태 변경 시도 - ID: {}, Target: {}", id, active);
        managerDAO.updateActive(active, id);
    }
}