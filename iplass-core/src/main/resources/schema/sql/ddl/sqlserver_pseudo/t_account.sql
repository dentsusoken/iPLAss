use mtdb
GO

DROP TABLE T_ACCOUNT
GO

CREATE TABLE T_ACCOUNT
(
    TENANT_ID NUMERIC(7) NOT NULL,
    ACCOUNT_ID VARCHAR(128) NOT NULL,
    PASSWORD VARCHAR(128) NOT NULL,
    SALT VARCHAR(64),
    OID VARCHAR(128) NOT NULL,
    LAST_LOGIN_ON DATETIME2(3),
    LAST_PASSWORD_CHANGE DATE,
    LOGIN_ERR_CNT NUMERIC(2,0) DEFAULT 0 NOT NULL,
    LOGIN_ERR_DATE DATETIME2(3),
    POL_NAME VARCHAR(128),
    CRE_USER VARCHAR(64),
    CRE_DATE DATETIME2(3),
    UP_USER VARCHAR(64),
    UP_DATE DATETIME2(3),
    CONSTRAINT T_ACCOUNT_PK PRIMARY KEY (TENANT_ID, ACCOUNT_ID),
    CONSTRAINT T_ACCOUNT_UQ UNIQUE (TENANT_ID, OID)
)
GO
