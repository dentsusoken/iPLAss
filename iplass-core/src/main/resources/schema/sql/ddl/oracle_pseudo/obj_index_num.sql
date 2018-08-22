/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM_INDEX1" ON "OBJ_INDEX_NUM" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM_INDEX2" ON "OBJ_INDEX_NUM" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__1" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__1" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__1_INDEX1" ON "OBJ_INDEX_NUM__1" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__1_INDEX2" ON "OBJ_INDEX_NUM__1" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__2" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__2" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__2_INDEX1" ON "OBJ_INDEX_NUM__2" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__2_INDEX2" ON "OBJ_INDEX_NUM__2" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__3" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__3" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__3_INDEX1" ON "OBJ_INDEX_NUM__3" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__3_INDEX2" ON "OBJ_INDEX_NUM__3" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__4" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__4" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__4_INDEX1" ON "OBJ_INDEX_NUM__4" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__4_INDEX2" ON "OBJ_INDEX_NUM__4" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__5" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__5" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__5_INDEX1" ON "OBJ_INDEX_NUM__5" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__5_INDEX2" ON "OBJ_INDEX_NUM__5" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__6" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__6" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__6_INDEX1" ON "OBJ_INDEX_NUM__6" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__6_INDEX2" ON "OBJ_INDEX_NUM__6" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__7" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__7" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__7_INDEX1" ON "OBJ_INDEX_NUM__7" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__7_INDEX2" ON "OBJ_INDEX_NUM__7" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__8" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__8" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__8_INDEX1" ON "OBJ_INDEX_NUM__8" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__8_INDEX2" ON "OBJ_INDEX_NUM__8" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__9" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__9" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__9_INDEX1" ON "OBJ_INDEX_NUM__9" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__9_INDEX2" ON "OBJ_INDEX_NUM__9" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__10" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__10" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__10_INDEX1" ON "OBJ_INDEX_NUM__10" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__10_INDEX2" ON "OBJ_INDEX_NUM__10" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__11" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__11" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__11_INDEX1" ON "OBJ_INDEX_NUM__11" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__11_INDEX2" ON "OBJ_INDEX_NUM__11" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__12" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__12" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__12_INDEX1" ON "OBJ_INDEX_NUM__12" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__12_INDEX2" ON "OBJ_INDEX_NUM__12" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__13" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__13" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__13_INDEX1" ON "OBJ_INDEX_NUM__13" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__13_INDEX2" ON "OBJ_INDEX_NUM__13" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__14" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__14" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__14_INDEX1" ON "OBJ_INDEX_NUM__14" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__14_INDEX2" ON "OBJ_INDEX_NUM__14" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__15" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__15" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__15_INDEX1" ON "OBJ_INDEX_NUM__15" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__15_INDEX2" ON "OBJ_INDEX_NUM__15" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__16" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__16" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__16_INDEX1" ON "OBJ_INDEX_NUM__16" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__16_INDEX2" ON "OBJ_INDEX_NUM__16" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__17" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__17" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__17_INDEX1" ON "OBJ_INDEX_NUM__17" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__17_INDEX2" ON "OBJ_INDEX_NUM__17" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__18" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__18" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__18_INDEX1" ON "OBJ_INDEX_NUM__18" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__18_INDEX2" ON "OBJ_INDEX_NUM__18" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__19" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__19" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__19_INDEX1" ON "OBJ_INDEX_NUM__19" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__19_INDEX2" ON "OBJ_INDEX_NUM__19" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__20" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__20" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__20_INDEX1" ON "OBJ_INDEX_NUM__20" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__20_INDEX2" ON "OBJ_INDEX_NUM__20" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__21" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__21" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__21_INDEX1" ON "OBJ_INDEX_NUM__21" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__21_INDEX2" ON "OBJ_INDEX_NUM__21" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__22" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__22" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__22_INDEX1" ON "OBJ_INDEX_NUM__22" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__22_INDEX2" ON "OBJ_INDEX_NUM__22" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__23" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__23" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__23_INDEX1" ON "OBJ_INDEX_NUM__23" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__23_INDEX2" ON "OBJ_INDEX_NUM__23" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__24" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__24" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__24_INDEX1" ON "OBJ_INDEX_NUM__24" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__24_INDEX2" ON "OBJ_INDEX_NUM__24" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__25" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__25" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__25_INDEX1" ON "OBJ_INDEX_NUM__25" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__25_INDEX2" ON "OBJ_INDEX_NUM__25" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__26" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__26" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__26_INDEX1" ON "OBJ_INDEX_NUM__26" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__26_INDEX2" ON "OBJ_INDEX_NUM__26" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__27" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__27" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__27_INDEX1" ON "OBJ_INDEX_NUM__27" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__27_INDEX2" ON "OBJ_INDEX_NUM__27" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__28" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__28" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__28_INDEX1" ON "OBJ_INDEX_NUM__28" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__28_INDEX2" ON "OBJ_INDEX_NUM__28" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__29" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__29" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__29_INDEX1" ON "OBJ_INDEX_NUM__29" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__29_INDEX2" ON "OBJ_INDEX_NUM__29" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__30" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__30" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__30_INDEX1" ON "OBJ_INDEX_NUM__30" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__30_INDEX2" ON "OBJ_INDEX_NUM__30" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

/* drop/create OBJ_INDEX_NUM */
DROP TABLE "OBJ_INDEX_NUM__31" CASCADE CONSTRAINT;

CREATE TABLE "OBJ_INDEX_NUM__31" 
(       "TENANT_ID" NUMBER(7,0) NOT NULL, 
        "OBJ_DEF_ID" VARCHAR2(128) NOT NULL, 
        "COL_NAME" VARCHAR2(36) NOT NULL, 
        "OBJ_ID" VARCHAR2(64) NOT NULL,
        "OBJ_VER" NUMBER(10,0) DEFAULT 0 NOT NULL,
        "VAL" NUMBER
)
;

CREATE INDEX "OBJ_INDEX_NUM__31_INDEX1" ON "OBJ_INDEX_NUM__31" ("TENANT_ID", "OBJ_DEF_ID", "COL_NAME", "VAL") ;
CREATE INDEX "OBJ_INDEX_NUM__31_INDEX2" ON "OBJ_INDEX_NUM__31" ("TENANT_ID", "OBJ_DEF_ID", "OBJ_ID") ;

