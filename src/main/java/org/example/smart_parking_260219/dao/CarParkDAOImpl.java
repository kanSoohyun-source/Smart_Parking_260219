package org.example.smart_parking_260219.dao;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.vo.CarParkVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class CarParkDAOImpl implements CarParkDAO {
    private static CarParkDAO instance;

    public CarParkDAOImpl() {}

    public static CarParkDAO getInstance() {
        if (instance == null) {
            instance = new CarParkDAOImpl();
        }
        return instance;
    }

    @Override
    public void insertCarPark(CarParkVO carParkVO) {
        String sql = "INSERT INTO parking_system.parking_spot (space, state, last_update) VALUES (?, false,now())";
        try {
            @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, carParkVO.getSpace());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 주차장 조회
    @Override
    public List<CarParkVO> selectAllCarPark() {
        String sql = "SELECT * FROM parking_system.parking_spot";
        List<CarParkVO> carParkVOList = new ArrayList<>();
        try {
            @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CarParkVO carParkVO = CarParkVO.builder()
                        .space(resultSet.getString("space"))
                        .state(resultSet.getBoolean("state"))
                        .carNum(resultSet.getString("car_num"))
                        .lastUpdate(resultSet.getTimestamp("last_update").toLocalDateTime())
                        .build();
                log.info("CarParkVo: {}", carParkVO);
                carParkVOList.add(carParkVO);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return carParkVOList;
    }

    // 주차 공간 입차 갱신
    @Override
    public void updateInputCarPark(CarParkVO carParkVO) {
        String sql = "UPDATE parking_system.parking_spot SET state = true, car_num =?, last_update = now() WHERE space = ?";
        try {
            @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, carParkVO.getCarNum());
            preparedStatement.setString(2, carParkVO.getSpace());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 주차 공간 출차 갱신
    @Override
    public void updateOutputCarPark(CarParkVO carParkVO) {
        String sql = "UPDATE parking_system.parking_spot SET state = false, car_num =null, last_update = now() WHERE car_num = ?";
        try {
            @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, carParkVO.getCarNum());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 빈 주차공간 조회
    @Override
    public List<CarParkVO> selectEmptyCarPark() {
        String sql = "SELECT * FROM parking_system.parking_spot WHERE state = false";
        List<CarParkVO> carParkVOList = new ArrayList<>();
        try {
            @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CarParkVO carParkVO = CarParkVO.builder()
                        .space(resultSet.getString("space"))
                        .state(resultSet.getBoolean("state"))
                        .carNum(resultSet.getString("car_num"))
                        .lastUpdate(resultSet.getTimestamp("last_update").toLocalDateTime())
                        .build();
                log.info("CarParkVo: {}", carParkVO);
                carParkVOList.add(carParkVO);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return carParkVOList;
    }
}
