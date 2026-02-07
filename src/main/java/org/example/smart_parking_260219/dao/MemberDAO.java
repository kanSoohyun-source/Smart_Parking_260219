package org.example.smart_parking_260219.dao;

import lombok.Cleanup;
import org.example.smart_parking_260219.vo.MemberVO;
import org.example.smart_parking_260219.dbconnection.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    private static MemberDAO instance;

    public MemberDAO() {}

    public static MemberDAO getInstance() {
        if(instance == null) {
            instance = new MemberDAO();
        }
        return instance;
    }

    public void insertMember(MemberVO memberVO) throws SQLException {
        String sql = "INSERT INTO member (member_id, subscription_id, car_num, car_type, name, phone, subscribed) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        @Cleanup Connection connection = ConnectionUtil.Instance.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, String.valueOf(memberVO.getMemberId()));
        preparedStatement.setString(2, String.valueOf(memberVO.getSubscriptionId()));
        preparedStatement.setString(3, memberVO.getCarNum());
        preparedStatement.setString(4, String.valueOf(memberVO.getCarType()));
        preparedStatement.setString(5, memberVO.getName());
        preparedStatement.setString(6, memberVO.getPhone());
        preparedStatement.setBoolean(7, (memberVO.isSubscribed()));

        preparedStatement.executeUpdate();
    }

    public List<MemberVO> selectAllMember() throws SQLException {
        String sql ="SELECT * FROM member";

        List<MemberVO> memberVOList = new ArrayList<>();

        @Cleanup Connection connection = ConnectionUtil.Instance.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            MemberVO memberVO = MemberVO.builder()
                    .memberId(Integer.parseInt(resultSet.getString("member_id")))
                    .subscriptionId(Integer.parseInt(resultSet.getString("subscription_id")))
                    .carNum(resultSet.getString("car_num"))
                    .carType(Integer.parseInt(resultSet.getString("car_type")))
                    .name(resultSet.getString("name"))
                    .phone(resultSet.getString("phone"))
                    .subscribed(resultSet.getBoolean("subscribed"))
                    .build();
            memberVOList.add(memberVO);
        }
        return memberVOList;
    }

    public MemberVO selectOneMember(String carNum) throws SQLException {
        String sql = "SELECT * FROM member WHERE car_num = ?";

        @Cleanup Connection connection = ConnectionUtil.Instance.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, carNum);
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()) {
            MemberVO memberVO = MemberVO.builder()
                    .memberId(Integer.parseInt(resultSet.getString("member_id")))
                    .subscriptionId(Integer.parseInt(resultSet.getString("subscription_id")))
                    .carNum(resultSet.getString("car_num"))
                    .carType(Integer.parseInt(resultSet.getString("car_type")))
                    .name(resultSet.getString("name"))
                    .phone(resultSet.getString("phone"))
                    .subscribed(resultSet.getBoolean("subscribed"))
                    .build();
            return memberVO;
        }
        return null;
    }

    public void updateMember(MemberVO memberVO) throws SQLException {
        String sql = "UPDATE member SET " +
                "car_type = ?, name = ?, phone = ?, subscribed = ? " +
                "WHERE car_num = ?";

        @Cleanup Connection connection = ConnectionUtil.Instance.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, String.valueOf(memberVO.getCarType()));
        preparedStatement.setString(2, memberVO.getName());
        preparedStatement.setString(3, memberVO.getPhone());
        preparedStatement.setBoolean(4, (memberVO.isSubscribed()));
        preparedStatement.setString(5, memberVO.getCarNum());

        preparedStatement.executeUpdate();
    }

    public void deleteMember (String carNum) throws SQLException {
        String sql = "DELETE FROM member WHERE car_num = ?";

        @Cleanup Connection connection = ConnectionUtil.Instance.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, carNum);
        preparedStatement.executeUpdate();
    }
}
