# noinspection SqlNoDataSourceInspectionForFile

DROP TABLE MEMBERS;

CREATE TABLE MEMBERS (
    ID                         INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    MEMBERSHIP_NUMBER          INT         NOT NULL,
    MEMBERSHIP_TYPE            VARCHAR(50) NOT NULL,
    STATUS                     VARCHAR(50) NOT NULL DEFAULT 'OPEN',
    FORENAME                   VARCHAR(50) NOT NULL,
    SURNAME                    VARCHAR(50) NOT NULL,
    PAYER_TYPE                 VARCHAR(20) NOT NULL DEFAULT 'MONTHLY',
    JOIN_DATE                  DATE,
    LEAVE_DATE                 DATE,
    EMAIL                      VARCHAR(255),
    LANDLINE_NUMBER            VARCHAR(50),
    MOBILE_NUMBER              VARCHAR(50),
    CARD_REQUEST_DATE          DATE,
    CARD_ISSUED_DATE           DATE,
    WELCOME_LETTER_ISSUED_DATE DATE,
    COMMENTS                   VARCHAR(2000)
);

DROP TABLE ADDRESSES;

CREATE TABLE ADDRESSES (
    ID             INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    ADDRESS_LINE_1 VARCHAR(255) NOT NULL,
    ADDRESS_LINE_2 VARCHAR(255),
    ADDRESS_LINE_3 VARCHAR(255),
    TOWN           VARCHAR(255) NOT NULL,
    REGION         VARCHAR(50)  NOT NULL,
    POSTCODE       VARCHAR(10),
    COUNTRY        VARCHAR(50),
    IS_ACTIVE      BOOLEAN,
    MEMBER_ID      INT          NOT NULL REFERENCES MEMBERS(ID)
);

DROP TABLE PAYMENTS;

CREATE TABLE PAYMENTS (
    ID               INT           NOT NULL AUTO_INCREMENT PRIMARY KEY,
    PAYMENT_DATE     DATE          NOT NULL,
    PAYMENT_AMOUNT   DECIMAL(10,2) NOT NULL,
    CREDIT_REFERENCE VARCHAR(200),
    NAME             VARCHAR(200),
    CREDITED_ACCOUNT VARCHAR(50),
    MEMBER_ID        INT REFERENCES MEMBERS(ID)
);

DROP TABLE PAYMENT_REFERENCES;

CREATE TABLE PAYMENT_REFERENCES (
    ID        INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    REFERENCE VARCHAR(255) NOT NULL,
    IS_ACTIVE BOOLEAN      NOT NULL,
    NAME      VARCHAR(255),
    MEMBER_ID INT          NOT NULL REFERENCES MEMBERS(ID)
);

DROP TABLE LOTTERY_DRAWS;

CREATE TABLE LOTTERY_DRAWS (
    ID          INT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    DRAW_DATE   DATE NOT NULL,
    NAME        VARCHAR(255),
    DRAW_MASTER VARCHAR(255)
);


DROP TABLE PRIZES;

CREATE TABLE PRIZES (
    ID                   INT     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    PRIZE                VARCHAR(1000),
    LOTTERY_DRAW_ID      INT     NOT NULL REFERENCES LOTTERY_DRAWS(ID),
    WINNER_ID            INT REFERENCES MEMBER(ID),
    PRIZE_COLLECTED      BOOLEAN NOT NULL,
    PRIZE_COLLECTED_DATE DATE
);

DROP TABLE NOTIFICATIONS;

CREATE TABLE NOTIFICATIONS (
  ID          INT           NOT NULL AUTO_INCREMENT PRIMARY KEY,
  EVENT_TYPE  VARCHAR(50)   NOT NULL,
  EVENT_DATE  DATE          NOT NULL,
  DESCRIPTION VARCHAR(1000) NOT NULL
);

DROP TABLE SECURITY_SUBJECTS;

CREATE TABLE SECURITY_SUBJECTS (
  ID       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  FORENAME VARCHAR(100) NOT NULL,
  SURNAME  VARCHAR(100) NOT NULL,
  USERNAME VARCHAR(100) NOT NULL,
  PASSWORD VARCHAR(100) NOT NULL,
  UNIQUE KEY `USERNAME_UNIQUE` (`USERNAME`)
);

CREATE INDEX MEMBER_ADDRESS_IDX ON ADDRESSES (MEMBER_ID);

CREATE INDEX MEMBER_PRIZES_IDX ON PRIZES (WINNER_ID);

CREATE INDEX MEMBER_REFERENCES_IDX ON PAYMENT_REFERENCES (MEMBER_ID);

CREATE INDEX MEMBER_PAYMENTS_IDX ON PAYMENTS (MEMBER_ID);

CREATE UNIQUE INDEX MEMBER_MEMBERSHIP_NUMBER ON MEMBERS (MEMBERSHIP_NUMBER);

CREATE INDEX PAYMENT_DATE_IDX ON PAYMENTS (PAYMENT_DATE);

CREATE INDEX MEMBER_SURNAME_IDX ON MEMBERS (SURNAME);

CREATE INDEX MEMBER_FORENAME_IDX ON MEMBERS (FORENAME);

CREATE INDEX MEMBER_STATUS_IDX ON MEMBERS (STATUS);

CREATE INDEX SECURITY_SUBJECT_NAME_IDX ON SECURITY_SUBJECTS (USERNAME);