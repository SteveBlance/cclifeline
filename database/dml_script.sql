INSERT INTO MEMBERS (MEMBERSHIP_NUMBER, MEMBERSHIP_TYPE, STATUS, FORENAME, SURNAME, PAYER_TYPE, JOIN_DATE, EMAIL,
                     LANDLINE_NUMBER, MOBILE_NUMBER, CARD_REQUEST_DATE, CARD_ISSUED_DATE, WELCOME_LETTER_ISSUED_DATE, COMMENTS)
VALUES (1234, 'Legacy', 'Open', 'Bobby', 'Smith', 'Monthly', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), 'bsmith@email.com',
        '01383 335577', NULL, STR_TO_DATE('14/12/2013', '%d/%m/%Y'), STR_TO_DATE('14/12/2013', '%d/%m/%Y'), NULL, NULL);

INSERT INTO PAYMENT_REFERENCES (REFERENCE, IS_ACTIVE, NAME, MEMBER_ID)
SELECT 'FPS CREDIT 1234', FALSE, SURNAME, ID FROM MEMBERS WHERE MEMBERSHIP_NUMBER = 1234;

INSERT INTO PAYMENT_REFERENCES (REFERENCE, IS_ACTIVE, NAME, MEMBER_ID)
SELECT 'FPS CREDIT 1234 NEW', TRUE, SURNAME, ID FROM MEMBERS WHERE MEMBERSHIP_NUMBER = 1234;

INSERT INTO MEMBERS (MEMBERSHIP_NUMBER, MEMBERSHIP_TYPE, STATUS, FORENAME, SURNAME, PAYER_TYPE, JOIN_DATE, LEAVE_DATE, EMAIL,
                     LANDLINE_NUMBER, MOBILE_NUMBER, CARD_REQUEST_DATE, CARD_ISSUED_DATE, WELCOME_LETTER_ISSUED_DATE, COMMENTS)
VALUES (1235, 'Legacy', 'Cancelled', 'Jim', 'Smith', 'Monthly', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), STR_TO_DATE('14/12/2014', '%d/%m/%Y'), 'jsmith@email.com',
        '01383 776622', '077666666', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), STR_TO_DATE('14/12/2013', '%d/%m/%Y'), NOW(), 'Membership card lost');

INSERT INTO PAYMENT_REFERENCES (REFERENCE, IS_ACTIVE, NAME, MEMBER_ID)
SELECT 'FPS CREDIT 1235', TRUE, SURNAME, ID FROM MEMBERS WHERE MEMBERSHIP_NUMBER = 1235;

INSERT INTO MEMBERS (MEMBERSHIP_NUMBER, MEMBERSHIP_TYPE, STATUS, FORENAME, SURNAME, PAYER_TYPE, JOIN_DATE, EMAIL,
                     LANDLINE_NUMBER, MOBILE_NUMBER, CARD_REQUEST_DATE, CARD_ISSUED_DATE, WELCOME_LETTER_ISSUED_DATE, COMMENTS)
VALUES (1244, 'Legacy', 'Open', 'Dave', 'Spence', 'Monthly', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), NULL,
        '00441383 887766', '077666666', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), STR_TO_DATE('14/12/2013', '%d/%m/%Y'), NOW(), NULL);

INSERT INTO PAYMENT_REFERENCES (REFERENCE, IS_ACTIVE, NAME, MEMBER_ID)
SELECT 'FPS CREDIT 1244', TRUE, SURNAME, ID FROM MEMBERS WHERE MEMBERSHIP_NUMBER = 1244;

INSERT INTO MEMBERS (MEMBERSHIP_NUMBER, MEMBERSHIP_TYPE, STATUS, FORENAME, SURNAME, PAYER_TYPE, JOIN_DATE, EMAIL,
                     LANDLINE_NUMBER, MOBILE_NUMBER, CARD_REQUEST_DATE, CARD_ISSUED_DATE, WELCOME_LETTER_ISSUED_DATE, COMMENTS)
VALUES (1245, 'Lifeline', 'Open', 'Jane', 'Wilson', 'Monthly', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), 'jwilson@email.com',
        NULL, NULL, STR_TO_DATE('14/12/2013', '%d/%m/%Y'), STR_TO_DATE('14/12/2013', '%d/%m/%Y'), NOW(), 'Membership card lost');

INSERT INTO PAYMENT_REFERENCES (REFERENCE, IS_ACTIVE, NAME, MEMBER_ID)
SELECT 'FPS CREDIT 1245', TRUE, SURNAME, ID FROM MEMBERS WHERE MEMBERSHIP_NUMBER = 1245;

