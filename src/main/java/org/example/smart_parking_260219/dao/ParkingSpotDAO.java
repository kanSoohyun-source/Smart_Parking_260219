package org.example.smart_parking_260219.dao;

import org.example.smart_parking_260219.vo.ParkingSpotVO;

import java.util.List;

public interface ParkingSpotDAO {
    void insertParkingSpot(ParkingSpotVO parkingSpotVO);                // 주차 공간 추가
    List<ParkingSpotVO> selectAllParkingSpot();                     // 주차 공간 조회
    void updateInputParkingSpot(ParkingSpotVO parkingSpotVO);           // 주차 공간 입차 갱신
    void updateOutputParkingSpot(ParkingSpotVO parkingSpotVO);          // 주차 공간 출차 갱신
    List<ParkingSpotVO> selectEmptyParkingSpot();                   // 빈 주차 공간 찾기
}
