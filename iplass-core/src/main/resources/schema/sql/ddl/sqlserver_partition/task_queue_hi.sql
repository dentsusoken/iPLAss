use mtdb
GO

DROP TABLE TASK_QUEUE_HI
GO

CREATE TABLE TASK_QUEUE_HI
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    Q_ID NUMERIC(7,0) NOT NULL,
    TASK_ID NUMERIC(16,0) NOT NULL,
    V_TIME NUMERIC(16,0) NOT NULL,
    STATUS CHAR(1) NOT NULL,
    G_KEY VARCHAR(128),
    VW_ID NUMERIC(7,0),
    EXP_MODE CHAR(1) NOT NULL,
    RES_FLG CHAR(1) NOT NULL,
    VER NUMERIC(16,0) NOT NULL,
    UP_DATE DATETIME2(3) NOT NULL,
    SERVER_ID VARCHAR(128),
    RE_CNT NUMERIC(7,0),
    CALLABLE VARBINARY(MAX),
    RES VARBINARY(MAX),
    CONSTRAINT TASK_QUEUE_HI_PK PRIMARY KEY (Q_ID, TENANT_ID, TASK_ID)
)
ON PS_MTDB (TENANT_ID)
GO
