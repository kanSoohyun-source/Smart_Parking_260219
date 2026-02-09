package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dao.ManagerDAO;
import org.example.smart_parking_260219.dto.ManagerDTO;
import org.example.smart_parking_260219.util.MapperUtil;
import org.example.smart_parking_260219.vo.ManagerVO;
import org.modelmapper.ModelMapper;

@Log4j2
public class ManagerService {

    private final ManagerDAO managerDAO = ManagerDAO.getInstance();
    private final ModelMapper modelMapper = MapperUtil.INSTANCE.getInstance();

    private static ManagerService instance;

    private ManagerService() {
    }

    public static ManagerService getInstance() {
        if (instance == null) {
            instance = new ManagerService();
        }
        return instance;
    }

    /* 관리자 인증 처리 (로그인) */
    public boolean isAuth(String managerId, String password) {
        boolean isAuth = false;

        ManagerVO managerVo = managerDAO.selectManager(managerId);
        // DB에 저장된 비밀번호와 입력한 비밀번호 비교
        if (managerVo != null && managerVo.getPassword().equals(password)) {
            isAuth = true;
        }
        return isAuth;
    }

    /* 관리자 상세 정보 가져오기 */
    public ManagerDTO getManager(String managerId) {
        ManagerVO managerVo = managerDAO.selectManager(managerId);
        if (managerVo == null) return null;

        ManagerDTO managerDto = modelMapper.map(managerVo, ManagerDTO.class);
        log.info("조회된 관리자 DTO: {}", managerDto);
        return managerDto;
    }

    /* 관리자 신규 등록 */
    public void addManager(ManagerDTO managerDto) {
        log.info("등록 시도 관리자: {}", managerDto);
        ManagerVO managerVo = modelMapper.map(managerDto, ManagerVO.class);
        managerDAO.insertManager(managerVo);
    }

    /* 관리자 정보 수정 */
    public void modifyManager(ManagerDTO managerDto) {
        log.info("수정 시도 관리자: {}", managerDto);
        ManagerVO managerVo = modelMapper.map(managerDto, ManagerVO.class);
        managerDAO.updateManager(managerVo);
    }
}
