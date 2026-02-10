package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dao.FeePolicyDAO;
import org.example.smart_parking_260219.dao.ParkingDAO;
import org.example.smart_parking_260219.dao.ParkingDAOImpl;
import org.example.smart_parking_260219.dao.PaymentDAO;
import org.example.smart_parking_260219.dto.FeePolicyDTO;
import org.example.smart_parking_260219.dto.ParkingDTO;
import org.example.smart_parking_260219.dto.PaymentDTO;
import org.example.smart_parking_260219.util.MapperUtil;
import org.example.smart_parking_260219.vo.FeePolicyVO;
import org.example.smart_parking_260219.vo.ParkingVO;
import org.example.smart_parking_260219.vo.PaymentVO;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2

public enum PaymentService {
    INSTANCE;

    private final PaymentDAO paymentDAO;
    private final ModelMapper modelMapper;
    private final ParkingDAO parkingDAO = new ParkingDAOImpl();
    private final FeePolicyDAO feePolicyDAO = new FeePolicyDAO();

    PaymentService() {
        paymentDAO = new PaymentDAO();
        modelMapper = MapperUtil.INSTANCE.getInstance();
    }

    // 결제 등록
    // 사용자가 결제한 내역을 등록하고, 주차 상태를 '정산 완료'로 변경
    public void addPayment(PaymentDTO paymentDTO, FeePolicyDTO feePolicyDTO, ParkingDTO parkingDTO) throws Exception {
        log.info("Service: addPayment 호출 - 차량번호: " + paymentDTO.getCarNum());

        // 1. 차량 정보 한 번만 조회 (NPE 방지를 위해 변수에 담기)
        ParkingVO parkingVO = parkingDAO.selectParkingByCarNum(paymentDTO.getCarNum());

        if (parkingVO == null || parkingVO.getParkingId() == 0) {
            throw new Exception("주차 중인 차량이 아닙니다.");
        }

        int parkingId = parkingVO.getParkingId();

        // 2-1. 요금 계산
        int calculatedFee = calculateFeeLogic(parkingDTO);

        // 2-2. 할인 금액 계산 (이미 위에서 가져온 parkingVO 사용)
        int discountAmount = calculateDiscountLogic(calculatedFee,
                parkingVO.getCarType(),
                modelMapper.map(feePolicyDTO, FeePolicyVO.class));

        // 3. DTO -> VO 변환 (Builder로 직접 생성하며 요금 주입)
        PaymentVO paymentVO = PaymentVO.builder()
                .parkingId(parkingId) // 혹은 paymentDTO.getParkingId()
                .policyId(paymentDTO.getPolicyId())
                .carNum(paymentDTO.getCarNum())
                .paymentType(paymentDTO.getPaymentType())
                .calculatedFee(calculatedFee) // 계산된 요금 주입
                .discountAmount(discountAmount) // 계산된 할인액 적용
                .finalFee(calculatedFee - discountAmount) // 최종 요금 계산
                .build();

        // 4. DAO 호출하여 DB 저장
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

        return modelMapper.map(paymentDAO.selectOnePayment(paymentNo), PaymentDTO.class);
    }

    // 내부 사용 메서드

    // 요금 계산
    // 주차 시간과 요금 정책을 기반으로 '할인 전 총 요금(calculatedFee)'을 계산
    private int calculateFeeLogic(ParkingDTO parkingDTO) {
        log.info("calculateFeeLogic - parkingId: " + parkingDTO.getParkingId());

        // 1. DB에서 데이터 조회 (DAO가 구현되어 있다고 가정)
        ParkingVO parkingVO = parkingDAO.selectParkingByParkingId(parkingDTO.getParkingId());
        FeePolicyVO policyVO = feePolicyDAO.selectOnePolicy();

        // 정책 데이터 (DB fee_policy 테이블 기준)
        int gracePeriod = policyVO.getGracePeriod();   // 무료 회차 (10분)
        int defaultTime = policyVO.getDefaultTime();   // 기본 시간 (60분)
        int defaultFee = policyVO.getDefaultFee();     // 기본 요금 (2000원)
        int extraTime = policyVO.getExtraTime();       // 추가 단위 시간 (30분)
        int extraFee = policyVO.getExtraFee();         // 추가 요금 (1000원)
        int maxDailyFee = policyVO.getMaxDailyFee();   // 일일 최대 요금 (15000원)

        // 2. 주차 시간(분) 계산
        // Duration.between(과거, 미래) -  두 시간 사이의 간격(차이)을 아주 쉽게 구해주는 도구
        // LocalDateTime, LocalTime, Instant 등 Java 8 날짜 타입끼리만 비교 가능
        // .toMinutes() -> 결과를 '분'으로 변환
        // 입출차 시간 계산
        LocalDateTime exitTime = parkingVO.getExitTime() != null ? parkingVO.getExitTime() : LocalDateTime.now();
        long totalMinutes = java.time.Duration.between(parkingVO.getEntryTime(), exitTime).toMinutes();
        log.info("totalMinutes: " + totalMinutes);

        // 3. 무료 회차 시간
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

    // 할인 금액 계산
    // 차량 타입과 정책을 기반으로 할인될 금액을 계산
    private int calculateDiscountLogic(int totalFee, int carType, FeePolicyVO policyVO) {
        double discountRate = 0.0;

        // DB에 car_type이 1(경차), 2(장애인), 3(일반) 등으로 저장

        if (1 == carType) {
            discountRate = policyVO.getLightDiscount(); // 0.3 (30%)
        } else if (2 == carType) {
            discountRate = policyVO.getDisabledDiscount(); // 0.5 (50%)
        }

        // 할인 금액 계산
        return (int) (totalFee * discountRate);
    }
}