package org.example.smart_parking_260219.dao;

import org.example.smart_parking_260219.vo.CarParkVO;

import java.util.List;

public interface CarParkDAO {
    void insertCarPark(CarParkVO carParkVO);                // 주차 공간 추가
    List<CarParkVO> selectAllCarPark();                     // 주차 공간 조회
    void updateInputCarPark(CarParkVO carParkVO);           // 주차 공간 입차 갱신
    void updateOutputCarPark(CarParkVO carParkVO);          // 주차 공간 출차 갱신
    List<CarParkVO> selectEmptyCarPark();                   // 빈 주차 공간 찾기
}
