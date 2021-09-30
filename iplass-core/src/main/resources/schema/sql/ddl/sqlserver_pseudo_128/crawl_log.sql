use mtdb
GO

DROP TABLE CRAWL_LOG
GO

CREATE TABLE CRAWL_LOG
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    OBJ_DEF_VER NUMERIC(10,0) NOT NULL,
    CRE_DATE DATETIME2(3),
    UP_DATE DATETIME2(3)
)
GO

CREATE INDEX CRAWL_LOG_INDEX1 ON CRAWL_LOG (TENANT_ID, OBJ_DEF_ID, OBJ_DEF_VER)
GO