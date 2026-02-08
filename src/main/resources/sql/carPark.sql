GRANT ALL PRIVILEGES ON `parking_system`.* TO 'todo_user1'@'localhost';

CREATE TABLE parking (
    parking_id INT        AUTO_INCREMENT                PRIMARY KEY COMMENT '주차 기록 고유 ID',
    car_num    VARCHAR(10)                  NOT NULL                COMMENT '차량 번호',
    member_id  INT                          NULL                    COMMENT '주차한 회원 ID',
    space_id   VARCHAR(10)                  NULL                    COMMENT '주차 구역 id',
    entry_time DATETIME                     NULL                    COMMENT '입차 시간',
    exit_time  DATETIME                     NULL                    COMMENT '출차 시간',
    total_time INT                          NULL                    COMMENT '총 주차 시간',
    car_type   INT        DEFAULT 1         NULL                    COMMENT '차량 종류(1 -> 일반, 2 -> 월정액, 3 -> 경차, 4 -> 장애인)',
    is_paid    BOOLEAN    DEFAULT FALSE     NULL                    COMMENT '결제 여부'
);

CREATE TABLE parking_spot (
    space       VARCHAR(10)                 NOT NULL    PRIMARY KEY COMMENT '주차 공간 구역',
    state       BOOLEAN     DEFAULT FALSE   NOT NULL                COMMENT 'false: 빈자리, true: 사용중',
    car_num     VARCHAR(10)                 NULL                    COMMENT '차 번호',
    last_update DATETIME                    NOT NULL                COMMENT '마지막으로 정보가 갱신된 시간'
);