CREATE  TABLE QUESTION(
    ID INT auto_increment primary  key ,
    TITLE VARCHAR(50),
    desciption TEXT,
    GMT_CREATE BIGINT,
    GMT_MODIFIED BIGINT,
    CREATOR INT,
    COMMENT_COUNT INT DEFAULT 0,
    VIEW_COUNT INT DEFAULT 0,
    LIKE_COUNT INT DEFAULT 0,
    TAG VARCHAR(256)
)