INSERT INTO MEMBERS (MEMBERSHIP_NUMBER, MEMBERSHIP_TYPE, STATUS, FORENAME, SURNAME, PAYER_TYPE, JOIN_DATE, EMAIL,
                     LANDLINE_NUMBER, MOBILE_NUMBER, CARD_REQUEST_DATE, CARD_ISSUED_DATE, WELCOME_LETTER_ISSUED_DATE, COMMENTS)
VALUES (1566, 'Lifeline', 'Open', 'Sandy', 'Jamieson', 'Monthly', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), 'sj@email.com',
        '01383 445566', '077666666', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), STR_TO_DATE('14/12/2013', '%d/%m/%Y'), NOW(), 'Was member 1122');

INSERT INTO PAYMENT_REFERENCES (REFERENCE, IS_ACTIVE, NAME, MEMBER_ID)
SELECT 'FPS CREDIT 1566', TRUE, SURNAME, ID FROM MEMBERS WHERE MEMBERSHIP_NUMBER = 1566;

INSERT INTO MEMBERS (MEMBERSHIP_NUMBER, MEMBERSHIP_TYPE, STATUS, FORENAME, SURNAME, PAYER_TYPE, JOIN_DATE, EMAIL,
                     LANDLINE_NUMBER, MOBILE_NUMBER, CARD_REQUEST_DATE, CARD_ISSUED_DATE, WELCOME_LETTER_ISSUED_DATE, COMMENTS)
VALUES (1567, 'Lifeline', 'Closed', 'Arthur', 'Cole', 'Monthly', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), 'arty@email.com',
        '+441383 112233', NULL, STR_TO_DATE('14/12/2013', '%d/%m/%Y'), STR_TO_DATE('14/12/2013', '%d/%m/%Y'), NOW(), NULL);

INSERT INTO PAYMENT_REFERENCES (REFERENCE, IS_ACTIVE, NAME, MEMBER_ID)
SELECT 'FPS CREDIT 1567', TRUE, SURNAME, ID FROM MEMBERS WHERE MEMBERSHIP_NUMBER = 1567;

INSERT INTO MEMBERS (MEMBERSHIP_NUMBER, MEMBERSHIP_TYPE, STATUS, FORENAME, SURNAME, PAYER_TYPE, JOIN_DATE, EMAIL,
                     LANDLINE_NUMBER, MOBILE_NUMBER, CARD_REQUEST_DATE, CARD_ISSUED_DATE, WELCOME_LETTER_ISSUED_DATE, COMMENTS)
VALUES (1568, 'Lifeline', 'Open', 'Mike', 'Reid', 'Monthly', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), 'mreid@email.com',
        '01383 445566', '077666666', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), STR_TO_DATE('14/12/2013', '%d/%m/%Y'), NULL, 'Membership card lost. Found under a tree outside Dens Park. Returned via carrier pigeon. Card still in good condition but the left upper edge is slightly curled up. Smells slightly damp also.');

INSERT INTO PAYMENT_REFERENCES (REFERENCE, IS_ACTIVE, NAME, MEMBER_ID)
SELECT 'FPS CREDIT 1568', TRUE, SURNAME, ID FROM MEMBERS WHERE MEMBERSHIP_NUMBER = 1568;

INSERT INTO MEMBERS (MEMBERSHIP_NUMBER, MEMBERSHIP_TYPE, STATUS, FORENAME, SURNAME, PAYER_TYPE, JOIN_DATE, EMAIL,
                     LANDLINE_NUMBER, MOBILE_NUMBER, CARD_REQUEST_DATE, CARD_ISSUED_DATE, WELCOME_LETTER_ISSUED_DATE, COMMENTS)
VALUES (1569, 'Lifeline', 'Open', 'Liz', 'Marshall', 'Monthly', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), 'LizzyM@email.com',
        '01383 231423', '077666666', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), STR_TO_DATE('14/12/2013', '%d/%m/%Y'), NULL, 'Membership card lost');

INSERT INTO PAYMENT_REFERENCES (REFERENCE, IS_ACTIVE, NAME, MEMBER_ID)
SELECT 'FPS CREDIT 1569', TRUE, SURNAME, ID FROM MEMBERS WHERE MEMBERSHIP_NUMBER = 1569;

INSERT INTO MEMBERS (MEMBERSHIP_NUMBER, MEMBERSHIP_TYPE, STATUS, FORENAME, SURNAME, PAYER_TYPE, JOIN_DATE, EMAIL,
                     LANDLINE_NUMBER, MOBILE_NUMBER, CARD_REQUEST_DATE, CARD_ISSUED_DATE, WELCOME_LETTER_ISSUED_DATE, COMMENTS)
