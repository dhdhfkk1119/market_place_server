-- 멤버
-- 관리자 계정
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'admin', '서울시 강남구', 'admin@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'ADMIN', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 1
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user1', '부산시 연제구', 'choongecho@gmail.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 2
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user2', '경기도 성남시', 'user2@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 탈퇴한 유저
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'withdraw', '서울시 마포구', 'withdrawn@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'WITHDRAWN', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 정지된 유저
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'banned', '서울시 서대문구', 'banned@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'BANNED', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 3
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user3', '부산광역시 해운대구', 'user3@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 4
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user4', '대구광역시 달서구', 'user4@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 5
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user5', '인천광역시 남동구', 'user5@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 6
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user6', '광주광역시 북구', 'user6@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 7
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user7', '대전광역시 유성구', 'user7@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 8
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user8', '울산광역시 남구', 'user8@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 9
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user9', '세종특별자치시 조치원읍', 'user9@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 10
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user10', '경기도 수원시', 'user10@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 11
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user11', '강원도 춘천시', 'user11@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 12
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user12', '전라북도 전주시', 'user12@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 13
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user13', '전라남도 목포시', 'user13@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 커뮤니티 대분류 카테고리
INSERT INTO  community_category_tb  (name) VALUES
('동네정보'),
('이웃과 함께'),
('소식'),
('기타');

-- 커뮤니티 세부 카테고리
INSERT INTO community_topic_tb (id, category_id, name) VALUES
(1, 1, '맛집'),
(2, 1, '생활/편의'),
(3, 1, '병원/약국'),
(4, 1, '미용'),

(5, 2, '반려동물'),
(6, 2, '운동'),
(7, 2, '취미'),
(8, 2, '고민/사연'),

(9, 3, '동네행사'),
(10, 3, '분실/실종'),
(11, 3, '동네사건사고'),
(12, 3, '공공소식'),

(13, 4, '일반');

-- 아이템 카테고리

INSERT INTO ITEM_CATEGORY_TB (NAME)
VALUES
('디지털 기기'),
('가구/인테리어'),
('생활가전'),
('도서'),
('스포츠/레저'),
('취미/게임'),
('의류/패션'),
('반려동물 용품'),
('식품'),
('기타');
<<<<<<< HEAD
=======


-- 아이템

INSERT INTO ITEM_TB (member_id, item_category_id, title, content, price, status, trade_location, average_rating, created_at)
VALUES
(1, 1, '삼성노트북', '삼성 최신형 노트북 판매합니다.', 5000,  'ON_SALE', '강남역', 3.5, CURRENT_TIMESTAMP),
(2, 1, 'LG노트북', 'LG 그램 중고 노트북입니다.',     12000, 'ON_SALE', '잠실역', 4.0, CURRENT_TIMESTAMP),
(3, 1, '애플노트북', '맥북 프로 상태 양호합니다.',     18000, 'ON_SALE', '마포역', 4.2, CURRENT_TIMESTAMP),
(4, 1, '레노버노트북', '레노버 아이디어패드 팝니다.', 7000,  'ON_SALE', '강서역', 3.8, CURRENT_TIMESTAMP),
(5, 1, '델노트북', '델 XPS 중고 노트북 판매',        20000, 'ON_SALE', '천호역', 4.7, CURRENT_TIMESTAMP),

(6, 2, '사무용의자', '편안한 사무용 의자 판매',        3000,  'ON_SALE', '노원역', 3.9, CURRENT_TIMESTAMP),
(7, 2, '원목의자', '인테리어에 좋은 원목 의자',        15000, 'ON_SALE', '불광역', 4.5, CURRENT_TIMESTAMP),
(8, 2, '게이밍의자', '장시간 사용에 좋은 게이밍 의자', 8000,  'ON_SALE', '교대역', 4.1, CURRENT_TIMESTAMP),
(9, 2, '식탁의자', '가정용 식탁 의자 세트 판매',       17000, 'ON_SALE', '용산역', 3.7, CURRENT_TIMESTAMP),
(10,2, '디자인의자', '디자인 감각 있는 의자',         10000, 'ON_SALE', '종각역', 4.4, CURRENT_TIMESTAMP),

