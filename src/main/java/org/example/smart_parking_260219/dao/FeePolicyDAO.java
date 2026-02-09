package org.example.smart_parking_260219.dao;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dbconnection.DBConnection_sms;
import org.example.smart_parking_260219.vo.FeePolicyVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class FeePolicyDAO {
    private static FeePolicyDAO instance;

    public static FeePolicyDAO getInstance() {
        if (instance == null) {
            instance = new FeePolicyDAO();
        }
        return instance;
    }

    // 새로운 정책 등록 (생성된 PK 반환)
    public void insertPolicy(FeePolicyVo feePolicyVo) {
        /* 데이터베이스에 정책을 추가하는 메서드 */
        log.info("feePolicyVo : {}", feePolicyVo);

        String sql = "INSERT INTO fee_policy " +
                " (grace_period, default_time, default_fee, extra_time, extra_fee, light_discount, " +
                "disabled_discount, subscribed_fee, max_daily_fee, is_active, modify_date) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())";


        try {
            @Cleanup Connection connection = DBConnection_sms.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, feePolicyVo.getGracePeriod());
            preparedStatement.setInt(2, feePolicyVo.getDefaultTime());
            preparedStatement.setInt(3, feePolicyVo.getDefaultFee());
            preparedStatement.setInt(4, feePolicyVo.getExtraTime());
            preparedStatement.setInt(5, feePolicyVo.getExtraFee());
            preparedStatement.setDouble(6, feePolicyVo.getLightDiscount());
            preparedStatement.setDouble(7, feePolicyVo.getDisabledDiscount());
            preparedStatement.setInt(8, feePolicyVo.getSubscribedFee());
            preparedStatement.setInt(9, feePolicyVo.getMaxDailyFee());
            preparedStatement.setBoolean(10, feePolicyVo.isActive());

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // [전체 조회] 요금 정책 목록 출력
    public List<FeePolicyVo> selectAllPolicies() {
        List<FeePolicyVo> list = new ArrayList<>();

        String sql = "SELECT * FROM fee_policy ORDER BY policy_id DESC";

        try {
            @Cleanup Connection connection = DBConnection_sms.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FeePolicyVo vo = FeePolicyVo.builder()
                        .policyId(resultSet.getInt("policy_id"))
                        .gracePeriod(resultSet.getInt("grace_period"))
                        .defaultTime(resultSet.getInt("default_time"))
                        .defaultFee(resultSet.getInt("default_fee"))
                        .extraTime(resultSet.getInt("extra_time"))
                        .extraFee(resultSet.getInt("extra_fee"))
                        .lightDiscount(resultSet.getDouble("light_discount"))
                        .disabledDiscount(resultSet.getDouble("disabled_discount"))
                        .subscribedFee(resultSet.getInt("subscribed_fee"))
                        .maxDailyFee(resultSet.getInt("max_daily_fee"))
                        .isActive(resultSet.getBoolean("is_active"))
                        .modifyDate(resultSet.getTimestamp("modify_date") != null
                                ? resultSet.getTimestamp("modify_date").toLocalDateTime()
                                : null)
                        .build();

                list.add(vo);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    // [단건 조회] 요금 정책 상세 출력
    public FeePolicyVo selectOnePolicy(int policyId) {
        String sql = "SELECT * FROM fee_policy WHERE policy_id = ?";

        try {
            @Cleanup Connection connection = DBConnection_sms.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, policyId);

            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return FeePolicyVo.builder()
                        .policyId(resultSet.getInt("policy_id"))
                        .gracePeriod(resultSet.getInt("grace_period"))
                        .defaultTime(resultSet.getInt("default_time"))
                        .defaultFee(resultSet.getInt("default_fee"))
                        .extraTime(resultSet.getInt("extra_time"))
                        .extraFee(resultSet.getInt("extra_fee"))
                        .lightDiscount(resultSet.getDouble("light_discount"))
                        .disabledDiscount(resultSet.getDouble("disabled_discount"))
                        .subscribedFee(resultSet.getInt("subscribed_fee"))
                        .maxDailyFee(resultSet.getInt("max_daily_fee"))
                        .isActive(resultSet.getBoolean("is_active"))
                        .modifyDate(resultSet.getTimestamp("modify_date") != null
                                ? resultSet.getTimestamp("modify_date").toLocalDateTime()
                                : null)
                        .build();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    // 정책 수정
    public int updatePolicy(FeePolicyVo feePolicyVo) {
        log.info("update feePolicyVo : {}", feePolicyVo);

        String sql =
                "UPDATE fee_policy SET " +
                        " grace_period = ?, " +
                        " default_time = ?, " +
                        " default_fee = ?, " +
                        " extra_time = ?, " +
                        " extra_fee = ?, " +
                        " light_discount = ?, " +
                        " disabled_discount = ?, " +
                        " subscribed_fee = ?, " +
                        " max_daily_fee = ?, " +
                        " is_active = ?, " +
                        " modify_date = now() " +
                        "WHERE policy_id = ?";

        try {
            @Cleanup Connection connection = DBConnection_sms.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, feePolicyVo.getGracePeriod());
            preparedStatement.setInt(2, feePolicyVo.getDefaultTime());
            preparedStatement.setInt(3, feePolicyVo.getDefaultFee());
            preparedStatement.setInt(4, feePolicyVo.getExtraTime());
            preparedStatement.setInt(5, feePolicyVo.getExtraFee());
            preparedStatement.setDouble(6, feePolicyVo.getLightDiscount());
            preparedStatement.setDouble(7, feePolicyVo.getDisabledDiscount());
            preparedStatement.setInt(8, feePolicyVo.getSubscribedFee());
            preparedStatement.setInt(9, feePolicyVo.getMaxDailyFee());
            preparedStatement.setBoolean(10, feePolicyVo.isActive());
            preparedStatement.setInt(11, feePolicyVo.getPolicyId());

            return preparedStatement.executeUpdate(); // 성공 시 1, 실패 시 0
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    // 정책 삭제
    public int deletePolicy(int policyId) {
        log.info("delete policyId : {}", policyId);

        String sql = "DELETE FROM fee_policy WHERE policy_id = ?";

        try {
            @Cleanup Connection connection = DBConnection_sms.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, policyId);
            return preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}

