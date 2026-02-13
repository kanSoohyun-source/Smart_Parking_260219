package org.example.smart_parking_260219.dao;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.connection.DBConnection;
import org.example.smart_parking_260219.dto.ManagerDTO;
import org.example.smart_parking_260219.vo.ManagerVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    /* 관리자 계정 등록 - [테스트 완료] */
    public void insertManager(ManagerVO managerVO) {
        String sql = "INSERT INTO manager (manager_id, manager_name, password, email) VALUES (?, ?, ?, ?)";
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, managerVO.getManagerId());
            preparedStatement.setString(2, managerVO.getManagerName());
            preparedStatement.setString(3, managerVO.getPassword());
            preparedStatement.setString(4, managerVO.getEmail());
            preparedStatement.executeUpdate();  //INSERT 실행
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* 비밀번호 암호화 - [] */
    public String passEncode(String password) {
        String sql = "SELECT password(?)";
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, password);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /* 아이디로 계정 찾기 - [테스트 완료] */
    public ManagerVO selectOne(String managerId) {
        String sql = "SELECT * FROM manager WHERE manager_id = ?";
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
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
                        .active(resultSet.getBoolean("active"))
                        .role(resultSet.getString("role"))
                        .build();
                return managerVO;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /* 모든 관리자 목록 조회 [테스트 완료] */
    public List<ManagerVO> selectAll() {
        String sql = "SELECT * FROM manager ORDER BY manager_no DESC";
        List<ManagerVO> list = new ArrayList<>();
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ManagerVO vo = ManagerVO.builder()
                        .managerNo(resultSet.getInt("manager_no"))
                        .managerId(resultSet.getString("manager_id"))
                        .managerName(resultSet.getString("manager_name"))
                        .password(resultSet.getString("password"))
                        .email(resultSet.getString("email"))
                        .active(resultSet.getBoolean("active"))
                        .role(resultSet.getString("role"))
                        .build();
                list.add(vo);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /* 관리자 계정 사용(활성화, 비활성화 수정) - [테스트 완료] */
    // update - active 전환 메서드
    public void updateActive(boolean active, String managerId) {
        String sql = "UPDATE manager set active = ? WHERE manager_id = ?";
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, active);
            preparedStatement.setString(2, managerId);
            preparedStatement.executeUpdate();  //UPDATE 실행

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* 관리자 정보 수정 (이름, 비밀번호, 이메일) */
    public void updateManager(ManagerVO managerVO) {
        String sql = "UPDATE manager SET manager_name = ?, password = ?, email = ? WHERE manager_id = ?";
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, managerVO.getManagerName());
            preparedStatement.setString(2, managerVO.getPassword());
            preparedStatement.setString(3, managerVO.getEmail());
            preparedStatement.setString(4, managerVO.getManagerId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                log.info("관리자 정보 업데이트 성공 - ID: {}, 이름: {}",
                        managerVO.getManagerId(), managerVO.getManagerName());
            } else {
                log.warn("업데이트된 행이 없음 - ID: {}", managerVO.getManagerId());
            }

        } catch (SQLException e) {
            log.error("관리자 정보 업데이트 중 오류 발생 - ID: {}", managerVO.getManagerId(), e);
            throw new RuntimeException("관리자 정보 업데이트 실패", e);
        }
    }
}