-- 책 5개
(1, 4, '자바책', '자바 프로그래밍 기초 교재',         2000,  'ON_SALE', '강남역', 4.0, CURRENT_TIMESTAMP),
(2, 4, '알고리즘책', '알고리즘 문제 해결 전략',        9000,  'ON_SALE', '잠실역', 4.6, CURRENT_TIMESTAMP),
(3, 4, '데이터베이스책', '데이터베이스 개론 교재',    11000, 'ON_SALE', '마포역', 3.8, CURRENT_TIMESTAMP),
(4, 4, '영어책', '토익 영어 문법 교재',               6000,  'ON_SALE', '강서역', 4.3, CURRENT_TIMESTAMP),
(5, 4, '머신러닝책', '머신러닝 입문서',               16000, 'ON_SALE', '천호역', 4.9, CURRENT_TIMESTAMP);


-- 좋아요
INSERT INTO ITEM_FAVORITE_TB (ITEM_ID, MEMBER_ID)
VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10),
(11, 11),
(12, 12),
(13, 13),
(14, 14),
(15, 15);

-- 트레이드
INSERT INTO trade_tb (item_id, seller_id, buyer_id, buyer_reviewed, seller_reviewed)
VALUES
(5, 1, 6, TRUE,  TRUE),
(6, 2, 7, TRUE,  FALSE),
(7, 3, 8, FALSE, TRUE),
(8, 4, 9, TRUE,  TRUE),
(9, 5, 10, FALSE, FALSE),
(10, 1, 7, TRUE,  TRUE),
(11, 2, 8, TRUE,  TRUE),
(12, 3, 9, FALSE, TRUE),
(13, 4, 10, TRUE, FALSE),
(14, 5, 6, TRUE,  TRUE);

-- 트레이드 리뷰
INSERT INTO trade_review_tb (trade_id, reviewer_id, content, rating, created_at)
VALUES
(1, 6, '좋은 거래였습니다. 감사합니다!', 5.0, CURRENT_TIMESTAMP),
(1, 1, '구매자분이 친절했습니다.',        4.8, CURRENT_TIMESTAMP),
(2, 7, '상품 상태가 설명과 같아요.',      4.5, CURRENT_TIMESTAMP),
(2, 2, '빠른 결제 감사합니다.',            5.0, CURRENT_TIMESTAMP),
(3, 8, '사진보다 상태가 별로였어요.',      3.0, CURRENT_TIMESTAMP),
(4, 9, '좋은 거래였어요.',                4.7, CURRENT_TIMESTAMP),
(4, 4, '구매자분 응답이 빨랐습니다.',      5.0, CURRENT_TIMESTAMP),
(5,10, '배송이 조금 늦었어요.',            3.5, CURRENT_TIMESTAMP),
(6, 7, '아주 만족스러운 거래였습니다.',    5.0, CURRENT_TIMESTAMP),
(6, 1, '연락이 빨라서 좋았습니다.',        4.9, CURRENT_TIMESTAMP),
(7, 8, '상품이 설명보다 더 좋았어요.',     5.0, CURRENT_TIMESTAMP),
(8, 9, '연락이 잘 안 되어 아쉬웠습니다.',  3.2, CURRENT_TIMESTAMP),
(8, 3, '결제는 빨랐습니다.',               4.0, CURRENT_TIMESTAMP),
(9,10, '판매자분이 친절했어요.',           4.8, CURRENT_TIMESTAMP),
(10,5, '시간 약속을 잘 지키셨어요.',       5.0, CURRENT_TIMESTAMP);
>>>>>>> 95fe62da921dd74cfc501267bf7ee4ba8972bd1a


-- 아이템

INSERT INTO ITEM_TB (member_id, item_category_id, title, content, price, status, trade_location, average_rating, created_at)
VALUES
(1, 1, '삼성노트북', '삼성 최신형 노트북 판매합니다.', 5000,  'ON_SALE', '강남역', 3.5, CURRENT_TIMESTAMP),
(2, 1, 'LG노트북', 'LG 그램 중고 노트북입니다.',     12000, 'ON_SALE', '잠실역', 4.0, CURRENT_TIMESTAMP),
(3, 1, '애플노트북', '맥북 프로 상태 양호합니다.',     18000, 'ON_SALE', '마포역', 4.2, CURRENT_TIMESTAMP),
(4, 1, '레노버노트북', '레노버 아이디어패드 팝니다.', 7000,  'ON_SALE', '강서역', 3.8, CURRENT_TIMESTAMP),
(5, 1, '델노트북', '델 XPS 중고 노트북 판매',        20000, 'ON_SALE', '천호역', 4.7, CURRENT_TIMESTAMP),

