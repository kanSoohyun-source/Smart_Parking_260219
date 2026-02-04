package org.example.smart_parking_260219.DAO;

import lombok.Cleanup;
import org.example.smart_parking_260219.Util.ConnectionUtil;
import org.example.smart_parking_260219.VO.MemberVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    public void insertMember(MemberVO memberVO) {
        /* 회원 등록*/
        String sql = "INSERT INTO member VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            @Cleanup Connection connection = ConnectionUtil.Instance.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, memberVO.getCarNum());
            preparedStatement.setString(2, String.valueOf(memberVO.getCarType()));
            preparedStatement.setString(3, memberVO.getName());
            preparedStatement.setString(4, memberVO.getPhone());
            preparedStatement.setString(5, String.valueOf(memberVO.isSubscribed()));
            preparedStatement.setString(6, String.valueOf(memberVO.getSubStartDate()));
            preparedStatement.setString(7, String.valueOf(memberVO.getSubEndDate()));
            preparedStatement.setString(8, String.valueOf(memberVO.getCreatedAt()));
            preparedStatement.setString(9, String.valueOf(memberVO.isActive()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<MemberVO> selectAllMember() {
        /*회원 목록 조회*/
        String sql = "SELECT * FROM member";

        List<MemberVO> memberVOList = new ArrayList<>();

        try {
            @Cleanup Connection connection = ConnectionUtil.Instance.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                MemberVO memberVO = MemberVO.builder()
                        .memberId(resultSet.getInt("member_id"))
                        .carNum(resultSet.getString("car_num"))
                        .carType(resultSet.getInt("car_type"))
                        .name(resultSet.getString("name"))
                        .phone(resultSet.getString("phone"))
                        .subscribed(resultSet.getBoolean("is_subscribed"))
                        .subStartDate(resultSet.getDate("sub_start_date"))
                        .subEndDate(resultSet.getDate("sub_end_date"))
                        .createdAt(resultSet.getDate("created_at").toLocalDate())
                        .active(resultSet.getBoolean("is_active"))
                        .build();
                memberVOList.add(memberVO);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return memberVOList;
    }

    public MemberVO selectOneMember(String carNum) {
        /* 차량 번호로 회원 조회 */
        String sql = "SELECT * FROM member WHERE car_num = ?";

        try {
            @Cleanup Connection connection = ConnectionUtil.Instance.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, carNum);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                MemberVO memberVO = MemberVO.builder()
                        .carType(resultSet.getInt("car_type"))
                        .name(resultSet.getString("name"))
                        .phone(resultSet.getString("phone"))
                        .subscribed(resultSet.getBoolean("is_subscribed"))
                        .subStartDate(resultSet.getDate("sub_start_date"))
                        .subEndDate(resultSet.getDate("sub_end_date"))
                        .createdAt(resultSet.getDate("created_at").toLocalDate())
                        .active(resultSet.getBoolean("is_active"))
                        .build();
                return memberVO;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateMember(MemberVO memberVO) {
        /* 회원 정보 수정 하기*/
        String sql = "UPDATE member SET " +
                "car_type = ?, name = ?, phone = ?, is_subscribed = ?, " +
                "sub_start_date = ?, sub_end_date = ?, created_at = ?, is_active = ?" +
                "WHERE car_num = ?";

        try {
            @Cleanup Connection connection = ConnectionUtil.Instance.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(memberVO.getCarType()));
            preparedStatement.setString(2, memberVO.getName());
            preparedStatement.setString(3, memberVO.getPhone());
            preparedStatement.setString(4, String.valueOf(memberVO.isSubscribed()));
            preparedStatement.setString(5, String.valueOf(memberVO.getSubStartDate()));
            preparedStatement.setString(6, String.valueOf(memberVO.getSubEndDate()));
            preparedStatement.setString(7, String.valueOf(memberVO.getCreatedAt()));
            preparedStatement.setString(8, String.valueOf(memberVO.isActive()));
            preparedStatement.setString(9, memberVO.getCarNum());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteMember(int carNum) {
        /*회원 삭제 하기*/
        String sql = "DELETE FROM member WHERE car_num = ?";

        try {
            @Cleanup Connection connection = ConnectionUtil.Instance.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, carNum);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}



