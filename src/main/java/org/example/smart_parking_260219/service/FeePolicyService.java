package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dao.FeePolicyDAO;
import org.example.smart_parking_260219.dto.FeePolicyDTO;
import org.example.smart_parking_260219.vo.FeePolicyVo;
import org.modelmapper.ModelMapper;
import util.MapperUtil;

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
    private FeePolicyDTO toDto(FeePolicyVo vo) {
        return modelMapper.map(vo, FeePolicyDTO.class);
    }

    private FeePolicyVo toVo(FeePolicyDTO dto) {
        return modelMapper.map(dto, FeePolicyVo.class);
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
        // Dto -> vo
        FeePolicyVo feePolicyVo = toVo(feePolicyDTO);
        log.info("feePolicyVo : {}" , feePolicyVo);
        feePolicyDAO.insertPolicy(feePolicyVo);
    }

    // [목록 조회]
    public List<FeePolicyDTO> getPolicyList() {
        List<FeePolicyVo> voList = feePolicyDAO.selectAllPolicies();

        List<FeePolicyDTO> dtoList = new ArrayList<>();
        for (FeePolicyVo vo : voList) {
            dtoList.add(toDto(vo));
        }
        return dtoList;
    }

    // [상세 조회]
    public FeePolicyDTO getPolicy(int policyId) {
        if (policyId <= 0) {
            throw new IllegalArgumentException("policyId가 올바르지 않습니다.");
        }

        FeePolicyVo vo = feePolicyDAO.selectOnePolicy(policyId);
        return (vo == null) ? null : toDto(vo);
    }

    // 정책 수정 (성공 시 1, 실패 시 0 반환하도록)
    public int modifyPolicy(FeePolicyDTO feePolicyDTO) {
        log.info("modifyPolicy()");

        FeePolicyVo feePolicyVo = feePolicyDTO.toVo(); // DTO -> VO

        // 수정은 policyId가 필수
        if (feePolicyDTO.getPolicyId() <= 0) {
            throw new IllegalArgumentException("policyId가 필요합니다.");
        }

        if (feePolicyDTO.getDefaultTime() <= 0) {
            throw new IllegalArgumentException("defaultTime은 0보다 커야 합니다.");
        }

        if (feePolicyDTO.getDefaultFee() < 0) {
            throw new IllegalArgumentException("defaultFee는 0 이상이어야 합니다.");
        }

        return feePolicyDAO.updatePolicy(feePolicyVo);
    }

    // 정책 삭제(물리 삭제)
    public int removePolicy(int policyId) {
        log.info("removePolicy() policyId: {}", policyId);

        if (policyId <= 0) {
            throw new IllegalArgumentException("policyId가 올바르지 않습니다.");
        }

        return feePolicyDAO.deletePolicy(policyId);
    }


}