VALUES (1570, 'Lifeline', 'Open', 'Bob', 'Marshall', 'Monthly', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), 'Bmarsh@email.com',
        '01383 446688', '0712226668', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), STR_TO_DATE('14/12/2013', '%d/%m/%Y'), NULL, 'Was member 1111');

INSERT INTO PAYMENT_REFERENCES (REFERENCE, IS_ACTIVE, NAME, MEMBER_ID)
SELECT 'FPS CREDIT 1570', TRUE, SURNAME, ID FROM MEMBERS WHERE MEMBERSHIP_NUMBER = 1570;

INSERT INTO MEMBERS (MEMBERSHIP_NUMBER, MEMBERSHIP_TYPE, STATUS, FORENAME, SURNAME, PAYER_TYPE, JOIN_DATE, EMAIL,
                     LANDLINE_NUMBER, MOBILE_NUMBER, CARD_REQUEST_DATE, CARD_ISSUED_DATE, WELCOME_LETTER_ISSUED_DATE, COMMENTS)
VALUES (1578, 'Lifeline', 'Open', 'Davie', 'Marshall', 'Annual', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), 'dm@email.com',
        '+1 555 443322', '0722334454', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), STR_TO_DATE('14/12/2013', '%d/%m/%Y'), NULL, NULL);

INSERT INTO PAYMENT_REFERENCES (REFERENCE, IS_ACTIVE, NAME, MEMBER_ID)
SELECT 'FPS CREDIT 1578', TRUE, SURNAME, ID FROM MEMBERS WHERE MEMBERSHIP_NUMBER = 1578;

INSERT INTO MEMBERS (MEMBERSHIP_NUMBER, MEMBERSHIP_TYPE, STATUS, FORENAME, SURNAME, PAYER_TYPE, JOIN_DATE, EMAIL,
                     LANDLINE_NUMBER, MOBILE_NUMBER, CARD_REQUEST_DATE, CARD_ISSUED_DATE, WELCOME_LETTER_ISSUED_DATE, COMMENTS)
VALUES (1580, 'Premium Legacy', 'Open', 'John', 'Brown', 'Monthly', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), 'broony@email.com',
        '01383 111111', '077111111', STR_TO_DATE('14/12/2013', '%d/%m/%Y'), STR_TO_DATE('14/12/2013', '%d/%m/%Y'), NULL, 'Membership card lost');

INSERT INTO PAYMENT_REFERENCES (REFERENCE, IS_ACTIVE, NAME, MEMBER_ID)
SELECT 'FPS CREDIT CC LIFELINE', TRUE, 'JOHN BROWN', ID FROM MEMBERS WHERE MEMBERSHIP_NUMBER = 1580;

INSERT INTO PAYMENTS (PAYMENT_DATE, PAYMENT_AMOUNT, CREDIT_REFERENCE, NAME, CREDITED_ACCOUNT, MEMBER_ID)
VALUES (STR_TO_DATE('14/05/2017', '%d/%m/%Y'), 20.00, 'FPS CREDIT 1161', 'J NOMAN', '91231800145173BB', NULL);

INSERT INTO PAYMENTS (PAYMENT_DATE, PAYMENT_AMOUNT, CREDIT_REFERENCE, NAME, CREDITED_ACCOUNT, MEMBER_ID)
VALUES (STR_TO_DATE('14/05/2017', '%d/%m/%Y'), 20.00, 'FPS CREDIT 1578', '', '91231800145173BB', NULL);

INSERT INTO PAYMENTS (PAYMENT_DATE, PAYMENT_AMOUNT, CREDIT_REFERENCE, NAME, CREDITED_ACCOUNT, MEMBER_ID)
VALUES (STR_TO_DATE('14/05/2017', '%d/%m/%Y'), 20.23, 'FPS CREDIT 1570', 'MARSHALL', '91231800145173BB', NULL);

INSERT INTO PAYMENTS (PAYMENT_DATE, PAYMENT_AMOUNT, CREDIT_REFERENCE, NAME, CREDITED_ACCOUNT, MEMBER_ID)
VALUES (STR_TO_DATE('14/05/2017', '%d/%m/%Y'), 20.00, 'FPS CREDIT 1568', 'REID', '91231800145173BB', NULL);

