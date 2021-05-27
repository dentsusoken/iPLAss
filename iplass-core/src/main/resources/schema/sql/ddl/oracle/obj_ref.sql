/* drop/create OBJ_REF */
DROP TABLE "OBJ_REF" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_REF" 
(
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

CREATE INDEX "OBJ_REF_INDEX1" ON "OBJ_REF" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID", "REF_DEF_ID") ;
CREATE INDEX "OBJ_REF_INDEX2" ON "OBJ_REF" ("TENANT_ID", "TARGET_OBJ_DEF_ID", "TARGET_OBJ_ID", "REF_DEF_ID") ;

/* drop/create OBJ_REF */
DROP TABLE "OBJ_REF__MTP" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_REF__MTP" 
(
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

CREATE INDEX "OBJ_REF__MTP_INDEX1" ON "OBJ_REF__MTP" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID", "REF_DEF_ID") ;
CREATE INDEX "OBJ_REF__MTP_INDEX2" ON "OBJ_REF__MTP" ("TENANT_ID", "TARGET_OBJ_DEF_ID", "TARGET_OBJ_ID", "REF_DEF_ID") ;

/* drop/create OBJ_REF */
DROP TABLE "OBJ_REF__USER" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_REF__USER" 
(
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

CREATE INDEX "OBJ_REF__USER_INDEX1" ON "OBJ_REF__USER" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID", "REF_DEF_ID") ;
CREATE INDEX "OBJ_REF__USER_INDEX2" ON "OBJ_REF__USER" ("TENANT_ID", "TARGET_OBJ_DEF_ID", "TARGET_OBJ_ID", "REF_DEF_ID") ;