(6, 2, '사무용의자', '편안한 사무용 의자 판매',        3000,  'ON_SALE', '노원역', 3.9, CURRENT_TIMESTAMP),
(7, 2, '원목의자', '인테리어에 좋은 원목 의자',        15000, 'ON_SALE', '불광역', 4.5, CURRENT_TIMESTAMP),
(8, 2, '게이밍의자', '장시간 사용에 좋은 게이밍 의자', 8000,  'ON_SALE', '교대역', 4.1, CURRENT_TIMESTAMP),
(9, 2, '식탁의자', '가정용 식탁 의자 세트 판매',       17000, 'ON_SALE', '용산역', 3.7, CURRENT_TIMESTAMP),
(10,2, '디자인의자', '디자인 감각 있는 의자',         10000, 'ON_SALE', '종각역', 4.4, CURRENT_TIMESTAMP),

-- 책 5개
(1, 4, '자바책', '자바 프로그래밍 기초 교재',         2000,  'ON_SALE', '강남역', 4.0, CURRENT_TIMESTAMP),
(2, 4, '알고리즘책', '알고리즘 문제 해결 전략',        9000,  'ON_SALE', '잠실역', 4.6, CURRENT_TIMESTAMP),
(3, 4, '데이터베이스책', '데이터베이스 개론 교재',    11000, 'ON_SALE', '마포역', 3.8, CURRENT_TIMESTAMP),
(4, 4, '영어책', '토익 영어 문법 교재',               6000,  'ON_SALE', '강서역', 4.3, CURRENT_TIMESTAMP),
(5, 4, '머신러닝책', '머신러닝 입문서',               16000, 'ON_SALE', '천호역', 4.9, CURRENT_TIMESTAMP);


-- 좋아요
INSERT INTO ITEM_FAVORITE_TB (ITEM_ID, MEMBER_ID)
VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10),
(11, 11),
(12, 12),
(13, 13),
(14, 14),
(15, 15);

-- 트레이드
INSERT INTO trade_tb (item_id, seller_id, buyer_id, buyer_reviewed, seller_reviewed)
VALUES
(5, 1, 6, TRUE,  TRUE),
(6, 2, 7, TRUE,  FALSE),
(7, 3, 8, FALSE, TRUE),
(8, 4, 9, TRUE,  TRUE),
(9, 5, 10, FALSE, FALSE),
(10, 1, 7, TRUE,  TRUE),
(11, 2, 8, TRUE,  TRUE),
(12, 3, 9, FALSE, TRUE),
(13, 4, 10, TRUE, FALSE),
(14, 5, 6, TRUE,  TRUE);

-- 트레이드 리뷰
INSERT INTO trade_review_tb (trade_id, reviewer_id, content, rating, created_at)
VALUES
(1, 6, '좋은 거래였습니다. 감사합니다!', 5.0, CURRENT_TIMESTAMP),
(1, 1, '구매자분이 친절했습니다.',        4.8, CURRENT_TIMESTAMP),
(2, 7, '상품 상태가 설명과 같아요.',      4.5, CURRENT_TIMESTAMP),
(2, 2, '빠른 결제 감사합니다.',            5.0, CURRENT_TIMESTAMP),
(3, 8, '사진보다 상태가 별로였어요.',      3.0, CURRENT_TIMESTAMP),
(4, 9, '좋은 거래였어요.',                4.7, CURRENT_TIMESTAMP),
(4, 4, '구매자분 응답이 빨랐습니다.',      5.0, CURRENT_TIMESTAMP),
(5,10, '배송이 조금 늦었어요.',            3.5, CURRENT_TIMESTAMP),
(6, 7, '아주 만족스러운 거래였습니다.',    5.0, CURRENT_TIMESTAMP),
(6, 1, '연락이 빨라서 좋았습니다.',        4.9, CURRENT_TIMESTAMP),
(7, 8, '상품이 설명보다 더 좋았어요.',     5.0, CURRENT_TIMESTAMP),
(8, 9, '연락이 잘 안 되어 아쉬웠습니다.',  3.2, CURRENT_TIMESTAMP),
(8, 3, '결제는 빨랐습니다.',               4.0, CURRENT_TIMESTAMP),
(9,10, '판매자분이 친절했어요.',           4.8, CURRENT_TIMESTAMP),
(10,5, '시간 약속을 잘 지키셨어요.',       5.0, CURRENT_TIMESTAMP);

