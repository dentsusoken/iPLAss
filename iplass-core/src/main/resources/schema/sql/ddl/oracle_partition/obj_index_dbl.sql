DROP TABLE "OBJ_INDEX_DBL" CASCADE CONSTRAINT;
CREATE TABLE "OBJ_INDEX_DBL" 
   (    "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" BINARY_DOUBLE
   )
   PARTITION BY RANGE (TENANT_ID) INTERVAL(1)
SUBPARTITION BY HASH (OBJ_DEF_ID) SUBPARTITIONS 8
(
    PARTITION OBJ_INDEX_DBL_0 VALUES LESS THAN (1)
)
;
 
CREATE INDEX "OBJ_INDEX_DBL_INDEX1" ON "OBJ_INDEX_DBL" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") LOCAL;
CREATE INDEX "OBJ_INDEX_DBL_INDEX2" ON "OBJ_INDEX_DBL" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") LOCAL;
