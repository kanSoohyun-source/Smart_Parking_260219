-- fee_policy dummy
insert into fee_policy
    (grace_period, default_time, default_fee, extra_time, extra_fee, light_discount, disabled_discount, subscribed_fee, max_daily_fee, is_active, modify_date)
    VALUES (10, 10, 2000, 30, 1000, 0.3, 0.5, 100000, 15000, true, now());

-- member dummy -> DAO TEST 3회 실행

-- manager dummy

-- id - test01, password - 1111
insert into manager (manager_id, manager_name, password, email, active, role)
    VALUES ('test01', 'tester1', '$2a$12$4ReuaFjNvpNJlf/ZzjTC1u59qKSvH0kZcg0jS1tlPP/K8ubssv8Jq', 'rkdtngus2201@naver.com', true, 'NORMAL' );

-- id - test02, password - 2222
insert into manager (manager_id, manager_name, password, email, active, role)
VALUES ('test02', 'tester2', '$2a$12$qpF3i2uW2w2QU7CntypHc.16BflMqHe82EJIDIpoHXut3FwA3i9SS', 'rkdtngus2201@naver.com', true, 'NORMAL' );

-- id - test03, password - 3333
insert into manager (manager_id, manager_name, password, email, active, role)
VALUES ('test03', 'tester3', '$2a$12$sFsO6OAo1jiaqN7UteHpiOBWGjK3Bned0eVw8zZHuZarhiUzwI2ma', 'rkdtngus2201@naver.com', true, 'NORMAL' );

-- id - test04, password - 4444
insert into manager (manager_id, manager_name, password, email, active, role)
VALUES ('test04', 'tester4', '$2a$12$itHGFdg2TSJS.CBA.s8J1OFYbOwma2TIqjILAqziesegCuvKl9pEm', 'rkdtngus2201@naver.com', true, 'NORMAL' );

-- id - test05, password - 5555
insert into manager (manager_id, manager_name, password, email, active, role)
VALUES ('test05', 'tester5', '$$2a$12$EZ5MMMRpXbdUfwwTCRYO..WcWgzqOoAFQ3KjGFSEFa9rTA0Fb2I.K', 'rkdtngus2201@naver.com', true, 'NORMAL' );

-- parking dummy
-- 비회원
insert into parking (space_id, car_num, car_type, entry_time, phone)
    VALUES ('A2', '111가1111', 1, now(), '010-1111-2222');
insert into parking (space_id, car_num, car_type, entry_time, phone)
VALUES ('A17', '222나2222', 3, '2026-02-18', '010-2222-3333');
insert into parking (space_id, car_num, car_type, entry_time, phone)
VALUES ('A24', '333다3333', 4, now(), '010-3333-4444');

-- 회원


