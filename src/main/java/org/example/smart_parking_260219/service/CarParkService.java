package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dao.CarParkDAO;
import org.example.smart_parking_260219.dao.CarParkDAOImpl;
import org.example.smart_parking_260219.dto.CarParkDTO;
import org.example.smart_parking_260219.util.MapperUtil;
import org.example.smart_parking_260219.vo.CarParkVO;
import org.modelmapper.ModelMapper;

import java.util.List;

@Log4j2
public enum CarParkService {
    INSTANCE;

    private final CarParkDAO carParkDAO;
    private final ModelMapper modelMapper;

    CarParkService() {
        carParkDAO = new CarParkDAOImpl();
        modelMapper = MapperUtil.INSTANCE.getInstance();
    }

    // 주차장 목록
    public List<CarParkDTO> getAllCarPark() {
        List<CarParkVO> carParkVOList = carParkDAO.selectAllCarPark();

        List<CarParkDTO> carParkDTOList = carParkVOList.stream()
                .map(carParkVO -> modelMapper.map(carParkVO, CarParkDTO.class)).toList();
        return carParkDTOList;
    }

    // 주차 공간 입차 갱신
    public void modifyInputCarPark(CarParkDTO carParkDTO) {
        CarParkVO carParkVO = modelMapper.map(carParkDTO, CarParkVO.class);
        carParkDAO.updateInputCarPark(carParkVO);
    }

    // 주차 공간 출차 갱신
    public void modifyOutputCarPark(CarParkDTO carParkDTO) {
        CarParkVO carParkVO = modelMapper.map(carParkDTO, CarParkVO.class);
        carParkDAO.updateOutputCarPark(carParkVO);
    }

    // 빈 주차 공간 찾기
    public List<CarParkDTO> getEmptyCarPark() {
        List<CarParkVO> carParkVOList = carParkDAO.selectEmptyCarPark();

        List<CarParkDTO> carParkDTOList = carParkVOList.stream()
                .map(carParkVO -> modelMapper.map(carParkVO, CarParkDTO.class)).toList();
        return carParkDTOList;
    }
}
