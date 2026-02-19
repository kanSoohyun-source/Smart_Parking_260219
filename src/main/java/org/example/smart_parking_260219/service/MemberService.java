package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.connection.DBConnection;
import org.example.smart_parking_260219.dao.MemberDAO;
import org.example.smart_parking_260219.dto.MemberDTO;
import org.example.smart_parking_260219.util.MapperUtil;
import org.example.smart_parking_260219.vo.MemberVO;
import org.modelmapper.ModelMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public enum MemberService {
    INSTANCE;

    private MemberDAO memberDAO;
    private ModelMapper modelMapper;
    private static final int SUBSCRIBED_FEE = 100000; // 월정액 고정 비용

    MemberService() {
        memberDAO = new MemberDAO();
        modelMapper = MapperUtil.INSTANCE.getInstance();
    }

    /* 월정액 매출 적용 서비스
     * 월별 일자별 월정액 매출 (해당 월 1일~말일)
     * param targetDate 조회 기준일
     * return 일자별 매출 Map (날짜, 매출액)
     */
    public Map<String, Long> getMonthlySubscriptionRevenue(LocalDate targetDate) throws SQLException {
        List<MemberVO> allMembers = memberDAO.selectAllMember();

        LocalDate firstDay = targetDate.withDayOfMonth(1);
        LocalDate lastDay = targetDate.withDayOfMonth(targetDate.lengthOfMonth());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 일자별 신규 가입자 수 집계
        Map<String, Long> dailyCount = new HashMap<>();

        // creatDate가 이번달인 월정액 회원 조회(회원등록은 무조건 월정액 가입자)
        for (MemberVO member : allMembers) {
            if (member.getCreateDate() != null) {
                LocalDate createDate = member.getCreateDate().toLocalDate();
                if (!createDate.isBefore(firstDay) && !createDate.isAfter(lastDay)) {
                    String dateKey = createDate.format(formatter);
                    dailyCount.put(dateKey, dailyCount.getOrDefault(dateKey, 0L) + 1);
                }
            }
        }

        // 신규 가입자 수 × 100,000원 = 일별 매출
        Map<String, Long> dailyRevenue = new HashMap<>();
        for (LocalDate date = firstDay; !date.isAfter(lastDay); date = date.plusDays(1)) {
            String dateKey = date.format(formatter);
            long count = dailyCount.getOrDefault(dateKey, 0L);
            dailyRevenue.put(dateKey, count * SUBSCRIBED_FEE);
        }

        return dailyRevenue;
    }

    // 회원 등록
    public void addMember(MemberDTO memberDTO) throws SQLException {
        MemberVO memberVO = modelMapper.map(memberDTO, MemberVO.class);
        log.info(memberVO);
        memberDAO.insertMember(memberVO);
    }

    // 목록 조회
    public List<MemberDTO> getAllMember() throws SQLException {
        List<MemberVO> memberVOList = memberDAO.selectAllMember();

        List<MemberDTO> memberDTOS = memberVOList.stream()
                .map(memberVO -> modelMapper.map(memberVO, MemberDTO.class)).toList();
        return memberDTOS;
    }

    // 차량번호로 조회(8자리)
    public MemberDTO getOneMember(String carNum) throws SQLException {
        MemberVO memberVO = memberDAO.selectOneMember(carNum);

        if (memberVO == null) {
            log.info("회원 정보 없음: {}", carNum);
            return null;
        }

        return modelMapper.map(memberVO, MemberDTO.class);
    }

    // 차량 번호로 조회(뒷 4자리)
    public List<MemberDTO> getCarNum(String car4Num) throws Exception {
        List<MemberVO> memberVOList = memberDAO.selectCar4Num(car4Num);
        return memberVOList.stream()
                .map(vo -> modelMapper.map(vo, MemberDTO.class))
                .toList();
    }


    // 회원 정보 수정
    public void modifyMember(MemberDTO memberDTO) throws SQLException {
        MemberVO memberVO = modelMapper.map(memberDTO, MemberVO.class);
        memberDAO.updateMember(memberVO);
    }

    // 월정액 만료 고객 월정액 여부 false로 변경
    public void expireSubscriptions() throws SQLException {
        memberDAO.updateExpiredSubscriptions();
        log.info("만료된 월정액 회원 처리 완료");
    }

    // 월정액 갱신 (현재 endDate 다음날부터 1개월)
    public void renewSubscription(String carNum) throws SQLException {
        MemberVO memberVO = memberDAO.selectOneMember(carNum);
        if (memberVO == null) throw new SQLException("회원 없음: " + carNum);

        LocalDate baseDate = memberVO.getEndDate();

        // 만료됐으면 오늘 다음날부터, 구독중이면 종료일 다음날부터
        LocalDate newStart = (baseDate == null || baseDate.isBefore(LocalDate.now()))
                ? LocalDate.now().plusDays(1)
                : baseDate.plusDays(1);

        LocalDate newEnd = newStart.plusMonths(1);

        MemberVO memberVO1 = MemberVO.builder()
                .carNum(carNum)
                .memberId(memberVO.getMemberId())
                .carType(memberVO.getCarType())
                .name(memberVO.getName())
                .phone(memberVO.getPhone())
                .subscribed(true)
                .startDate(newStart)
                .endDate(newEnd)
                .subscribedFee(memberVO.getSubscribedFee())
                .createDate(memberVO.getCreateDate())
                .build();

        memberDAO.updateSubscription(memberVO1);
        log.info("월정액 갱신 완료: {} ({} ~ {})", carNum, newStart, newEnd);
    }

    // 회원 삭제
    public void removeMember(String carNum) throws SQLException {
        memberDAO.deleteMember(carNum);
    }
}
