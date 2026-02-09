package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dao.PaymentDAO;
import org.example.smart_parking_260219.dto.PaymentDTO;
import org.example.smart_parking_260219.util.MapperUtil;
import org.example.smart_parking_260219.vo.PaymentVO;
import org.modelmapper.ModelMapper;

import java.util.List;

@Log4j2

public enum PaymentService {
    INSTANCE;

    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final ModelMapper modelMapper = MapperUtil.INSTANCE.getInstance();
    // private final ParkingDAO parkingDAO = new ParkingDAO();
    // private final FeePolicyDAO feePolicyDAO = new FeePolicyDAO();

    // 결제 등록
    // 사용자가 결제한 내역을 등록하고, 주차 상태를 '정산 완료'로 변경
    public void addPayment(PaymentDTO paymentDTO) throws Exception {
        log.info("Service: addPayment 호출 - 차량번호: " + paymentDTO.getCarNum());

        // 1. 차량 번호로 현재 주차 중인 parking_id 조회 (필수 로직)
        // int parkingId = parkingDAO.selectIdByCarNum(paymentDTO.getCarNum());
        // if(parkingId == 0) throw new Exception("주차 중인 차량이 아닙니다.");

        // 2. 요금 재계산 (보안상 서버에서 한 번 더 계산하는 것이 정석)
        // int calculatedFee = calculateFeeLogic(parkingId, paymentDTO.getPolicyId());
        // paymentDTO.setCalculatedFee(calculatedFee);

        // 3. 결제 수단 변환 (String "카드" -> int 1)
        int paymentTypeInt = paymentDTO.getPaymentType();

        // 4. DTO -> VO 변환 (DB 저장을 위한 객체 생성)
        PaymentVO paymentVO = PaymentVO.builder()
                .parkingId(paymentDTO.getParkingId()) // 주의: DTO 필드 설계상 parkingId가 필요함 (임시로 paymentNo 사용 매핑 시 주의)
                .policyId(paymentDTO.getPolicyId())
                .carNum(paymentDTO.getCarNum())
                .paymentType(paymentTypeInt)
                .calculatedFee(paymentDTO.getCalculatedFee())
                .discountAmount(paymentDTO.getDiscountAmount())
                .finalFee(paymentDTO.getFinalFee())
                .build();

        // 5. DAO 호출하여 DB 저장
        paymentDAO.insertPayment(paymentVO);
        log.info("Service: 결제 등록 완료");
    }

     // 결제 전체 목록 조회
     // 관리자 페이지 등에서 보여줄 전체 결제 내역
    public List<PaymentDTO> getPaymentList() {
        log.info("Service: getPaymentList 호출");

        // 1. DAO에서 전체 VO 목록 가져오기
        List<PaymentVO> paymentVOList = paymentDAO.selectAllPayments();

        // 2. VO List -> DTO List 변환 (Stream API 사용)
        List<PaymentDTO> paymentDTOList = paymentVOList.stream()
                .map(paymentVO -> modelMapper.map(paymentVO, PaymentDTO.class)).toList();
        return paymentDTOList;
    }


     // 결제 상세 조회
     // 특정 결제 건에 대한 상세 정보

    public PaymentDTO getPayment(int paymentNo) {
        log.info("Service: getPayment - ID: " + paymentNo);

        return modelMapper.map( paymentDAO.selectOnePayment(paymentNo), PaymentDTO.class);
    }

    // 내부 사용 메서드

    // 요금 계산
    // 주차 시간과 요금 정책을 기반으로 '할인 전 총 요금(calculatedFee)'을 계산
    /*
    private int calculateFeeLogic(int parkingId, int policyId) {
        log.info("calculateFeeLogic - parkingId: " + parkingId);

        // 1. DB에서 데이터 조회 (DAO가 구현되어 있다고 가정)
        // ParkingVO parkingVO = parkingDAO.selectOne(parkingId);
        // FeePolicyVO policyVO = feePolicyDAO.selectOne(policyId);

        // 정책 데이터 (DB fee_policy 테이블 기준)
        int gracePeriod = policyVO.getGracePeriod();   // 무료 회차 (10분)
        int defaultTime = policyVO.getDefaultTime();   // 기본 시간 (60분)
        int defaultFee = policyVO.getDefaultFee();     // 기본 요금 (2000원)
        int extraTime = policyVO.getExtraTime();       // 추가 단위 시간 (30분)
        int extraFee = policyVO.getExtraFee();         // 추가 요금 (1000원)
        int maxDailyFee = policyVO.getMaxDailyFee();   // 일일 최대 요금 (15000원)
        // --------------------------------------------------------------------

        // 2. 주차 시간(분) 계산

        // Duration.between(과거, 미래) -  두 시간 사이의 간격(차이)을 아주 쉽게 구해주는 도구
        // LocalDateTime, LocalTime, Instant 등 Java 8 날짜 타입끼리만 비교 가능

        // .toMinutes() -> 결과를 '분'으로 변환
        long totalMinutes = java.time.Duration.between(entryTime, exitTime).toMinutes();
        log.info("totalMinutes: " + totalMinutes);

        // 3. 무료 회차 시간 이내인가?
        if (totalMinutes <= gracePeriod) {
            return 0; // 0원 리턴
        }

        // 4. 기본 요금 계산
        int totalFee = defaultFee; // 일단 기본요금 깔고 시작

        // 5. 추가 요금 계산 (기본 시간 초과 시)
        if (totalMinutes > defaultTime) {
            long extraMinutes = totalMinutes - defaultTime; // 초과된 분 (예: 90분 주차 -> 30분 초과)

            // 올림 처리 중요 (예: 1분이라도 넘으면 30분 요금 부과)
            // Math.ceil -> (올림 처리)
            // Math.ceil((double) 31 / 30) => 2.0 단위 -> 2 * 1000원
            int units = (int) Math.ceil((double) extraMinutes / extraTime);

            totalFee += (units * extraFee);
        }

        // 6. 일일 최대 요금 상한선 적용
        if (totalFee > maxDailyFee) {
            totalFee = maxDailyFee;
        }

        return totalFee;
    }
     */

    // 할인 금액 계산
    // 차량 타입과 정책을 기반으로 할인될 금액을 계산

//    private int calculateDiscountLogic(int totalFee, String carType, FeePolicyVO policyVO) {
//        double discountRate = 0.0;
//
//        // DB에 car_type이 1(경차), 2(장애인), 3(일반) 등으로 저장되어 있다고 가정
//        // 혹은 String으로 "경차"라고 들어온다고 가정
//        if ("1".equals(carType)) {
//            discountRate = policyVO.getLightDiscount(); // 0.3 (30%)
//        } else if ("2".equals(carType)) {
//            discountRate = policyVO.getDisabledDiscount(); // 0.5 (50%)
//        }
//
//        // 할인 금액 계산 (십원 단위 절삭 등은 정책에 따라 추가)
//        return (int) (totalFee * discountRate);
//    }

}
