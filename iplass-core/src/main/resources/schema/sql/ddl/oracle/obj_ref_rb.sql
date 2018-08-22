/* drop/create OBJ_REF_RB */
DROP TABLE "OBJ_REF_RB" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_REF_RB" 
(
    "RB_ID" NUMBER(16,0),
    "TENANT_ID" NUMBER(7,0) NOT NULL, 
    "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
    "REF_DEF_ID" VARCHAR2(128) NOT NULL, 
    "OBJ_ID" VARCHAR2(64) NOT NULL, 
    "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
    "TARGET_OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
    "TARGET_OBJ_ID" VARCHAR2(64) NOT NULL,
    "TARGET_OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL
)
;
CREATE INDEX "OBJ_REF_RB_INDEX1" ON "OBJ_REF_RB" ("TENANT_ID", "OBJ_DEF_ID", "RB_ID") ;

/* drop/create OBJ_REF_RB */
DROP TABLE "OBJ_REF_RB__MTP" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_REF_RB__MTP" 
(
    "RB_ID" NUMBER(16,0),
    "TENANT_ID" NUMBER(7,0) NOT NULL, 
    "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
    "REF_DEF_ID" VARCHAR2(128) NOT NULL, 
    "OBJ_ID" VARCHAR2(64) NOT NULL, 
    "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
    "TARGET_OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
    "TARGET_OBJ_ID" VARCHAR2(64) NOT NULL,
    "TARGET_OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL
)
;
CREATE INDEX "OBJ_REF_RB__MTP_INDEX1" ON "OBJ_REF_RB__MTP" ("TENANT_ID", "OBJ_DEF_ID", "RB_ID") ;

/* drop/create OBJ_REF_RB */
DROP TABLE "OBJ_REF_RB__USER" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_REF_RB__USER" 
(
    "RB_ID" NUMBER(16,0),
    "TENANT_ID" NUMBER(7,0) NOT NULL, 
    "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
    "REF_DEF_ID" VARCHAR2(128) NOT NULL, 
    "OBJ_ID" VARCHAR2(64) NOT NULL, 
    "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
    "TARGET_OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
    "TARGET_OBJ_ID" VARCHAR2(64) NOT NULL,
    "TARGET_OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL
)
;
CREATE INDEX "OBJ_REF_RB__USER_INDEX1" ON "OBJ_REF_RB__USER" ("TENANT_ID", "OBJ_DEF_ID", "RB_ID") ;

