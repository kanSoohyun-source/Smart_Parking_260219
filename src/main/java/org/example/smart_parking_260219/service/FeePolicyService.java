package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.connection.DBConnection;
import org.example.smart_parking_260219.dao.FeePolicyDAO;
import org.example.smart_parking_260219.dto.FeePolicyDTO;
import org.example.smart_parking_260219.vo.FeePolicyVO;
import org.modelmapper.ModelMapper;
import org.example.smart_parking_260219.util.MapperUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class FeePolicyService {
    private final FeePolicyDAO feePolicyDAO = FeePolicyDAO.getInstance();
    private final ModelMapper modelMapper = MapperUtil.INSTANCE.getInstance();

    private static FeePolicyService instance;
    private FeePolicyService() {}
    public static FeePolicyService getInstance() {
        if (instance == null) {
            instance = new FeePolicyService();
        }
        return instance;
    }

    // ModelMapper 사용 변환
    private FeePolicyDTO toDto(FeePolicyVO vo) {
        return modelMapper.map(vo, FeePolicyDTO.class);
    }

    private FeePolicyVO toVo(FeePolicyDTO dto) {
        return modelMapper.map(dto, FeePolicyVO.class);
    }

    // 신규 추가: 월정액 요금 조회
    public Integer getSubscribedFee() throws Exception {
        log.info("getSubscribedFee() 호출");
        try (Connection connection = DBConnection.INSTANCE.getConnection()) {
            return feePolicyDAO.getSubscribedFee(connection);
        }
    }

    public void addPolicy(FeePolicyDTO feePolicyDTO) {
        log.info("addPolicy()");


        if (feePolicyDTO.getDefaultTime() <= 0) {
            throw new IllegalArgumentException("defaultTime은 0보다 커야 합니다.");
        }

        if (feePolicyDTO.getDefaultFee() < 0) {
            throw new IllegalArgumentException("defaultFee는 0 이상이어야 합니다.");
        }

        if (feePolicyDTO.getExtraFee() < 0) {
            throw new IllegalArgumentException("extraFee는 0 이상이어야 합니다.");
        }

        if (feePolicyDTO.getGracePeriod() < 0) {
            throw new IllegalArgumentException("gracePeriod는 0 이상이어야 합니다.");
        }

        feePolicyDAO.deactivateAllPolicies();  // 기존 정책 전부 false

        // Dto -> vo
        FeePolicyVO originalVo = toVo(feePolicyDTO);

        FeePolicyVO feePolicyVo = FeePolicyVO.builder()
                .policyId(originalVo.getPolicyId())
                .gracePeriod(originalVo.getGracePeriod())
                .defaultTime(originalVo.getDefaultTime())
                .defaultFee(originalVo.getDefaultFee())
                .extraTime(originalVo.getExtraTime())
                .extraFee(originalVo.getExtraFee())
                .lightDiscount(originalVo.getLightDiscount())
                .disabledDiscount(originalVo.getDisabledDiscount())
                .subscribedFee(originalVo.getSubscribedFee())
                .maxDailyFee(originalVo.getMaxDailyFee())
                .isActive(true)
                .modifyDate(originalVo.getModifyDate())
                .build();
        log.info("feePolicyVo : {}" , feePolicyVo);
        feePolicyDAO.insertPolicy(feePolicyVo);
    }

    // [목록 조회]
    public List<FeePolicyDTO> getPolicyList() {
        List<FeePolicyVO> voList = feePolicyDAO.selectAllPolicies();

        List<FeePolicyDTO> dtoList = new ArrayList<>();
        for (FeePolicyVO vo : voList) {
            dtoList.add(toDto(vo));
        }
        return dtoList;
    }

    // [상세 조회]
    public FeePolicyDTO getPolicy() {
        FeePolicyVO feePolicyVO = feePolicyDAO.selectOnePolicy();

        if (feePolicyVO == null) {
            return null; // 또는 throw new IllegalStateException("활성 정책이 없습니다.");
        }
        return toDto(feePolicyVO);
    }

    public FeePolicyDTO getPolicyById(int policyId) throws Exception {
        // DAO를 통해 DB에서 해당 ID의 정책 데이터를 조회해 옵니다.
        // 예: return feePolicyDAO.selectOne(id);

        // 현재는 전체 리스트에서 필터링하는 예시입니다.
        return getPolicyList().stream()
                .filter(policy -> policy.getPolicyId() == policyId)
                .findFirst()
                .orElse(null);
    }

    public void applyPolicy(int id) throws Exception {
        // 1. 현재 활성화된 모든 정책을 꺼버림 (작성하신 메서드 호출)
        feePolicyDAO.deactivateAllPolicies();

        // 2. 선택한 특정 ID의 정책만 켬 (새로 추가한 메서드 호출)
        feePolicyDAO.activatePolicy(id);

        log.info(id + "번 요금 정책이 현재 정책으로 적용되었습니다.");
    }
}
