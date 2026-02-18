package org.example.smart_parking_260219.dao;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.connection.DBConnection;
import org.example.smart_parking_260219.vo.ManagerVO;
import org.example.smart_parking_260219.vo.ValidationVO;

import java.sql.*;
import java.time.LocalDateTime;

@Log4j2
public class ValidationDAO {

    /* 추가 */
    public void insert(ValidationVO validationVO) {
        String sql = "INSERT INTO validation (string_otp, email, expiry_time) VALUES (?, ?, ?)";
        final int EXP = 5;
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, validationVO.getStringOTP());
            preparedStatement.setString(2, validationVO.getEmail());
            // 만료 시간 설정 : 현재 시간(LocalDateTime.now()) + 5분(plusMinutes(EXP))
            // DB의 TIMESTAMP 타입과 맞추기 위해 Timestamp.valueOf()로 형변환
            preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().plusMinutes(EXP)));
            preparedStatement.executeUpdate();  //INSERT 실행

            log.info("인증 정보 DB 저장 완료: " + validationVO.getEmail());
        } catch (SQLException e) {
            log.error("인증 정보 DB 저장 실패", e);
            throw new RuntimeException(e);
        }
    }

    /* 조회 */
    public ValidationVO select(String email) {
        ValidationVO valiVO = null;

        String sql = "SELECT * FROM validation WHERE email = ?";
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Timestamp를 LocalDateTime으로 형변환
                Timestamp timestamp = resultSet.getTimestamp("expiry_time");
                LocalDateTime expiryTime = timestamp.toLocalDateTime();

                valiVO = ValidationVO.builder()
                        .no(resultSet.getInt("no"))
                        .stringOTP(resultSet.getString("string_otp"))
                        .email(resultSet.getString("email"))
                        .expiryTime(expiryTime)
                        .build();

                log.info("인증 정보 조회 완료: " + email + ", 만료시간: " + expiryTime);
            } else {
                log.warn("인증 정보 없음: " + email);
            }
        } catch (SQLException e) {
            log.error("인증 정보 조회 실패", e);
            throw new RuntimeException(e);
        }
        return valiVO;
    }

    /* 기존 인증 정보 삭제 (재발송 시) */
    public void deleteByEmail(String email) {
        String sql = "DELETE FROM validation WHERE email = ?";
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);

            // 실행 결과를 deleted 변수에 저장
            int deleted = preparedStatement.executeUpdate();

            log.info("기존 인증 정보 삭제: " + email + " (" + deleted + "건)");
        } catch (SQLException e) {
            log.error("인증 정보 삭제 실패", e);
            throw new RuntimeException(e);
        }
    }
}
