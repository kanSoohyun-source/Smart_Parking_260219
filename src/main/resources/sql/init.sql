CREATE DATABASE IF NOT EXISTS `smart_parking_team2`;

USE `smart_parking_team2`;

# member : 회원, 차량 정보 테이블 [완료]
CREATE TABLE IF NOT EXISTS `member`
(
    `member_id`   INT AUTO_INCREMENT PRIMARY KEY COMMENT '회원 고유 식별자',
    `car_num`     VARCHAR(20) NOT NULL UNIQUE COMMENT '차량번호(중복불가, 공백제거)',
    `car_type`    TINYINT     NOT NULL COMMENT '차량유형 (1:일반, 2:월정액대상, 3:경차, 4:장애인)',
    `name`        VARCHAR(20) NOT NULL COMMENT '운전자 이름',
    `phone`       VARCHAR(20) NOT NULL COMMENT '연락처',
    `create_date` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가입일',
    `subscribed`  BOOLEAN     NOT NULL DEFAULT FALSE COMMENT '현재 월정액 구독 중인지 여부'
);

# manager : 관리자 정보 테이블 [완료]
CREATE TABLE IF NOT EXISTS `manager`
(
    `manager_no`   INT AUTO_INCREMENT PRIMARY KEY COMMENT '관리자 시스템 내부 번호',
    `manager_id`   VARCHAR(50)  NOT NULL UNIQUE COMMENT '로그인 아이디 (중복 불가)',
    `manager_name` VARCHAR(20)  NOT NULL COMMENT '관리자 이름',
    `password`     VARCHAR(255) NOT NULL COMMENT '관리자 비밀번호 (해시 암호화)',
    `email`        VARCHAR(100) NOT NULL COMMENT '관리자 이메일(2차 인증)',
    `active`       BOOLEAN      NOT NULL DEFAULT TRUE COMMENT '계정 활성화 여부'
);

# subscribe : 월정액 회원 정보 테이블 [완료]
CREATE TABLE IF NOT EXISTS `subscribe`
(
    `subscription_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '월정액 이력 고유 ID',
    `member_id`       INT     NOT NULL COMMENT '구독한 회원 ID (FK)',
    `start_date`      DATE    NOT NULL COMMENT '월정액 시작일',
    `end_date`        DATE    NOT NULL COMMENT '월정액 종료일',
    `status`          BOOLEAN NOT NULL DEFAULT TRUE COMMENT '월정액 유효 여부',
    `payment_amount`  INT     NOT NULL COMMENT '결제 금액',
    `last_update`     DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '결제일',

    CONSTRAINT `fk_subscribe_member` FOREIGN KEY (`member_id`)
        REFERENCES `member` (`member_id`) ON DELETE CASCADE
);

# parking_spot : 주차 공간 상태 테이블 [완료]
CREATE TABLE IF NOT EXISTS `parking_spot`
(
    `space_id`    VARCHAR(5) PRIMARY KEY COMMENT '주차 공간 번호(A1, B2...)',
    `empty`       BOOLEAN NOT NULL DEFAULT TRUE COMMENT '빈 공간 여부(True:비어있음, False:주차중)',
    `car_num`     VARCHAR(10)      DEFAULT NULL COMMENT '현재 주차된 차량 번호(없으면 NULL)',
    `last_update` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '상태 변경일'
);

# parking : 입차, 출차, 요금 정보 테이블 [완료]
CREATE TABLE IF NOT EXISTS `parking`
(
    `parking_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '주차 기록 ID',
    `member_id`  INT                  DEFAULT NULL COMMENT '회원 ID (비회원일 경우 NULL)',
    `space_id`   VARCHAR(20) NOT NULL COMMENT '주차 공간 ID (FK)',
    `car_num`    VARCHAR(20) NOT NULL COMMENT '차량 번호 (스냅샷)',
    `car_type`   TINYINT     NOT NULL COMMENT '차량 유형 (스냅샷)',
    `entry_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '입차 시간',
    `exit_time`  DATETIME             DEFAULT NULL COMMENT '출차 시간 (주차중이면 NULL)',
    `total_time` INT                  DEFAULT 0 COMMENT '주차 시간(분)',
    `paid`       BOOLEAN              DEFAULT FALSE COMMENT '정산 완료 여부',

    CONSTRAINT `fk_parking_member` FOREIGN KEY (`member_id`)
        REFERENCES `member` (`member_id`) ON DELETE SET NULL,
    CONSTRAINT `fk_parking_spot` FOREIGN KEY (`space_id`)
        REFERENCES `parking_spot` (`space_id`) ON UPDATE CASCADE
);

# payment : 결제, 매출 정보 테이블 [완료]
CREATE TABLE IF NOT EXISTS `payment`
(
    `payment_id`      INT AUTO_INCREMENT PRIMARY KEY COMMENT '결제 ID',
    `parking_id`      INT      NOT NULL COMMENT '주차 기록 ID (FK)',
    `policy_id`       INT               DEFAULT NULL COMMENT '적용된 요금 정책 ID (FK)',
    `payment_type`    TINYINT  NOT NULL COMMENT '결제 수단 (1:카드, 2:현금, 3:월정액차감)',
    `calculated_fee`    INT      NOT NULL DEFAULT 0 COMMENT '할인 전 요금',
    `discount_amount` INT      NOT NULL DEFAULT 0 COMMENT '총 할인 금액',
    `final_fee`       INT      NOT NULL DEFAULT 0 COMMENT '최종 결제 금액',
    `payment_date`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '실제 결제 일시',

    CONSTRAINT `fk_payment_parking` FOREIGN KEY (`parking_id`)
        REFERENCES `parking` (`parking_id`),
    CONSTRAINT `fk_payment_policy` FOREIGN KEY (`policy_id`)
        REFERENCES `fee_policy` (`policy_id`)
);

# fee_policy : 요금 부과 정책 테이블 [완료]
CREATE TABLE IF NOT EXISTS `fee_policy`
(
    `policy_id`         INT AUTO_INCREMENT PRIMARY KEY COMMENT '요금 정책 고유 ID',
    `grace_period`      INT     NOT NULL DEFAULT 10 COMMENT '무료 회차 시간(분) - 이 시간 내 출차 시 0원',
    `default_time`      INT     NOT NULL DEFAULT 60 COMMENT '기본 요금 적용 시간(분) - 60분',
    `default_fee`       INT     NOT NULL DEFAULT 2000 COMMENT '기본 요금 - 2,000원',
    `extra_time`        INT     NOT NULL DEFAULT 30 COMMENT '추가 요금 단위 시간(분) - 30분',
    `extra_fee`         INT     NOT NULL DEFAULT 1000 COMMENT '추가 요금 - 1,000원',
    `light_discount`    DOUBLE  NOT NULL DEFAULT 0.3 COMMENT '경차 할인율(0.3 = 30%)',
    `disabled_discount` DOUBLE  NOT NULL DEFAULT 0.5 COMMENT '장애인 할인율(0.5 = 50%)',
    `subscribed_fee`    INT     NOT NULL DEFAULT 100000 COMMENT '월정액 가격 - 100,000원',
    `max_daily_fee`     INT     NOT NULL DEFAULT 15000 COMMENT '일일 최대 요금 - 15,000원',
    `is_active`         BOOLEAN NOT NULL DEFAULT TRUE COMMENT '현재 정책 활성화 여부',
    `modify_date`       DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '정책 수정일'
);