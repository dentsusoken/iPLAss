use mtdb
GO

DROP TABLE CACHE_STORE
GO

CREATE TABLE CACHE_STORE
(
    NS VARCHAR(256) NOT NULL,
    C_KEY NVARCHAR(322) NOT NULL,
    C_VAL VARBINARY(MAX),
    VER NUMERIC(19,0),
    CRE_TIME NUMERIC(19,0),
    INV_TIME NUMERIC(19,0),
    CI_0 NVARCHAR(322),
    CI_1 NVARCHAR(322),
    CI_2 NVARCHAR(322),
    CONSTRAINT CACHE_STORE_PK PRIMARY KEY (NS, C_KEY)
)

GO
CREATE INDEX CACHE_STORE_INDEX0 ON CACHE_STORE (NS, CI_0)
GO
CREATE INDEX CACHE_STORE_INDEX1 ON CACHE_STORE (NS, CI_1)
GO
CREATE INDEX CACHE_STORE_INDEX2 ON CACHE_STORE (NS, CI_2)
GO
