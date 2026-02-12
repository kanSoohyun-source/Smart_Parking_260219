package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dao.ManagerDAO;
import org.example.smart_parking_260219.dto.ManagerDTO;
import org.example.smart_parking_260219.util.MapperUtil;
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

        if (managerVO != null && managerVO.getPassword().equals(password)) {
            return managerVO.isActive(); // 활성화 상태일 때만 true 반환
        }
        return false;
    }

    public ManagerDTO getManager(String managerId) {
        ManagerVO memberVo = managerDAO.selectOne(managerId);
        if (memberVo == null) return null;

        ManagerDTO managerDTO = modelMapper.map(memberVo, ManagerDTO.class);
        log.info("조회된 관리자 DTO: {}", managerDTO);
        return managerDTO;
    }

    /* 관리자 전체 목록 조회 (DTO 변환 포함) */
    public List<ManagerDTO> getAllManagers() {
        log.info("getAllManagers... 호출");
        List<ManagerVO> voList = managerDAO.selectAll();

        return voList.stream()
                .map(vo -> modelMapper.map(vo, ManagerDTO.class))
                .toList();
    }

    public void addManager(ManagerDTO managerDTO) {
        log.info("추가할 관리자 DTO: {}", managerDTO);
        ManagerVO memberVo = modelMapper.map(managerDTO, ManagerVO.class);
        managerDAO.insertManager(memberVo);
    }


    public void updateActiveStatus(String id, boolean active) {
        log.info("서비스: 상태 변경 시도 - ID: {}, Target: {}", id, active);
        // DAO 객체를 통해 DB 업데이트 실행
        managerDAO.updateActive(active, id);
    }


}
