package org.example.smart_parking_260219.dao;

import lombok.Cleanup;
import org.example.smart_parking_260219.connection.DBConnection;
import org.example.smart_parking_260219.vo.MemberVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    // 회원 가입을 위한 메서드
    public void insertMember(MemberVO memberVO) throws SQLException {
        String sql = "INSERT INTO member (car_num, car_type, name, phone, create_date ,subscribed) " +
                "VALUES (?, ?, ?, ?, NOW(), ?)";

        @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, memberVO.getCarNum());
        preparedStatement.setString(2, String.valueOf(memberVO.getCarType()));
        preparedStatement.setString(3, memberVO.getName());
        preparedStatement.setString(4, memberVO.getPhone());
        preparedStatement.setBoolean(5, (memberVO.isSubscribed()));

        preparedStatement.executeUpdate();
    }

    // 전체 회원 목록을 출력하는 메서드
    public List<MemberVO> selectAllMember() throws SQLException {
        String sql ="SELECT * FROM member ORDER BY member_id DESC";

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
                    .createDate(resultSet.getObject("create_date", LocalDateTime.class))
                    .subscribed(resultSet.getBoolean("subscribed"))
                    .build();
            memberVOList.add(memberVO);
        }
        return memberVOList;
    }

    // 차량 뒷 4자리로 검색하는 메서드
    public List<MemberVO> selectCar4Num(String car4Num) throws SQLException {
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
                    .createDate(resultSet.getObject("create_date", LocalDateTime.class))
                    .subscribed(resultSet.getBoolean("subscribed"))
                    .build();
            memberVOList.add(memberVO);
        }
        return memberVOList;
    }

    // 차량 뒷 4자리로 검색하는 메서드
    public MemberVO selectOneMember(String carNum) throws SQLException {
        String sql = "SELECT * FROM member WHERE car_num = ?";

        List<MemberVO> memberVOList = new ArrayList<>();

        @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, carNum);
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            MemberVO memberVO = MemberVO.builder()
                    .memberId(resultSet.getInt("member_id"))
                    .carNum(resultSet.getString("car_num"))
                    .carType(resultSet.getInt("car_type"))
                    .name(resultSet.getString("name"))
                    .phone(resultSet.getString("phone"))
                    .createDate(resultSet.getObject("create_date", LocalDateTime.class))
                    .subscribed(resultSet.getBoolean("subscribed"))
                    .build();
            return (memberVO);
        }
        return null;
    }

    // 회원 수정 메서드
    public void updateMember(MemberVO memberVO) throws SQLException {
        String sql = "UPDATE member SET " +
                "car_type = ?, name = ?, phone = ?, subscribed = ? " +
                "WHERE car_num = ?";

        @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, String.valueOf(memberVO.getCarType()));
        preparedStatement.setString(2, memberVO.getName());
        preparedStatement.setString(3, memberVO.getPhone());
        preparedStatement.setBoolean(4, (memberVO.isSubscribed()));
        preparedStatement.setString(5, memberVO.getCarNum());

        preparedStatement.executeUpdate();
    }

    // 회원 삭제 메서드
    public void deleteMember (String carNum) throws SQLException {
        String sql = "DELETE FROM member WHERE car_num = ?";

        @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, carNum);
        preparedStatement.executeUpdate();
    }

}
