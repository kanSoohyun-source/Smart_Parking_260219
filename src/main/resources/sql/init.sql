CREATE DATABASE IF NOT EXISTS parking_system;

CREATE USER 'admin' IDENTIFIED BY '1231';

USE parking_system;

CREATE TABLE MEMBERS (
                         member_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '회원을 구분하기 위한 고유 식별자',
                         car_num VARCHAR(10) NOT NULL COMMENT '차량 번호 (중복 불가)',
                         car_type INT NOT NULL COMMENT '차량 유형 (1-일반/2-월정액/3-경차/4-장애인)',
                         name VARCHAR(20) NOT NULL COMMENT '차량 운전자 이름',
                         phone VARCHAR(20) NOT NULL COMMENT '운전자 연락처',
                         is_subscribed BOOLEAN NOT NULL COMMENT '월정액 회원 여부',
                         sub_start_date DATE DEFAULT NULL COMMENT '월정액 시작일 (일반 회원은 NULL)',
                         sub_end_date DATE DEFAULT NULL COMMENT '월정액 종료일 (일반 회원은 NULL)',
                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '차량(회원) 등록일',
                         is_active BOOLEAN NOT NULL DEFAULT FALSE COMMENT '구독 활성화 여부(default false)',
                         UNIQUE KEY unique_car_num (car_num)
);

CREATE TABLE MANAGER (
                         manager_no INT PRIMARY KEY AUTO_INCREMENT COMMENT '관리자 일련번호',
                         manager_id INT NOT NULL COMMENT '관리자 고유 식별자',
                         manager_name VARCHAR(10) NOT NULL COMMENT '관리자 이름',
                         password VARCHAR(20) NOT NULL COMMENT '관리자 로그인 비밀번호',
                         email VARCHAR(50) NOT NULL COMMENT '관리자 이메일(2차 인증용)',
                         is_active BOOLEAN NOT NULL DEFAULT false COMMENT '사용 여부(defalt false)'
);

CREATE TABLE Subscribe (
                           subscription_id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'PK, auto increment',
                           car_num VARCHAR(10) NOT NULL COMMENT 'FK(members), not null',
                           start_date DATE NULL COMMENT '구독 시작일',
                           end_date DATE NULL COMMENT '구독 종료일',
                           status BOOLEAN NOT NULL DEFAULT true COMMENT '구독 상태 (default true)',
                           last_update DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '주차 공간 상태가 마지막으로 변경된 시간',
                           FOREIGN KEY (car_num) REFERENCES Members(car_num)
);