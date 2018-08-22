DROP TABLE "OBJ_UNIQUE_TS" CASCADE CONSTRAINT;
CREATE TABLE "OBJ_UNIQUE_TS" 
(
    "TENANT_ID" NUMBER(7,0) NOT NULL, 
    "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
    "COL_NAME" VARCHAR2(36) NOT NULL, 
    "OBJ_ID" VARCHAR2(64) NOT NULL,
    "VAL" TIMESTAMP(3)
)
;
 
CREATE UNIQUE INDEX "OBJ_UNIQUE_TS_INDEX1" ON "OBJ_UNIQUE_TS" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL");
CREATE INDEX "OBJ_UNIQUE_TS_INDEX2" ON "OBJ_UNIQUE_TS" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID");
