package org.example.smart_parking_260219.dao;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.connection.DBConnection;
import org.example.smart_parking_260219.dto.PaymentDTO;
import org.example.smart_parking_260219.vo.PaymentVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2

public class PaymentDAO {

    // 등록 - 요금 계산 후 결제 정보 저장
    public void insertPayment(PaymentVO vo) {
        log.info("insertPayment 실행: " + vo.getCarNum());

        // 1. 해당 차량의 '출차 전' 주차 기록 ID 조회 (차 번호 존재 여부 확인 대용)
        // 2. 결제 내역(payment) 등록
        // 3. 주차 기록(parking)의 결제 완료 및 출차 시간 업데이트

        String sql = "INSERT INTO payment (parking_id, policy_id, payment_type, calculated_fee, discount_amount, final_fee, payment_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, NOW())";

        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // 결제 정보 등록
            preparedStatement.setInt(1, vo.getParkingId());
            preparedStatement.setInt(2, vo.getPolicyId());
            preparedStatement.setInt(3, vo.getPaymentType());
            preparedStatement.setInt(4, vo.getCalculatedFee());
            preparedStatement.setInt(5, vo.getDiscountAmount());
            preparedStatement.setInt(6, vo.getFinalFee());
            preparedStatement.executeUpdate();

            // 주차 상태 업데이트
            String updateParkingSql = "UPDATE parking SET paid = TRUE, exit_time = NOW() WHERE parking_id = ?";

            preparedStatement = connection.prepareStatement(updateParkingSql);
            preparedStatement.setInt(1, vo.getParkingId());
            preparedStatement.executeUpdate();

            log.info("결제 및 출차 처리 완료");
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    // 전체 조회 - 결제 목록 출력 (차량번호 포함)
    public List<PaymentVO> selectAllPayments() {
        List<PaymentVO> paymentVOList = new ArrayList<>();
        String sql = "SELECT pay.*, p.car_num FROM payment pay "
                + "JOIN parking p ON pay.parking_id = p.parking_id "
                + "ORDER BY pay.payment_id DESC";
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                PaymentVO paymentVO = PaymentVO.builder()
                        .paymentId(resultSet.getInt("payment_id"))
                        .parkingId(resultSet.getInt("parking_id"))
                        .policyId(resultSet.getInt("policy_id"))
                        .carNum(resultSet.getString("car_num"))
                        .paymentType(resultSet.getInt("payment_type"))
                        .calculatedFee(resultSet.getInt("calculated_fee"))
                        .discountAmount(resultSet.getInt("discount_amount"))
                        .finalFee(resultSet.getInt("final_fee"))
                        .paymentDate(resultSet.getDate("payment_date").toLocalDate()).build();
                paymentVOList.add(paymentVO);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return paymentVOList;
    }

    // 단건 조회 - 결제 내역 상세 출력
    public PaymentVO selectOnePayment(int paymentNo) {
        String sql = "SELECT pay.*, p.car_num FROM payment pay "
                + "JOIN parking p ON pay.parking_id = p.parking_id "
                + "WHERE pay.payment_id = ?";
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, paymentNo);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                PaymentVO paymentVO = PaymentVO.builder()
                        .paymentId(resultSet.getInt("payment_id"))
                        .parkingId(resultSet.getInt("parking_id"))
                        .policyId(resultSet.getInt("policy_id"))
                        .carNum(resultSet.getString("car_num"))
                        .paymentType(resultSet.getInt("payment_type"))
                        .calculatedFee(resultSet.getInt("calculated_fee"))
                        .discountAmount(resultSet.getInt("discount_amount"))
                        .finalFee(resultSet.getInt("final_fee"))
                        .paymentDate(resultSet.getDate("payment_date").toLocalDate()).build();
                return paymentVO;
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return null;
    }
}
