package org.example.smart_parking_260219.dao;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.connection.DBConnection;
import org.example.smart_parking_260219.dto.StatisticsDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log4j2

public class StatisticsDAO {

    private static StatisticsDAO instance;

    // 수정 포인트: 외부에서 new StatisticsDAO()를 호출하지 못하도록 private으로 설정
    private StatisticsDAO() {}

    public static StatisticsDAO getInstance() {
        if(instance == null) {
            instance = new StatisticsDAO();
        }
        return instance;
    }

    /*
     * 선택한 하루의 시간대별 매출 조회
     * targetDate "2026-02-12" 형태
     */
    public List<StatisticsDTO> selectHourlySalesByDay(String targetDate) {
        List<StatisticsDTO> list = new ArrayList<>();

        // SQL 설명:
        // 1. HOUR(payment_date): 결제 시점에서 '시간'만 추출 (0~23)
        // 2. WHERE DATE(payment_date) = ?: 사용자가 선택한 그날의 데이터만 필터링
        String sql = "SELECT HOUR(payment_date) as hour, SUM(final_fee) as total " +
                "FROM payment " +
                "WHERE DATE(payment_date) = ? " +
                "GROUP BY hour " +
                "ORDER BY hour";

        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, targetDate);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                StatisticsDTO dto = StatisticsDTO.builder()
                        .label(resultSet.getInt("hour") + "시")
                        .value(resultSet.getLong("total"))
                        .build();
                list.add(dto);
            }
        } catch (SQLException e) {
            log.error("selectHourlySalesByDay SQL error: ", e);
            throw new RuntimeException(e);
        }
        return list;
    }

    /*
     * 선택한 하루의 시간대별 입차 대수 조회
     */
    public List<StatisticsDTO> selectHourlyCountByDay(String targetDate) {
        List<StatisticsDTO> list = new ArrayList<>();

        String sql = "SELECT HOUR(entry_time) as hour, COUNT(*) as cnt " +
                "FROM parking " +
                "WHERE DATE(entry_time) = ? " +
                "GROUP BY hour " +
                "ORDER BY hour";

        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, targetDate);

            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                StatisticsDTO dto = StatisticsDTO.builder()
                        .label(resultSet.getInt("hour") + "시")
                        .value(resultSet.getLong("cnt"))
                        .build();
                list.add(dto);
            }
        } catch (SQLException e) {
            log.error("selectHourlyCountByDay SQL error: ", e);
            throw new RuntimeException(e);
        }
        return list;
    }

    /*
     * 특정 연/월의 월별 매출 통계
     * 사용자가 선택한 연도(year)와 월(month)을 받아 해당 기간의 일별 매출
     */
    public List<StatisticsDTO> selectMonthlySalesByYearMonth(int year, int month) {
        List<StatisticsDTO> list = new ArrayList<>();

        // 날짜 범위 계산
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1);

        // SQL 설명:
        // 1. YEAR(payment_date)와 MONTH(payment_date)를 각각 ? 파라미터로 필터링
        // 2. DATE(payment_date)로 그룹화하여 일별 합계 계산
        // year랑 date를 각각 AND로 비교하는 것 보다 속도가 빠름
        String sql = "SELECT DATE(payment_date) as day, SUM(final_fee) as total " +
                "FROM payment " +
                "WHERE payment_date >= ? AND payment_date < ? " +
                "GROUP BY day " +
                "ORDER BY day";
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, start.toString());
            preparedStatement.setString(2, end.toString());
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                StatisticsDTO dto = StatisticsDTO.builder()
                        .label(resultSet.getString("day")) // "2026-02-01" 형태
                        .value(resultSet.getLong("total"))
                        .build();
                list.add(dto);
            }
        } catch (SQLException e) {
            log.error("selectDailySalesByYearMonth SQL error: ", e);
            throw new RuntimeException(e);
        }
        return list;
    }

    /*
     * 선택 연/월별 차종 이용 통계
     */
    public List<StatisticsDTO> selectCarTypeStatsByPeriod(int year, int month) {
        List<StatisticsDTO> list = new ArrayList<>();

        LocalDate start = LocalDate.of(year, month, 1); // 선택 월의 1일
        LocalDate end = start.plusMonths(1);

        // SQL 설명:
        // 1. 결제 정보와 주차 정보를 연결
        // 2. 결제일(payment_date)을 기준으로 연도와 월을 필터링
        // 3. 차종(car_type)별로 묶어서 카운트
        String sql = "SELECT p.car_type, COUNT(*) as cnt " +
                "FROM payment pay " +
                "JOIN parking p ON pay.parking_id = p.parking_id " +
                "WHERE pay.payment_date >= ? AND pay.payment_date < ? " +
                "GROUP BY p.car_type";

        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, start.toString());
            preparedStatement.setString(2, end.toString());
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int type = resultSet.getInt("car_type");
                String typeName = switch(type) {
                    case 1 -> "일반";
                    case 2 -> "월정액";
                    case 3 -> "경차";
                    case 4 -> "장애인";
                    default -> "기타";
                };

                StatisticsDTO dto = StatisticsDTO.builder()
                        .label(typeName)
                        .value(resultSet.getLong("cnt"))
                        .build();
                list.add(dto);
            }
        } catch (SQLException e) {
            log.error("selectCarTypeStatsByPeriod SQL error: ", e);
            throw new RuntimeException(e);
        }
        return list;
    }
}
