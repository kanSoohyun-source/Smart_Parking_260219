package org.example.smart_parking_260219.dao;

import lombok.Cleanup;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.vo.ParkingVO;

import java.sql.*;
import java.time.Duration;
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
        String sql = "INSERT INTO parking_system.parking (member_id, car_num, space_id, entry_time, car_type) VALUES (?, ?, ?, now(), ?)";
        try {
            @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, parkingVO.getMemberId());
            preparedStatement.setString(2, parkingVO.getCarNum());
            preparedStatement.setString(3, parkingVO.getSpaceId());
            preparedStatement.setInt(4, parkingVO.getCarType());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 주차 차량 조회
    @Override
    public ParkingVO selectParkingByLast4(String last4) {
        String sql = "SELECT * FROM parking_system.parking WHERE RIGHT(car_num, 4) = ?";
        try {
            @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, last4);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Timestamp entryTime = resultSet.getTimestamp("entry_time");
                Timestamp exitTime = resultSet.getTimestamp("exit_time");
                ParkingVO parkingVO = ParkingVO.builder()
                        .parkingId(resultSet.getInt("parking_id"))
                        .memberId(resultSet.getInt("member_id"))
                        .carNum(resultSet.getString("car_num"))
                        .spaceId(resultSet.getString("space_id"))
                        .entryTime(entryTime != null ? entryTime.toLocalDateTime() : null)
                        .exitTime(exitTime != null ? exitTime.toLocalDateTime() : null)
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

    @Override
    public ParkingVO selectParkingByNum(String carNum) {
        String sql = "SELECT * FROM parking_system.parking WHERE car_num = ?";
        try {
            @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, carNum);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Timestamp entryTime = resultSet.getTimestamp("entry_time");
                Timestamp exitTime = resultSet.getTimestamp("exit_time");
                ParkingVO parkingVO = ParkingVO.builder()
                        .parkingId(resultSet.getInt("parking_id"))
                        .carNum(resultSet.getString("car_num"))
                        .memberId(resultSet.getInt("member_id"))
                        .spaceId(resultSet.getString("space_id"))
                        .entryTime(entryTime != null ? entryTime.toLocalDateTime() : null)
                        .exitTime(exitTime != null ? exitTime.toLocalDateTime() : null)
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
        LocalDateTime entry = selectParkingByNum(parkingVO.getCarNum()).getEntryTime();
        if (entry == null) {
            log.error("entry is null");
            return;
        }
        LocalDateTime exit = LocalDateTime.now();

        long totalMinutes = Duration.between(entry, exit).toMinutes();

        String sql = "UPDATE parking_system.parking SET exit_time= ?, total_time= ?, is_paid=true WHERE car_num=?";
        try {
            @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(exit));
            preparedStatement.setLong(2, totalMinutes);
            preparedStatement.setString(3, parkingVO.getCarNum());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
