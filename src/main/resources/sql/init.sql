# SQL문 임시 작성
-- 외래키, 기본키, 디폴트 값 지정 하지 않음.

CREATE DATABASE IF NOT EXISTS `smart_parking_team2`;

USE `smart_parking_team2`;

# member : 회원, 차량 정보 테이블
CREATE TABLE IF NOT EXISTS `member`
(
    `member_id`      INT AUTO_INCREMENT PRIMARY KEY comment '회원을 구분하기 위한 고유 식별자',
    `car_num`        VARCHAR(10) comment '차량번호(중복불가)',
    `car_type`       INT comment '차량번호 (1일반,2월정액,3경차,4장애인)',
    `name`           VARCHAR(20) comment '차량 운전자 이름',
    `phone`          VARCHAR(20) comment '운전자 연락처',
    `is_subscribed`  BOOLEAN comment '월정액 회원 여부',
    `sub_start_date` DATE comment '월정액 시작일 (일반회원=null)',
    `sub_end_date`   DATE comment '월정액 종료일 (일반회원=null)',
    `create_at`      DATETIME comment '차량(회원) 등록일',
    `is_active`      BOOLEAN comment '구독 활성화여부'
);

# manager : 관리자 정보 테이블
CREATE TABLE IF NOT EXISTS `manger`
(
    `manager_no`   INT AUTO_INCREMENT comment '관리자 일련번호',
    `manager_id`   INT comment '관리자 고유 식별자',
    `manager_name` VARCHAR(10) comment '관리자 이름',
    `password`     VARCHAR(20) comment '관리자 로그인 비밀번호',
    `email`        VARCHAR(50) comment '관리자 이메일(2차 인증)',
    `is_active`    BOOLEAN comment '사용여부'
);

# subscribe : 월정액 회원 정보 테이블
CREATE TABLE IF NOT EXISTS `subscribe`
(
    `subscription_id` INT AUTO_INCREMENT comment '관리자 일련번호',
    `car_num`         VARCHAR(10) comment '',
    `start_date`      DATE comment '',
    `end_date`        DATE comment '',
    `status`          BOOLEAN comment '',
    `last_update`     DATETIME comment ''
);

# car_park : 주차 공간 상태 테이블
# CREATE TABLE IF NOT EXISTS `car_park` (
# <<삭제된 것이 맞는지>>
# );

# parking : 입차, 출차, 요금 정보 테이블
CREATE TABLE IF NOT EXISTS `parking`
(
    `parking_id`      INT AUTO_INCREMENT comment '주차 기록 고유 ID',
    `member_id`       INT comment '주차한 회원 ID',
    `car_type`        INT comment '차량 유형 (1일반,2월정액,3경차,4장애인)',
    `car_num`         VARCHAR(10) comment '주차한 차량 번호',
    `space_id`        VARCHAR(10) comment '사용한 주차 공간 ID',
    `entry_time`      DATETIME comment '차량 입차 시간',
    `exit_time`       DATETIME comment '차량 출차 시간',
    `parking_minutes` INT comment '총 주차 시간(단위:분)',
    `is_paid`         BOOLEAN comment '결제 완료 여부'
);

# payment : 결제, 매출 정보 테이블
CREATE TABLE IF NOT EXISTS `payment`
(
    `payment_id`      INT AUTO_INCREMENT comment '결제 고유 ID',
    `parking_id`      INT comment '결제 대상 주차 기록 ID',
    `policy_id`       INT comment '요금 정책 고유 ID',
    `payment_type`    VARCHAR(10) comment '결제 방식 (카드/월정액)',
    `calculated_fee`  INT comment '할인 정책 전 계산된 요금',
    `discount_amount` INT comment '할인된 금액',
    `final_fee`       INT comment '실제 부과된 최종 요금',
    `paid_at`         DATETIME comment '결제 일시',
    `sales_date`      DATE comment '매출 집계 기준 날짜'
);

# fee_policy : 요금 부과 정책 테이블
CREATE TABLE IF NOT EXISTS `fee_policy`
(
    `policy_id`         INT AUTO_INCREMENT comment '요금 정책 고유 ID',
    `default_time`      INT comment '기본 요금이 적용되는 시간(단위:분)',
    `default_fee`       INT comment '기본 시간에 대한 요금',
    `extra_time`        INT comment '추가 요금 부과 시간 단위',
    `extra_fee`         INT comment '추가 시간당 요금',
    `light_discount`    DOUBLE comment '경차 할인율',
    `disabled_discount` DOUBLE comment '장애인 할인율',
    `max_daily_fee`     INT comment '하루 최대 요금 한도',
    `modify_date`       DATETIME comment '수정 날짜'
);