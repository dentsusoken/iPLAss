DROP TABLE "OBJ_META" CASCADE CONSTRAINT;
CREATE TABLE "OBJ_META"
(
    "TENANT_ID" NUMBER(7,0) NOT NULL,
    "OBJ_DEF_ID" VARCHAR2(128 BYTE) NOT NULL,
    "OBJ_DEF_VER" NUMBER(10,0) NOT NULL,
    "OBJ_DEF_PATH" VARCHAR2(1024 BYTE) NOT NULL,
    "OBJ_DEF_DISP_NAME" VARCHAR2(1024 BYTE),
    "OBJ_DESC" VARCHAR2(1024 BYTE),
    "OBJ_META_DATA" BLOB NOT NULL,
    "STATUS" VARCHAR2(1 BYTE),
    "SHARABLE" VARCHAR2(1 BYTE),
    "OVERWRITABLE" VARCHAR2(1 BYTE),
    "CRE_USER" VARCHAR2(64 BYTE),
    "CRE_DATE" TIMESTAMP (3) NOT NULL,
    "UP_USER" VARCHAR2(64 BYTE),
    "UP_DATE" TIMESTAMP (3) NOT NULL,
     CONSTRAINT "OBJ_META_PK" PRIMARY KEY ("TENANT_ID", "OBJ_DEF_ID", "OBJ_DEF_VER")
);


  CREATE INDEX "OBJ_META_INDEX1" ON "OBJ_META" ("TENANT_ID", "OBJ_DEF_PATH", "STATUS")
;

