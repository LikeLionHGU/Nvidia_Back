-- =========================
-- Address
-- =========================
insert into address (id, latitude, longitude, road_name) values
                                                             (1, 37.5665, 126.9780, '서울특별시 중구 세종대로 110'),
                                                             (2, 35.1796, 129.0756, '부산광역시 중구 중앙대로 100'),
                                                             (3, 36.3504, 127.3845, '대전광역시 서구 둔산대로 100'),
                                                             (4, 37.4563, 126.7052, '인천광역시 남동구 인주대로 100'),
                                                             (5, 35.8714, 128.6014, '대구광역시 중구 공평로 100');

-- =========================
-- Room
-- =========================
insert into room (id, en_name, en_phone_number, address_id, account, max_people, price, memo)
values
    (100, '세종스터디룸', '010-1111-2222', 1, '신한 110-123-456789', 6, 15000, '종로 중심, 조용한 분위기'),
    (101, '해운대미팅룸', '010-2222-3333', 2, '국민 333-222-111111', 10, 25000, '바다 근처, 채광 좋음'),
    (102, '둔산협업룸', '010-3333-4444', 3, '우리 1002-555-666666', 8, 20000, '협업 친화적 레이아웃'),
    (103, '인주프라이빗룸', '010-4444-5555', 4, '농협 302-7777-888888', 4, 12000, '프라이빗, 방음 좋음'),
    (104, '공평크리에이티브', '010-5555-6666', 5, '카카오 3333-99-000000', 12, 30000, '창의적인 무드, 장비 완비');

-- =========================
-- Room.chipList (Element Collection)
-- (Room 엔티티의 chipList가 enum 이름 그대로 저장된다고 하셨으므로 enum name 그대로 입력)
-- =========================
-- insert into room_chip_list (room_id, chip_list) values
--                                                     (100, '조용한'),
--                                                     (100, '집중하기_좋은'),
--                                                     (101, '채광_좋은'),
--                                                     (101, '개방적인'),
--                                                     (102, '협업_친화적'),
--                                                     (102, '모던한'),
--                                                     (103, '프라이빗한'),
--                                                     (103, '방음_좋은'),
--                                                     (104, '창의적인'),
--                                                     (104, '장비_완비'),
--                                                     (104, '트렌디한');

-- =========================
-- Room.optionList (Element Collection)
-- =========================
-- insert into room_option_list (room_id, option_list) values
--                                                         (100, '화이트보드'),
--                                                         (100, 'WIFI'),
--                                                         (101, '프로젝터'),
--                                                         (101, 'WIFI'),
--                                                         (102, 'TV'),
--                                                         (102, 'HDMI'),
--                                                         (103, '개인콘센트'),
--                                                         (103, '방음도어'),
--                                                         (104, '프로젝터'),
--                                                         (104, '마이크'),
--                                                         (104, '스피커');

-- =========================
-- Room.photoList (Element Collection)
-- 실제 운영에서는 S3 등의 실제 URL을 넣어주세요
-- =========================
-- insert into room_photo_list (room_id, photo_list) values
--                                                       (100, 'https://example.com/photo/sejong-1.jpg'),
--                                                       (100, 'https://example.com/photo/sejong-2.jpg'),
--                                                       (101, 'https://example.com/photo/haeundae-1.jpg'),
--                                                       (102, 'https://example.com/photo/dunsan-1.jpg'),
--                                                       (103, 'https://example.com/photo/inju-1.jpg'),
--                                                       (104, 'https://example.com/photo/gongpyeong-1.jpg'),
--                                                       (104, 'https://example.com/photo/gongpyeong-2.jpg');

-- =========================
-- Schedule (영업자가 등록한 예약가능 슬롯)
-- 날짜/슬롯 인덱스는 예시입니다.
-- =========================
insert into schedule (id, date, en_phone_number, re_phone_number, room_id)
values
    (200, DATE '2025-08-20', '010-1111-2222', null, 100),
    (201, DATE '2025-08-20', '010-2222-3333', null, 101),
    (202, DATE '2025-08-21', '010-3333-4444', null, 102),
    (203, DATE '2025-08-21', '010-4444-5555', null, 103),
    (204, DATE '2025-08-22', '010-5555-6666', null, 104);

-- Schedule.slotIndex (Element Collection: 가능 슬롯 인덱스)
insert into schedule_slot_index (schedule_id, slot_index) values
                                                              (200, 9),  (200, 10), (200, 11),
                                                              (201, 13), (201, 14),
                                                              (202, 10), (202, 11), (202, 12),
                                                              (203, 15),
                                                              (204, 9),  (204, 10), (204, 11), (204, 12);

-- =========================
-- Reservation (사용자 예약)
-- =========================
insert into reservation (id, re_name, re_phone_number, date, room_id)
values
    (300, '김하나', '010-9000-1111', DATE '2025-08-20', 100),
    (301, '박둘',   '010-9000-2222', DATE '2025-08-21', 102),
    (302, '이셋',   '010-9000-3333', DATE '2025-08-22', 104);

-- Reservation.slotIndex (Element Collection: 예약된 슬롯 인덱스 세트)
insert into reservation_slot_index (reservation_id, slot_index) values
                                                                    (300, 10),
                                                                    (301, 11),
                                                                    (302, 9), (302, 10);

-- =========================
-- 양방향 연관 관계 상 Address.room (mappedBy)이므로 별도 업데이트 없이
-- Room의 address_id FK만 정확하면 됩니다.
-- =========================