INSERT INTO PAYMENTS (PAYMENT_DATE, PAYMENT_AMOUNT, CREDIT_REFERENCE, NAME, CREDITED_ACCOUNT, MEMBER_ID)
VALUES (STR_TO_DATE('14/05/2017', '%d/%m/%Y'), 20.00, 'FPS CREDIT 9988', 'P NORSEMAN', '91231800145173BB', NULL);

INSERT INTO PAYMENTS (PAYMENT_DATE, PAYMENT_AMOUNT, CREDIT_REFERENCE, NAME, CREDITED_ACCOUNT, MEMBER_ID)
VALUES (STR_TO_DATE('14/05/2017', '%d/%m/%Y'), 20.00, 'FPS CREDIT 8800', 'J NORMAN', '91231800145173BB', NULL);

INSERT INTO LOTTERY_DRAWS (DRAW_DATE, NAME, LOTTERY_DATE, DRAW_MASTER)
VALUES (STR_TO_DATE('01/05/2017', '%d/%m/%Y'), 'MAYDAY MAYHEM', STR_TO_DATE('14/05/2017', '%d/%m/%Y'), 'Ross');

INSERT INTO PRIZES(PRIZE, LOTTERY_DRAW_ID, WINNER_ID, PRIZE_COLLECTED, PRIZE_COLLECTED_DATE)
SELECT 'Ford Ka', LD.ID, M.ID, TRUE, STR_TO_DATE('03/05/2017', '%d/%m/%Y') FROM LOTTERY_DRAWS LD, MEMBERS M WHERE LD.NAME = 'MAYDAY MAYHEM' AND M.MEMBERSHIP_NUMBER = 1578;

INSERT INTO PRIZES(PRIZE, LOTTERY_DRAW_ID, WINNER_ID, PRIZE_COLLECTED, PRIZE_COLLECTED_DATE)
SELECT '£250', LD.ID, M.ID, TRUE, STR_TO_DATE('14/05/2017', '%d/%m/%Y') FROM LOTTERY_DRAWS LD, MEMBERS M WHERE LD.NAME = 'MAYDAY MAYHEM' AND M.MEMBERSHIP_NUMBER = 1569;

INSERT INTO PRIZES(PRIZE, LOTTERY_DRAW_ID, WINNER_ID, PRIZE_COLLECTED, PRIZE_COLLECTED_DATE)
SELECT '£100', LD.ID, M.ID, TRUE, STR_TO_DATE('12/05/2017', '%d/%m/%Y') FROM LOTTERY_DRAWS LD, MEMBERS M WHERE LD.NAME = 'MAYDAY MAYHEM' AND M.MEMBERSHIP_NUMBER = 1244;

INSERT INTO ADDRESSES (ADDRESS_LINE_1, TOWN, REGION, POSTCODE, COUNTRY, IS_ACTIVE, MEMBER_ID)
SELECT CONCAT(ID, ' High Street'),'Dunfermline','Fife', 'KY12 9YY', 'Scotland', TRUE, ID
FROM MEMBERS;

INSERT INTO SECURITY_SUBJECTS (FORENAME, SURNAME, USERNAME, PASSWORD)
VALUES ('Ross', 'Lindsay', 'Ross', '$2a$10$A8hKMalj4ioo1UA64QDA2.x3wC/21zX5QUagcvJxbRBj5H7bq58Mu');

INSERT INTO SECURITY_SUBJECTS (FORENAME, SURNAME, USERNAME, PASSWORD)
VALUES ('Steve', 'Blance', 'Steve', '$2a$10$AYZtnmn.elowcWkiRyD8y.atpi4eFj8/sNV3.QFcs5ybeVIccr5AG');

INSERT INTO NOTIFICATIONS (EVENT_TYPE, EVENT_DATE, DESCRIPTION)
VALUES ('Twitter', STR_TO_DATE('06/06/2017', '%d/%m/%Y'), '10 new followers');

INSERT INTO NOTIFICATIONS(EVENT_TYPE, EVENT_DATE, DESCRIPTION)
VALUES('Announcement', STR_TO_DATE('06/06/2017', '%d/%m/%Y'), 'New Lifeline System Now Live!');

INSERT INTO NOTIFICATIONS (EVENT_TYPE, EVENT_DATE, DESCRIPTION)
VALUES ('Announcement', STR_TO_DATE('03/05/2017', '%d/%m/%Y'), 'Lifeline Launched');

INSERT INTO CONFIGURATION(NAME, DATE_VALUE)
VALUES('LAST_ELIGIBILITY_REFRESH_DATE', NOW());

INSERT INTO CONFIGURATION(NAME, BOOLEAN_VALUE)
VALUES('ELIGIBILITY_REFRESH_REQUIRED', TRUE);
