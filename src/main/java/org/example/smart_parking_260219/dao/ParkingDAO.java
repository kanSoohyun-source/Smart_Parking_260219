package org.example.smart_parking_260219.dao;

import org.example.smart_parking_260219.vo.ParkingVO;

import java.util.List;

public interface ParkingDAO {
    void insertParking(ParkingVO parkingVO);                // 입차된 시간
    ParkingVO selectParkingByLast4(String last4);           // 차번호 뒷자리 차량 찾기
    ParkingVO selectParkingByCarNum(String carNum);         // 차번호 전체 일치 차량 조회
    void updateParking(ParkingVO parkingVO);                // 출차된 시간
    ParkingVO selectParkingByParkingId(int parkingId);      // 프라이머리키 기준 차량 조회
    List<ParkingVO> selectAllParking();                     // 주차 구역 목록 (대시보드 주차 구역 출력에 필요)
    ParkingVO selectLastParkingByCarNum(String carNum);     // 최근 입차 데이터 출력 (대시보드 입차 상황 출력에 필요)
    ParkingVO selectALLParkingByCarNum(String carNum);         // 차번호 전체 일치 차량 조회

}