CREATE TABLE address
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    latitude  DOUBLE                NOT NULL,
    longitude DOUBLE                NOT NULL,
    road_name VARCHAR(255)          NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    re_name         VARCHAR(255)          NULL,
    re_phone_number VARCHAR(255)          NULL,
    selected_time   INT                   NOT NULL,
    room_id         BIGINT                NOT NULL,
    slot_index      BLOB                  NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE room
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    account         VARCHAR(255)          NULL,
    en_name         VARCHAR(255)          NULL,
    en_phone_number VARCHAR(255)          NULL,
    max_people      INT                   NOT NULL,
    memo            VARCHAR(2000)         NULL,
    price           INT                   NOT NULL,
    address_id      BIGINT                NOT NULL,
    chip_list       BLOB                  NULL,
    option_list     BLOB                  NULL,
    photo_list      BLOB                  NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE schedule
(
    date            date                  NULL,
    id              BIGINT AUTO_INCREMENT NOT NULL,
    room_id         BIGINT                NULL,
    en_phone_number VARCHAR(255)          NULL,
    re_phone_number VARCHAR(255)          NULL,
    slot_index      BLOB                  NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

ALTER TABLE room
    ADD CONSTRAINT UKmsj6pl25a9mfurteu4nsg4t1u UNIQUE (address_id);

ALTER TABLE room
    ADD CONSTRAINT FK98b6qeo4s2wbfach8x3g84a3d FOREIGN KEY (address_id) REFERENCES address (id) ON DELETE NO ACTION;

ALTER TABLE schedule
    ADD CONSTRAINT FKh2hdhbss2x31ns719hka6enma FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE NO ACTION;

CREATE INDEX FKh2hdhbss2x31ns719hka6enma ON schedule (room_id);

ALTER TABLE reservation
    ADD CONSTRAINT FKm8xumi0g23038cw32oiva2ymw FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE NO ACTION;

CREATE INDEX FKm8xumi0g23038cw32oiva2ymw ON reservation (room_id);


-- =========================
-- Address
-- (변화 없음: 단일 값 저장)
-- =========================
insert into address (id, latitude, longitude, road_name) values (1, 37.5665, 126.9780, '서울특별시 중구 세종대로 110');
insert into address (id, latitude, longitude, road_name) values (2, 35.1796, 129.0756, '부산광역시 중구 중앙대로 100');
insert into address (id, latitude, longitude, road_name) values (3, 36.3504, 127.3845, '대전광역시 서구 둔산대로 100');
insert into address (id, latitude, longitude, road_name) values (4, 37.4563, 126.7052, '인천광역시 남동구 인주대로 100');
insert into address (id, latitude, longitude, road_name) values (5, 35.8714, 128.6014, '대구광역시 중구 공평로 100');

-- =========================
-- Room
-- (chipList, optionList, photoList를 단일 컬럼에 통합)
-- =========================
insert into room (id, en_name, en_phone_number, address_id, account, max_people, price, memo, chip_list, option_list, photo_list)
values
    (100, '세종스터디룸', '010-1111-2222', 1, '신한 110-123-456789', 6, 15000, '종로 중심, 조용한 분위기', '조용한,집중하기_좋은', '화이트보드,WIFI', 'https://example.com/photo/sejong-1.jpg,https://example.com/photo/sejong-2.jpg'),
    (101, '해운대미팅룸', '010-2222-3333', 2, '국민 333-222-111111', 10, 25000, '바다 근처, 채광 좋음', '채광_좋은,개방적인', '프로젝터,WIFI', 'https://example.com/photo/haeundae-1.jpg'),
    (102, '둔산협업룸', '010-3333-4444', 3, '우리 1002-555-666666', 8, 20000, '협업 친화적 레이아웃', '협업_친화적,모던한', 'TV,HDMI', 'https://example.com/photo/dunsan-1.jpg'),
    (103, '인주프라이빗룸', '010-4444-5555', 4, '농협 302-7777-888888', 4, 12000, '프라이빗, 방음 좋음', '프라이빗한,방음_좋은', '개인콘센트,방음도어', 'https://example.com/photo/inju-1.jpg'),
    (104, '공평크리에이티브', '010-5555-6666', 5, '카카오 3333-99-000000', 12, 30000, '창의적인 무드, 장비 완비', '창의적인,장비_완비,트렌디한', '프로젝터,마이크,스피커', 'https://example.com/photo/gongpyeong-1.jpg,https://example.com/photo/gongpyeong-2.jpg');

-- =========================
-- Schedule
-- (slotIndex를 단일 컬럼에 통합)
-- =========================
insert into schedule (id, date, en_phone_number, re_phone_number, room_id, slot_index)
values
    (200, DATE '2025-08-20', '010-1111-2222', null, 100, '9,10,11'),
    (201, DATE '2025-08-20', '010-2222-3333', null, 101, '13,14'),
    (202, DATE '2025-08-21', '010-3333-4444', null, 102, '10,11,12'),
    (203, DATE '2025-08-21', '010-4444-5555', null, 103, '15'),
    (204, DATE '2025-08-22', '010-5555-6666', null, 104, '9,10,11,12');

-- =========================
-- Reservation
-- (slotIndex를 단일 컬럼에 통합)
-- =========================
insert into reservation (id, re_name, re_phone_number, date, room_id, slot_index)
values
    (300, '김하나', '010-9000-1111', DATE '2025-08-20', 100, '10'),
    (301, '박둘',   '010-9000-2222', DATE '2025-08-21', 102, '11'),
    (302, '이셋',   '010-9000-3333', DATE '2025-08-22', 104, '9,10');

-- =========================
-- 양방향 연관 관계 상 Address.room (mappedBy)이므로 별도 업데이트 없이
-- Room의 address_id FK만 정확하면 됩니다.
-- =========================