-- 멤버
-- 관리자 계정
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'admin', '서울시 강남구', 'admin@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'ADMIN', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 1
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user1', '부산시 연제구', 'choongecho@gmail.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 2
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user2', '경기도 성남시', 'user2@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 탈퇴한 유저
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'withdraw', '서울시 마포구', 'withdrawn@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'WITHDRAWN', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 정지된 유저
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'banned', '서울시 서대문구', 'banned@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'BANNED', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 3
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user3', '부산광역시 해운대구', 'user3@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 4
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user4', '대구광역시 달서구', 'user4@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 5
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user5', '인천광역시 남동구', 'user5@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 6
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user6', '광주광역시 북구', 'user6@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 7
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user7', '대전광역시 유성구', 'user7@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 8
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user8', '울산광역시 남구', 'user8@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 9
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user9', '세종특별자치시 조치원읍', 'user9@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 10
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user10', '경기도 수원시', 'user10@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 11
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user11', '강원도 춘천시', 'user11@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 12
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user12', '전라북도 전주시', 'user12@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 유저 13
INSERT INTO MEMBER_TB (password, login_id, address, email, provider, provider_id, email_verified_at,
                       role, status, retransaction_rate, manner_score, created_at, updated_at)
VALUES ('{bcrypt}$2a$10$CwTycUXWue0Thq9StjUM0uJ8HUf5hU/6If0YkpZG3hWxjK0tHfHcW',
        'user13', '전라남도 목포시', 'user13@market.com',
        'MARKIT', NULL, CURRENT_TIMESTAMP,
        'USER', 'ACTIVE', 0, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 커뮤니티 대분류 카테고리
INSERT INTO  community_category_tb  (name) VALUES
('동네정보'),
@@ -24,6 +153,97 @@ INSERT INTO community_topic_tb (id, category_id, name) VALUES

(13, 4, '일반');

-- 아이템 카테고리

INSERT INTO ITEM_CATEGORY_TB (NAME)
VALUES
('디지털 기기'),
('가구/인테리어'),
('생활가전'),
('도서'),
('스포츠/레저'),
('취미/게임'),
('의류/패션'),
('반려동물 용품'),
('식품'),
('기타');


-- 아이템

INSERT INTO ITEM_TB (member_id, item_category_id, title, content, price, status, trade_location, average_rating, created_at)
VALUES
(1, 1, '삼성노트북', '삼성 최신형 노트북 판매합니다.', 5000,  'ON_SALE', '강남역', 3.5, CURRENT_TIMESTAMP),
(2, 1, 'LG노트북', 'LG 그램 중고 노트북입니다.',     12000, 'ON_SALE', '잠실역', 4.0, CURRENT_TIMESTAMP),
(3, 1, '애플노트북', '맥북 프로 상태 양호합니다.',     18000, 'ON_SALE', '마포역', 4.2, CURRENT_TIMESTAMP),
(4, 1, '레노버노트북', '레노버 아이디어패드 팝니다.', 7000,  'ON_SALE', '강서역', 3.8, CURRENT_TIMESTAMP),
(5, 1, '델노트북', '델 XPS 중고 노트북 판매',        20000, 'ON_SALE', '천호역', 4.7, CURRENT_TIMESTAMP),

(6, 2, '사무용의자', '편안한 사무용 의자 판매',        3000,  'ON_SALE', '노원역', 3.9, CURRENT_TIMESTAMP),
(7, 2, '원목의자', '인테리어에 좋은 원목 의자',        15000, 'ON_SALE', '불광역', 4.5, CURRENT_TIMESTAMP),
(8, 2, '게이밍의자', '장시간 사용에 좋은 게이밍 의자', 8000,  'ON_SALE', '교대역', 4.1, CURRENT_TIMESTAMP),
(9, 2, '식탁의자', '가정용 식탁 의자 세트 판매',       17000, 'ON_SALE', '용산역', 3.7, CURRENT_TIMESTAMP),
(10,2, '디자인의자', '디자인 감각 있는 의자',         10000, 'ON_SALE', '종각역', 4.4, CURRENT_TIMESTAMP),

-- 책 5개
(1, 4, '자바책', '자바 프로그래밍 기초 교재',         2000,  'ON_SALE', '강남역', 4.0, CURRENT_TIMESTAMP),
(2, 4, '알고리즘책', '알고리즘 문제 해결 전략',        9000,  'ON_SALE', '잠실역', 4.6, CURRENT_TIMESTAMP),
(3, 4, '데이터베이스책', '데이터베이스 개론 교재',    11000, 'ON_SALE', '마포역', 3.8, CURRENT_TIMESTAMP),
(4, 4, '영어책', '토익 영어 문법 교재',               6000,  'ON_SALE', '강서역', 4.3, CURRENT_TIMESTAMP),
(5, 4, '머신러닝책', '머신러닝 입문서',               16000, 'ON_SALE', '천호역', 4.9, CURRENT_TIMESTAMP);


-- 좋아요
INSERT INTO ITEM_FAVORITE_TB (ITEM_ID, MEMBER_ID)
VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10),
(11, 11),
(12, 12),
(13, 13),
(14, 14),
(15, 15);

