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
import java.util.List;

@Log4j2
public enum MemberService {
    INSTANCE;

    private MemberDAO memberDAO;
    private ModelMapper modelMapper;

    MemberService() {
        memberDAO = new MemberDAO();
        modelMapper = MapperUtil.INSTANCE.getInstance();
    }

    public void addMember(MemberDTO memberDTO) throws SQLException {
        MemberVO memberVO = modelMapper.map(memberDTO, MemberVO.class);
        log.info(memberVO);
        memberDAO.insertMember(memberVO);
    }

    public List<MemberDTO> getAllMember() throws SQLException {
        List<MemberVO> memberVOList = memberDAO.selectAllMember();

        List<MemberDTO> memberDTOS = memberVOList.stream()
                .map(memberVO -> modelMapper.map(memberVO, MemberDTO.class)).toList();
        return memberDTOS;
    }

    public MemberDTO getOneMember(String carNum) throws SQLException {
        MemberVO memberVO = memberDAO.selectOneMember(carNum);

        if (memberVO == null) {
            log.info("회원 정보 없음: {}", carNum);
            return null;
        }

        return modelMapper.map(memberVO, MemberDTO.class);
    }

    public List<MemberDTO> getCarNum(String car4Num) throws Exception {
        List<MemberVO> memberVOList = memberDAO.selectCar4Num(car4Num);
        return memberVOList.stream()
                .map(vo -> modelMapper.map(vo, MemberDTO.class))
                .toList();
    }

    public void modifyMember(MemberDTO memberDTO) throws SQLException {
        MemberVO memberVO = modelMapper.map(memberDTO, MemberVO.class);
        memberDAO.updateMember(memberVO);
    }

    // ✅ 만료된 회원 subscribed → false 처리
    public void expireSubscriptions() throws SQLException {
        memberDAO.updateExpiredSubscriptions();
        log.info("만료된 월정액 회원 처리 완료");
    }

    // ✅ 월정액 갱신 (현재 endDate 다음날부터 1개월)
    public void renewSubscription(String carNum) throws SQLException {
        MemberVO memberVO = memberDAO.selectOneMember(carNum);
        if (memberVO == null) throw new SQLException("회원 없음: " + carNum);

        LocalDate baseDate = memberVO.getEndDate();

        // 만료됐으면 오늘 다음날부터, 구독중이면 종료일 다음날부터
        LocalDate newStart = (baseDate == null || baseDate.isBefore(LocalDate.now()))
                ? LocalDate.now().plusDays(1)
                : baseDate.plusDays(1);

        LocalDate newEnd = newStart.plusMonths(1);

        MemberVO updated = MemberVO.builder()
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

        memberDAO.updateSubscription(updated);
        log.info("월정액 갱신 완료: {} ({} ~ {})", carNum, newStart, newEnd);
    }


    public void removeMember(String carNum) throws SQLException {
        memberDAO.deleteMember(carNum);
    }
}
