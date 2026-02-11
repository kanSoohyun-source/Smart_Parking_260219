package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dao.ManagerDAO;
import org.example.smart_parking_260219.dto.ManagerDTO;
import org.example.smart_parking_260219.util.MapperUtil;
import org.example.smart_parking_260219.vo.ManagerVO;
import org.modelmapper.ModelMapper;

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

    public void addManager(ManagerDTO managerDTO) {
        log.info("추가할 관리자 DTO: {}", managerDTO);
        ManagerVO memberVo = modelMapper.map(managerDTO, ManagerVO.class);
        managerDAO.insertManager(memberVo);
    }

    /*
    // 비번 수정
    public void modifyManager(ManagerDTO managerDTO) {
        // 1. 먼저 DB에서 기존 정보를 VO로 가져옴 (수정 안 할 필드 유지를 위해)
        ManagerVO existingVO = managerDAO.selectOne(managerDTO.getManagerId());

        // 2. DTO에 새 비밀번호가 입력되어 있다면 암호화해서 교체
        if (managerDTO.getPassword() != null && !managerDTO.getPassword().trim().isEmpty()) {
            String encodedPw = managerDAO.passEncode(managerDTO.getPassword());
            existingVO.setPassword(encodedPw);
        }

        // 3. 이름, 이메일 등 다른 정보는 DTO에서 VO로 복사
        existingVO.setManagerName(managerDTO.getManagerName());
        existingVO.setEmail(managerDTO.getEmail());

        // 4. DAO에게 VO 전달 (DB 작업은 여기서만 VO 사용)
        managerDAO.updateManager(existingVO);
    }
     */
}
