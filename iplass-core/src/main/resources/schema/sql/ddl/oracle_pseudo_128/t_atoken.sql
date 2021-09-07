DROP TABLE "T_ATOKEN" CASCADE CONSTRAINT;
CREATE TABLE "T_ATOKEN" 
(
    "TENANT_ID" NUMBER(7) NOT NULL,
    "T_TYPE" VARCHAR2(32 BYTE) NOT NULL,
    "U_KEY" VARCHAR2(128 BYTE) NOT NULL, 
    "SERIES" VARCHAR2(256 BYTE) NOT NULL, 
    "TOKEN" VARCHAR2(256 BYTE) NOT NULL, 
    "POL_NAME" VARCHAR2(128 BYTE),
    "S_DATE" TIMESTAMP (3),
    "T_INFO" BLOB,
    CONSTRAINT "T_ATOKEN_PK" PRIMARY KEY ("TENANT_ID", "T_TYPE", "SERIES")
)
;

CREATE INDEX "T_ATOKEN_INDEX1" ON "T_ATOKEN" ("TENANT_ID", "T_TYPE", "U_KEY");
CREATE INDEX "T_ATOKEN_INDEX2" ON "T_ATOKEN" ("TENANT_ID", "U_KEY");
