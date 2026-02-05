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
        String sql = "INSERT INTO parking_system.parking_spot (space, state, last_update) VALUES (?,?,?,?,now())";
        try {
            @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, carParkVO.getSpace());
            preparedStatement.setBoolean(2, carParkVO.isState());
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
                        .carNum(resultSet.getInt("car_num"))
                        .phone(resultSet.getString("phone"))
                        .lastUpdate((LocalDateTime) resultSet.getObject("last_update"))
                        .build();
                log.info("CarParkVo: {}", carParkVO);
                carParkVOList.add(carParkVO);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return carParkVOList;
    }

    // 주차공간 현황 갱신
    @Override
    public void updateCarPark(CarParkVO carParkVO) {
        String sql = "UPDATE parking_system.parking_spot SET state = " + !(carParkVO.isState()) + ", car_num =?, phone = ?, last_update = now() WHERE space = ?";
        try {
            @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, carParkVO.getCarNum());
            preparedStatement.setString(2, carParkVO.getPhone());
            preparedStatement.setString(3, carParkVO.getSpace());

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
            if (resultSet.next()) {
                CarParkVO carParkVO = CarParkVO.builder()
                        .space(resultSet.getString("space"))
                        .state(resultSet.getBoolean("state"))
                        .carNum(resultSet.getInt("car_num"))
                        .phone(resultSet.getString("phone"))
                        .lastUpdate((LocalDateTime) resultSet.getObject("last_update"))
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
