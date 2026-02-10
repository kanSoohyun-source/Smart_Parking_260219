package org.example.smart_parking_260219.dao;

import lombok.Cleanup;
import org.example.smart_parking_260219.connection.DBConnection;
import org.example.smart_parking_260219.vo.SubscribeVO;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SubscribeDAO {
    private static SubscribeDAO instance;

    public SubscribeDAO() {}

    public static SubscribeDAO getInstance() {
        if(instance == null) {
            instance = new SubscribeDAO();
        }
        return instance;
    }
    // 월정액 가입
    // 월정액 아이디는 자동증분, 멤버아이디는 member 테이블에서 외래키로 가져오기
    public void insertSubscribe(SubscribeVO subscribeVO) throws SQLException {
        String sql = "INSERT INTO subscribe (member_id, start_date, end_date, status, payment_amount, last_update) " +
                "VALUES (?, ?, ?, ?, ?, NOW())";

        @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, subscribeVO.getMemberId());
        preparedStatement.setObject(2, subscribeVO.getStartDate());
        preparedStatement.setObject(3, subscribeVO.getEndDate());
        preparedStatement.setBoolean(4, subscribeVO.isStatus());
        preparedStatement.setInt(5, subscribeVO.getPaymentAmount());

        preparedStatement.executeUpdate();
    }
    // 월정액 회원 조회
    // 월정액 회원으로 한번 이상 등록된 회원 조회
    public List<SubscribeVO> selectAllSubscribe() throws SQLException {
        String sql ="SELECT * FROM subscribe";

        List<SubscribeVO> subscribeVOS = new ArrayList<>();

        @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            SubscribeVO subscribeVO = SubscribeVO.builder()
                    .subscriptionId(resultSet.getInt("subscription_id"))
                    .memberId(resultSet.getInt("member_id"))
                    .startDate(resultSet.getObject("start_date", LocalDateTime.class))
                    .endDate(resultSet.getObject("end_date", LocalDateTime.class))
                    .status(resultSet.getBoolean("status"))
                    .paymentAmount(resultSet.getInt("payment_amount"))
                    .lastUpdate(resultSet.getObject("last_update", LocalDateTime.class))
                    .build();
            subscribeVOS.add(subscribeVO);
        }
        return subscribeVOS;
    }

    // 회원 상세 조회(차량 번호로 조회)
    public SubscribeVO selectOneSubscribe(int memberId) throws SQLException {
        String sql = "SELECT * FROM subscribe WHERE member_id = ?";

        @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, memberId);
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()) {
            SubscribeVO subscribeVO = SubscribeVO.builder()
                    .memberId(resultSet.getInt("member_id"))
                    .startDate(resultSet.getObject("start_date", LocalDateTime.class))
                    .endDate(resultSet.getObject("end_date", LocalDateTime.class))
                    .status(resultSet.getBoolean("status"))
                    .paymentAmount(resultSet.getInt("payment_amount"))
                    .lastUpdate(resultSet.getObject("last_update", LocalDateTime.class))
                    .build();
            return subscribeVO;
        }
        return null;
    }

    // 회원 정보 수정
    // 차량번호로 검색 후 정보 수정. member_id, create_date 수정 항목 제외
    public void updateSubscribe(SubscribeVO subscribeVO) throws SQLException {
        String sql = "UPDATE subscribe SET " +
                "start_date = ?, end_date = ?, status = ?, payment_amount = ? " +
                "WHERE member_id = ?";

        @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setObject(1, subscribeVO.getStartDate());
        preparedStatement.setObject(2, subscribeVO.getEndDate());
        preparedStatement.setBoolean(3, subscribeVO.isStatus());
        preparedStatement.setInt(4, subscribeVO.getPaymentAmount());
        preparedStatement.setInt(5, subscribeVO.getMemberId());

        preparedStatement.executeUpdate();
    }

    // 회원 삭제
    public void deletesubscribe (int memberId) throws SQLException {
        String sql = "DELETE FROM subscribe WHERE member_id = ?";

        @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, memberId);
        preparedStatement.executeUpdate();
    }
}
