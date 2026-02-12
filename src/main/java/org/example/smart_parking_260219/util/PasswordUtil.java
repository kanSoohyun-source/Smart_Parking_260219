//package org.example.smart_parking_260219.util;
//
//import org.example.smart_parking_260219.connection.DBConnection;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class PasswordUtil {
//    /*
//    MySQL PASSWORD() 함수를 사용한 비밀번호 암호화
//    @Param conn DB 연결
//    @param password 평문 비밀번호
//    @return 암호화된 비밀번호
//     */
//
//    // 작업이 원활히 끝나면 HASH(SHA-256) 알고리즘으로 변경해볼 것
//    public static String encodePassword(DBConnection connection, String password) throws SQLException {
//        String sql = "SELECT PASSWORD(?) AS hashed";
//        try (PreparedStatement preparedStatement = connection.getConnection(sql)) {
//            preparedStatement.setString(1, password);
//            ResultSet rs = preparedStatement.executeQuery();
//            if (rs.next()) {
//                return rs.getString("hashed");
//            }
//        }
//        throw new SQLException("비밀번호 암호화 실패");
//    }
//}
