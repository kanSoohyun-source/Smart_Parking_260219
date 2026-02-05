package org.example.smart_parking_260219.dao;

import org.example.smart_parking_260219.vo.ParkingVO;

public interface ParkingDAO {
    void insertParking(ParkingVO parkingVO);                // 입차된 시간
    ParkingVO selectParking(String last4);   // 차번호 뒷자리 차량 찾기
    void updateParking(ParkingVO parkingVO);               // 출차된 시간
}
