package org.example.smart_parking_260219.dao;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.connection.DBConnection;
import org.example.smart_parking_260219.dto.ManagerDTO;
import org.example.smart_parking_260219.vo.ManagerVO;
import org.example.smart_parking_260219.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ManagerDAO {

    private static ManagerDAO instance;

    private ManagerDAO() {
    }

    public static ManagerDAO getInstance() {
        if (instance == null) {
            instance = new ManagerDAO();
        }
        return instance;
    }

    /* 관리자 계정 등록 - 비밀번호 암호화(BCrypt)*/
    public void insertManager(ManagerVO managerVO) {
        String sql = "INSERT INTO manager (manager_id, manager_name, password, email) VALUES (?, ?, ?, ?)";
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, managerVO.getManagerId());
            preparedStatement.setString(2, managerVO.getManagerName());

            // 사용자가 입력한 평문 비밀번호 -> BCrypt로 비밀번호 해싱(암호화)
            String hashedPassword = PasswordUtil.hashPassword(managerVO.getPassword());
            preparedStatement.setString(3, hashedPassword);

            preparedStatement.setString(4, managerVO.getEmail());
            preparedStatement.executeUpdate();  //INSERT 실행

            log.info("관리자 계정 등록 완료 - ID: {}, 이름: {}",
                    managerVO.getManagerId(), managerVO.getManagerName());
        } catch (SQLException e) {
            log.error("관리자 계정 등록 중 오류 발생", e);
            throw new RuntimeException(e);
        }
    }

    /*
     비밀번호 암호화 -> BCrypt 방식 적용 전, 삭제 예정

     @deprecated : 이 메서드는 더 이상 사용되지 않는다는 뜻.
     (PasswordUtil.hashPassword() 사용 권장)
     */
    @Deprecated
    public String passEncode(String password) {
        log.warn("passEncode() 메서드는 deprecated 되었습니다. PasswordUtil.hashPassword()를 사용하세요.");
        // 실제 동작은 새로운 로직(PasswordUtil)에 위임함 (하위 호환성 유지)
        return PasswordUtil.hashPassword(password);
    }

    /* 아이디로 계정 찾기 */
    public ManagerVO selectOne(String managerId) {
        String sql = "SELECT * FROM manager WHERE manager_id = ?";
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, managerId);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ManagerVO managerVO = ManagerVO.builder()
                        .managerNo(resultSet.getInt("manager_no"))
                        .managerId(resultSet.getString("manager_id"))
                        .managerName(resultSet.getString("manager_name"))
                        .password(resultSet.getString("password"))  // DB에 저장된 해시값이 담김
                        .email(resultSet.getString("email"))
                        .active(resultSet.getBoolean("active"))
                        .role(resultSet.getString("role"))
                        .build();
                return managerVO;
            }
        } catch (SQLException e) {
            log.error("관리자 조회 중 오류 발생 - ID: {}", managerId, e);
            throw new RuntimeException(e);
        }
        return null;
    }

    /* 모든 관리자 목록 조회 */
    public List<ManagerVO> selectAll() {
        String sql = "SELECT * FROM manager ORDER BY manager_no DESC";
        List<ManagerVO> list = new ArrayList<>();
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ManagerVO vo = ManagerVO.builder()
                        .managerNo(resultSet.getInt("manager_no"))
                        .managerId(resultSet.getString("manager_id"))
                        .managerName(resultSet.getString("manager_name"))
                        .password(resultSet.getString("password"))  // DB에 저장된 해시값이 담김
                        .email(resultSet.getString("email"))
                        .active(resultSet.getBoolean("active"))
                        .role(resultSet.getString("role"))
                        .build();
                list.add(vo);
            }
        } catch (SQLException e) {
            log.error("관리자 목록 조회 중 오류 발생", e);
            throw new RuntimeException(e);
        }
        return list;
    }

    /* 관리자 계정 사용 (활성화/비활성화 수정) */
    public void updateActive(boolean active, String managerId) {
        String sql = "UPDATE manager set active = ? WHERE manager_id = ?";
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, active);
            preparedStatement.setString(2, managerId);
            preparedStatement.executeUpdate();

            log.info("관리자 활성화 상태 변경 - ID: {}, Active: {}", managerId, active);

        } catch (SQLException e) {
            log.error("관리자 활성화 상태 변경 중 오류 발생", e);
            throw new RuntimeException(e);
        }
    }

    /* 관리자 정보 수정 (이름, 비밀번호, 이메일) */
    // 비밀번호가 비어있으면 기존 비밀번호 유지
    public void updateManager(ManagerVO managerVO) {
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();

            // 변경할 비밀번호 값이 입력되었는지 확인
            String password = managerVO.getPassword();
            boolean updatePassword = (password != null && !password.trim().isEmpty());

            String sql;
            PreparedStatement preparedStatement;

            /* 비밀번호 수정 */
            if (updatePassword) {
                // 비밀번호를 수정하는 경우
                sql = "UPDATE manager SET manager_name = ?, password = ?, email = ? WHERE manager_id = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, managerVO.getManagerName());

                // BCrypt (이미 해시된 경우는 그대로 사용)
                String hashedPassword;
                // 이미 해시화된 값($2a$) -> 해싱 X [중복 해싱 방지]
                if (password.length() == 60 && password.startsWith("$2a$")) {
                    hashedPassword = password;
                    log.debug("기존 BCrypt 해시 유지 - ID: {}", managerVO.getManagerId());
                } else {
                    // 평문 비밀번호인 경우 새로 해싱
                    hashedPassword = PasswordUtil.hashPassword(password);
                    log.info("새 비밀번호로 변경 - ID: {}", managerVO.getManagerId());
                }
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, managerVO.getEmail());
                preparedStatement.setString(4, managerVO.getManagerId());

            } else {
                // 비밀번호는 변경하지 않고 이름과 이메일만 수정
                sql = "UPDATE manager SET manager_name = ?, email = ? WHERE manager_id = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, managerVO.getManagerName());
                preparedStatement.setString(2, managerVO.getEmail());
                preparedStatement.setString(3, managerVO.getManagerId());

                log.info("비밀번호 제외하고 정보 수정 - ID: {}", managerVO.getManagerId());
            }

            int affectedRows = preparedStatement.executeUpdate();
            preparedStatement.close();

            if (affectedRows > 0) {
                log.info("관리자 정보 업데이트 성공 - ID: {}, 이름: {}, 비밀번호 변경: {}",
                        managerVO.getManagerId(), managerVO.getManagerName(), updatePassword);
            } else {
                log.warn("업데이트된 행이 없음 - ID: {}", managerVO.getManagerId());
            }

        } catch (SQLException e) {
            log.error("관리자 정보 업데이트 중 오류 발생 - ID: {}", managerVO.getManagerId(), e);
            throw new RuntimeException("관리자 정보 업데이트 실패", e);
        }
    }

    /* 비밀번호'만' 변경 */
    public void updatePassword(String managerId, String newPassword) {
        String sql = "UPDATE manager SET password = ? WHERE manager_id = ?";
        try {
            @Cleanup Connection connection = DBConnection.INSTANCE.getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // BCrypt로 새 비밀번호 해싱
            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, managerId);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                log.info("비밀번호 변경 성공 - ID: {}", managerId);
            } else {
                log.warn("비밀번호 변경 실패 - 존재하지 않는 ID: {}", managerId);
            }

        } catch (SQLException e) {
            log.error("비밀번호 변경 중 오류 발생 - ID: {}", managerId, e);
            throw new RuntimeException("비밀번호 변경 실패", e);
        }
    }
}