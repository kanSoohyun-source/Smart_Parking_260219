package org.example.smart_parking_260219.dao;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.vo.ParkingVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Log4j2
public class ParkingDAOImpl implements ParkingDAO {
    private static ParkingDAO instance;

    public ParkingDAOImpl() {}

    public static ParkingDAO getInstance() {
        if (instance == null) {
            instance = new ParkingDAOImpl();
        }
        return instance;
    }

    // 입차 확인
    @Override
    public void insertParking(ParkingVO parkingVO) {
        String sql = "INSERT INTO parking_system.extry_exit_log (entry_exit_no, car_num, phone, space, entry_time, car_type) VALUES (?, ?, ?, ?, now(), ?)";
        try {
            @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, parkingVO.getEntryExitNo());
            preparedStatement.setString(2, parkingVO.getCarNum());
            preparedStatement.setString(3, parkingVO.getPhone());
            preparedStatement.setString(4, parkingVO.getSpace());
            preparedStatement.setInt(5, parkingVO.getCarType());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 주차 차량 조회
    @Override
    public ParkingVO selectParking(String last4) {
        String sql = "SELECT * FROM parking_system.extry_exit_log WHERE RIGHT(car_num, 4)=?";
        try {
            @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, last4);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ParkingVO parkingVO = ParkingVO.builder()
                        .entryExitNo(resultSet.getInt("entry_exit_no"))
                        .carNum(resultSet.getString("car_num"))
                        .phone(resultSet.getString("phone"))
                        .space(resultSet.getString("space"))
                        .entryTime((LocalDateTime) resultSet.getObject("entry_time"))
                        .exitTime((LocalDateTime) resultSet.getObject("exit_time"))
                        .totalTime(resultSet.getInt("total_time"))
                        .carType(resultSet.getInt("car_type"))
                        .isPaid(resultSet.getBoolean("is_paid"))
                        .build();
                log.info("parkingVO : {}", parkingVO);
                return parkingVO;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // 출차 확인
    @Override
    public void updateParking(ParkingVO parkingVO) {
        String sql = "UPDATE parking_system.extry_exit_log SET exit_time= now(), total_time= " +
                (Integer.parseInt(String.valueOf(parkingVO.getExitTime())) - Integer.parseInt(String.valueOf(parkingVO.getEntryTime())))
                + ", is_paid=true WHERE space=?";
        try {
            @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, parkingVO.getSpace());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
