package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dao.MemberDAO;
import org.example.smart_parking_260219.dto.MemberDTO;
import org.example.smart_parking_260219.util.MapperUtil;
import org.example.smart_parking_260219.vo.MemberVO;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
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
        return modelMapper.map(memberDAO.selectOneMember(carNum), MemberDTO.class);
    }

    public void modifyMember(MemberDTO memberDTO) throws SQLException {
        MemberVO memberVO = modelMapper.map(memberDTO, MemberVO.class);
        memberDAO.updateMember(memberVO);
    }

    public void removeMember(String carNum) throws SQLException {
        memberDAO.deleteMember(carNum);
    }


}
