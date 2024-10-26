CREATE TABLE USER
(
    ID       BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    NICKNAME VARCHAR(255) NOT NULL UNIQUE COMMENT 'Nickname',
    USER_TYPE ENUM('ADMIN','USER') NOT NULL COMMENT 'User Type',
    CT_UTC   TIMESTAMP    NOT NULL COMMENT 'Creation Time',
    UT_UTC   TIMESTAMP    NOT NULL COMMENT 'Update Time'
);