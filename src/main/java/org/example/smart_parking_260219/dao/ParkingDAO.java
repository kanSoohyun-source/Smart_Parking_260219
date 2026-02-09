package org.example.smart_parking_260219.dao;

import org.example.smart_parking_260219.vo.ParkingVO;

public interface ParkingDAO {
    void insertParking(ParkingVO parkingVO);                // 입차된 시간
    ParkingVO selectParkingByLast4(String last4);   // 차번호 뒷자리 차량 찾기
    ParkingVO selectParkingByCarNum(String carNum);   //
    void updateParking(ParkingVO parkingVO);               // 출차된 시간
    ParkingVO selectParkingByParkingId(int parkingId);      // 프라이머리키 기준 차량 조회
}