-- 트레이드
INSERT INTO trade_tb (item_id, seller_id, buyer_id, buyer_reviewed, seller_reviewed)
VALUES
(5, 1, 6, TRUE,  TRUE),
(6, 2, 7, TRUE,  FALSE),
(7, 3, 8, FALSE, TRUE),
(8, 4, 9, TRUE,  TRUE),
(9, 5, 10, FALSE, FALSE),
(10, 1, 7, TRUE,  TRUE),
(11, 2, 8, TRUE,  TRUE),
(12, 3, 9, FALSE, TRUE),
(13, 4, 10, TRUE, FALSE),
(14, 5, 6, TRUE,  TRUE);

-- 트레이드 리뷰
INSERT INTO trade_review_tb (trade_id, reviewer_id, content, rating, created_at)
VALUES
(1, 6, '좋은 거래였습니다. 감사합니다!', 5.0, CURRENT_TIMESTAMP),
(1, 1, '구매자분이 친절했습니다.',        4.8, CURRENT_TIMESTAMP),
(2, 7, '상품 상태가 설명과 같아요.',      4.5, CURRENT_TIMESTAMP),
(2, 2, '빠른 결제 감사합니다.',            5.0, CURRENT_TIMESTAMP),
(3, 8, '사진보다 상태가 별로였어요.',      3.0, CURRENT_TIMESTAMP),
(4, 9, '좋은 거래였어요.',                4.7, CURRENT_TIMESTAMP),
(4, 4, '구매자분 응답이 빨랐습니다.',      5.0, CURRENT_TIMESTAMP),
(5,10, '배송이 조금 늦었어요.',            3.5, CURRENT_TIMESTAMP),
(6, 7, '아주 만족스러운 거래였습니다.',    5.0, CURRENT_TIMESTAMP),
(6, 1, '연락이 빨라서 좋았습니다.',        4.9, CURRENT_TIMESTAMP),
(7, 8, '상품이 설명보다 더 좋았어요.',     5.0, CURRENT_TIMESTAMP),
(8, 9, '연락이 잘 안 되어 아쉬웠습니다.',  3.2, CURRENT_TIMESTAMP),
(8, 3, '결제는 빨랐습니다.',               4.0, CURRENT_TIMESTAMP),
(9,10, '판매자분이 친절했어요.',           4.8, CURRENT_TIMESTAMP),
(10,5, '시간 약속을 잘 지키셨어요.',       5.0, CURRENT_TIMESTAMP);


