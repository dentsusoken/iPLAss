DROP TABLE "OBJ_STORE_TMP" CASCADE CONSTRAINT;
CREATE GLOBAL TEMPORARY TABLE "OBJ_STORE_TMP" 
(
    "OBJ_ID" VARCHAR2(64) NOT NULL, 
    "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL
)
;
