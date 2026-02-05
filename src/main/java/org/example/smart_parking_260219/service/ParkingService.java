package org.example.smart_parking_260219.service;

import org.example.smart_parking_260219.dao.ParkingDAO;
import org.example.smart_parking_260219.dao.ParkingDAOImpl;
import org.example.smart_parking_260219.dto.ParkingDTO;
import org.example.smart_parking_260219.util.MapperUtil;
import org.example.smart_parking_260219.vo.ParkingVO;
import org.modelmapper.ModelMapper;

public enum ParkingService {
    INSTANCE;

    private final ParkingDAO parkingDAO;
    private final ModelMapper modelMapper;

    ParkingService() {
        parkingDAO = new ParkingDAOImpl();
        modelMapper = MapperUtil.INSTANCE.getInstance();
    }

    // 입차된 시간
    public void addParking(ParkingDTO parkingDTO) {
        ParkingVO parkingVO = modelMapper.map(parkingDTO, ParkingVO.class);
        parkingDAO.insertParking(parkingVO);
    }

    // 차번호 뒷자리 일치 차량 조회
    public ParkingVO getParking(String last4) {
        return modelMapper.map(parkingDAO.selectParking(last4), ParkingVO.class);
    }

    // 출차된 시간
    public void modifyParking(ParkingDTO parkingDTO) {
        ParkingVO parkingVO = modelMapper.map(parkingDTO, ParkingVO.class);
        parkingDAO.updateParking(parkingVO);
    }
}
