package org.example.smart_parking_260219.dao;

import lombok.Cleanup;
import org.example.smart_parking_260219.connection.DBConnection;
import org.example.smart_parking_260219.vo.MemberVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        String sql = "INSERT INTO member (" +
                "car_num, car_type, name, phone, subscribed, start_date, end_date,subscribed_fee, create_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?,  NOW())";

        @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, memberVO.getCarNum());
        preparedStatement.setString(2, String.valueOf(memberVO.getCarType()));
        preparedStatement.setString(3, memberVO.getName());
        preparedStatement.setString(4, memberVO.getPhone());
        preparedStatement.setBoolean(5, (memberVO.isSubscribed()));
        preparedStatement.setObject(6, memberVO.getStartDate());
        preparedStatement.setObject(7, memberVO.getEndDate());
        preparedStatement.setInt(8, memberVO.getSubscribedFee());

        preparedStatement.executeUpdate();
    }

    public List<MemberVO> selectAllMember() throws SQLException {
        String sql ="SELECT * FROM member ORDER BY member_id DESC ";

        List<MemberVO> memberVOList = new ArrayList<>();

        @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            MemberVO memberVO = MemberVO.builder()
                    .memberId(resultSet.getInt("member_id"))
                    .carNum(resultSet.getString("car_num"))
                    .carType(resultSet.getInt("car_type"))
                    .name(resultSet.getString("name"))
                    .phone(resultSet.getString("phone"))
                    .subscribed(resultSet.getBoolean("subscribed"))
                    .startDate(resultSet.getObject("start_date", java.time.LocalDate.class))
                    .endDate(resultSet.getObject("end_date", java.time.LocalDate.class))
                    .subscribedFee(resultSet.getInt("subscribed_fee"))
                    .createDate(resultSet.getObject("create_date", LocalDateTime.class))
                    .build();
            memberVOList.add(memberVO);
        }
        return memberVOList;
    }

    public MemberVO selectOneMember(String carNum) throws SQLException {
        String sql = "SELECT * FROM member WHERE car_num = ?";

        @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, carNum);
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()) {
            MemberVO memberVO = MemberVO.builder()
                    .memberId(resultSet.getInt("member_id"))
                    .carNum(resultSet.getString("car_num"))
                    .carType(resultSet.getInt("car_type"))
                    .name(resultSet.getString("name"))
                    .phone(resultSet.getString("phone"))
                    .subscribed(resultSet.getBoolean("subscribed"))
                    .startDate(resultSet.getObject("start_date", java.time.LocalDate.class))
                    .endDate(resultSet.getObject("end_date", java.time.LocalDate.class))
                    .subscribedFee(resultSet.getInt("subscribed_fee"))
                    .createDate(resultSet.getObject("create_date", LocalDateTime.class))
                    .build();
            return memberVO;
        }
        return null;
    }

    public List<MemberVO> selectCar4Num(String car4Num) throws SQLException {
        // ✅ 뒤 4자리로 LIKE 검색
        String sql = "SELECT * FROM member WHERE car_num LIKE ?";

        List<MemberVO> memberVOList = new ArrayList<>();

        @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "%" + car4Num);
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            MemberVO memberVO = MemberVO.builder()
                    .memberId(resultSet.getInt("member_id"))
                    .carNum(resultSet.getString("car_num"))
                    .carType(resultSet.getInt("car_type"))
                    .name(resultSet.getString("name"))
                    .phone(resultSet.getString("phone"))
                    .subscribed(resultSet.getBoolean("subscribed"))
                    .startDate(resultSet.getObject("start_date", LocalDate.class))
                    .endDate(resultSet.getObject("end_date", LocalDate.class))
                    .subscribedFee(resultSet.getInt("subscribed_fee"))
                    .createDate(resultSet.getObject("create_date", LocalDateTime.class))
                    .build();
            memberVOList.add(memberVO);
        }
        return memberVOList;
    }

    public void updateMember(MemberVO memberVO) throws SQLException {
        String sql = "UPDATE member SET " +
                "car_type = ?, name = ?, phone = ?, subscribed = ?, start_date = ?, end_date = ? " +
                "WHERE car_num = ?";

        @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, memberVO.getCarType());
        preparedStatement.setString(2, memberVO.getName());
        preparedStatement.setString(3, memberVO.getPhone());
        preparedStatement.setBoolean(4, (memberVO.isSubscribed()));
        preparedStatement.setObject(5, memberVO.getStartDate());
        preparedStatement.setObject(6, memberVO.getEndDate());
        preparedStatement.setString(7, memberVO.getCarNum());

        preparedStatement.executeUpdate();
    }

    public void deleteMember (String carNum) throws SQLException {
        String sql = "DELETE FROM member WHERE car_num = ?";

        @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, carNum);
        preparedStatement.executeUpdate();
    }
}
