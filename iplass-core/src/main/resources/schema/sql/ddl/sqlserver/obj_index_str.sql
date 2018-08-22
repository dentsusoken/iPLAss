use mtdb
GO

DROP TABLE OBJ_INDEX_STR
GO

CREATE TABLE OBJ_INDEX_STR
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL NVARCHAR(365) -- 最大キー長は900バイトのため
)
GO

CREATE INDEX OBJ_INDEX_STR_INDEX1 ON OBJ_INDEX_STR (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_STR_INDEX2 ON OBJ_INDEX_STR (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO
