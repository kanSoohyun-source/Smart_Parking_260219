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
    public void addPayment(PaymentDTO paymentDTO) throws Exception {
        log.info("Service: addPayment 호출 - 차량번호: " + paymentDTO.getCarNum());

        // 1. 차량 정보 한 번만 조회 (NPE 방지를 위해 변수에 담기)
        ParkingVO parkingVO = parkingDAO.selectParkingByCarNum(paymentDTO.getCarNum());

        if (parkingVO == null || parkingVO.getParkingId() == 0) {
            throw new Exception("주차 중인 차량이 아닙니다.");
        }

        int parkingId = parkingVO.getParkingId();

        // 3. DTO -> VO 변환 (Builder로 직접 생성하며 요금 주입)
        PaymentVO paymentVO = PaymentVO.builder()
                .parkingId(parkingId) // 혹은 paymentDTO.getParkingId()
                .policyId(paymentDTO.getPolicyId())
                .carNum(paymentDTO.getCarNum())
                .paymentType(paymentDTO.getPaymentType())
                .calculatedFee(paymentDTO.getCalculatedFee()) // 계산된 요금 주입
                .discountAmount(paymentDTO.getDiscountAmount()) // 계산된 할인액 적용
                .finalFee(paymentDTO.getFinalFee()) // 최종 요금 계산
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
    public PaymentDTO getPayment(int parkingId) {
        log.info("Service: getPayment - ID: " + parkingId);

        return modelMapper.map(paymentDAO.selectOnePayment(parkingId), PaymentDTO.class);
    }

    // 요금 계산
    // 주차 시간과 요금 정책을 기반으로 '할인 전 총 요금(calculatedFee)'을 계산
    /*
     * 입차 시점부터 24시간 단위로 구간을 나누어 요금을 합산
     * 예: 10일 16시 입차 ~ 11일 10시 출차 (18시간 주차) -> 24시간 미만이므로 최대 15,000원
     */
    public int calculateFeeLogic(ParkingDTO parkingDTO) {
        log.info("calculateFeeLogic (24시간 주기 합산) - parkingId: " + parkingDTO.getParkingId());

        ParkingVO parkingVO = parkingDAO.selectParkingByParkingId(parkingDTO.getParkingId());
        FeePolicyVO policyVO = feePolicyDAO.selectOnePolicy();

        LocalDateTime entryTime = parkingVO.getEntryTime();
        LocalDateTime exitTime = (parkingVO.getExitTime() != null) ? parkingVO.getExitTime() : LocalDateTime.now();

        int totalAccumulatedFee = 0;
        LocalDateTime currentStart = entryTime;

        // 1. 입차 시간으로부터 24시간씩 더해가며 구간을 나눔
        // currentStart에 24시간을 더한 시간이 exitTime보다 이전인 동안 반복
        while (currentStart.plusDays(1).isBefore(exitTime)) {
            LocalDateTime endOfCycle = currentStart.plusDays(1);

            // 24시간 꽉 채운 구간에 대해 요금 계산 (대부분 일일 최대 요금이 적용됨)
            totalAccumulatedFee += calculateSingleDayFee(currentStart, endOfCycle, policyVO);

            // 시작 지점을 24시간 뒤로 이동
            currentStart = endOfCycle;
        }

        // 2. 마지막 남은 자투리 시간(24시간 미만 구간) 요금 합산
        totalAccumulatedFee += calculateSingleDayFee(currentStart, exitTime, policyVO);

        log.info("최종 합산 요금 (24시간 기준): " + totalAccumulatedFee);
        return totalAccumulatedFee;
    }

    // 할인 금액 계산
    // 차량 타입과 정책을 기반으로 할인될 금액을 계산
    public int calculateDiscountLogic(int totalFee, int carType, FeePolicyVO policyVO) {
        double discountRate = 0.0;

        // carType -> 1:일반, 2:월정액대상, 3:경차, 4:장애인

        if (carType == 2) { // 월정액일 경우
            discountRate = 1.0; // 100% 할인 (무료)
        } else if (3 == carType) { // 경차일 경우
            discountRate = policyVO.getLightDiscount(); // 0.3 (30%)
        } else if (4 == carType) { // 장애인일 경우
            discountRate = policyVO.getDisabledDiscount(); // 0.5 (50%)
        }

        // 할인 금액 계산
        return (int) (totalFee * discountRate);
    }

    // 내부 사용 메서드
    // 지정된 시간 구간(start ~ end)에 대한 요금을 계산, 일일 최대 요금 정책을 적용

    private int calculateSingleDayFee(LocalDateTime start, LocalDateTime end, FeePolicyVO policyVO) {
        // 두 시간 사이의 차이(분)를 구함
        long minutes = java.time.Duration.between(start, end).toMinutes();

        // 무료 회차 - 주차 시간이 정책상 무료 시간(예: 10분) 이하이면 0원 반환
        if (minutes <= policyVO.getGracePeriod()) return 0;

        // 기본 요금 - 일단 기본 요금(예: 2000원)부터 시작
        int fee = policyVO.getDefaultFee();

        // 추가 요금 - 주차 시간이 기본 시간(예: 60분)을 초과했을 경우
        if (minutes > policyVO.getDefaultTime()) {
            long extraMinutes = minutes - policyVO.getDefaultTime();

            // 초과된 시간을 단위 시간(예: 30분)으로 나누어 올림 처리 (1분만 넘어도 1단위 추가)
            int units = (int) Math.ceil((double) extraMinutes / policyVO.getExtraTime());

            // 단위당 추가 요금(예: 1000원)을 곱해서 더함
            fee += (units * policyVO.getExtraFee());
        }
        /*
        최대 요금 제한
        계산된 요금이 해당 날짜의 최대 요금(예: 15000원)을 넘으면 최대 요금만 받음.
        Math.min(A, B)는 둘 중 작은 값을 선택
         */
        return Math.min(fee, policyVO.getMaxDailyFee());
    }
}