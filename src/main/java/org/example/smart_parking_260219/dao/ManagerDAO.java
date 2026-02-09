package org.example.smart_parking_260219.dao;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.database.DBConnection;
import org.example.smart_parking_260219.vo.ManagerVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Log4j2
public class ManagerDAO {
    private static ManagerDAO instance;

    private ManagerDAO() {
    }

    public static ManagerDAO getInstance() {
        if (instance == null) {
            instance = new ManagerDAO();
        }
        return instance;
    }

    /* 관리자 검색 */
    public ManagerVO selectManager(String managerId) {
        String sql = "SELECT * FROM smart_parking_team2.manager WHERE manager_id = ?";

        try {
            @Cleanup Connection connection = DBConnection.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, managerId);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                ManagerVO managerVO = ManagerVO.builder()
                        .managerNo(resultSet.getInt("manager_no"))
                        .managerId(resultSet.getString("manager_id"))
                        .managerName(resultSet.getString("manager_name"))
                        .password(resultSet.getString("password"))
                        .email(resultSet.getString("email"))
                        .active(resultSet.getBoolean("active")).build();
                log.info("managerVO : {}", managerVO);
                return managerVO;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /* 관리자 등록 */
    public void insertManager(ManagerVO managerVO) {
        String sql = "INSERT INTO smart_parking_team2.manager (manager_no, manager_id, manager_name, password, email, active) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            @Cleanup Connection connection = DBConnection.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, managerVO.getManagerNo());
            preparedStatement.setString(2, managerVO.getManagerId());
            preparedStatement.setString(3, managerVO.getManagerName());
            preparedStatement.setString(4, managerVO.getPassword());
            preparedStatement.setString(5, managerVO.getEmail());
            preparedStatement.setBoolean(6, managerVO.isActive());

            preparedStatement.executeUpdate(); //INSERT 실행

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /* 관리자 수정 */
    public void updateManager(ManagerVO managerVO) {
        String sql = "UPDATE smart_parking_team2.manager SET manager_name = ?, password = ?, email = ?, active = ? WHERE manager_id = ?";

        try {
            @Cleanup Connection connection = DBConnection.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, managerVO.getManagerName());
            preparedStatement.setString(2, managerVO.getPassword());
            preparedStatement.setString(3, managerVO.getEmail());
            preparedStatement.setBoolean(4, managerVO.isActive());
            preparedStatement.setString(5, managerVO.getManagerId());

            preparedStatement.executeUpdate(); //UPDATE 실행

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
