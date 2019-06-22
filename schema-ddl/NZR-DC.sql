/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     12/21/2012 7:46:04 PM                        */
/*==============================================================*/
DROP TABLE NZR_DC_BATCH CASCADE CONSTRAINTS;

DROP TABLE NZR_DC_BATCH_DETAIL CASCADE CONSTRAINTS;

DROP TABLE NZR_DC_CONSTRAINT_HIST CASCADE CONSTRAINTS;

DROP TABLE NZR_DC_CONSTRAINT_ATTR_HIST CASCADE CONSTRAINTS;

DROP TABLE NZR_DC_DETAIL CASCADE CONSTRAINTS;

DROP TABLE NZR_DC_DETAIL_HIST CASCADE CONSTRAINTS;

DROP TABLE NZR_DC_DETAIL_ATTR_HIST CASCADE CONSTRAINTS;

DROP TABLE NZR_DC_SET CASCADE CONSTRAINTS;

DROP TABLE NZR_HOST CASCADE CONSTRAINTS;

DROP TABLE NZR_STG_FILE CASCADE CONSTRAINTS;


DROP TABLE NZR_STG_FOLDER CASCADE CONSTRAINTS;

DROP TABLE NZR_WRK_CONSTRAINTS CASCADE CONSTRAINTS;

DROP TABLE NZR_WRK_CONSTRAINT_ATTR CASCADE CONSTRAINTS;

DROP TABLE NZR_WRK_TABLE CASCADE CONSTRAINTS;

DROP TABLE NZR_WRK_TABLE_ATTR CASCADE CONSTRAINTS;

DROP TABLE NZR_SCHEDULES CASCADE CONSTRAINTS;

/*==============================================================*/
/* Table: NZR_DC_BATCH                                          */
/*==============================================================*/
CREATE TABLE NZR_DC_BATCH 
(
   DC_BATCH_SKEY        INTEGER              NOT NULL,
   DC_SKEY              INTEGER              NOT NULL,
   HIGH_STABLE_XID      INTEGER              NOT NULL,
   DC_BATCH_START_TS    TIMESTAMP            NOT NULL,
   DC_BATCH_END_TS      TIMESTAMP                NULL,
   DC_BATCH_STATUS      VARCHAR2(10)         NOT NULL
        CONSTRAINT CKC_BATCH_STATUS_NZR_DC CHECK	(DC_BATCH_STATUS IN ('RUNNING','COMPLETED', 'ERROR')),
   DC_BATCH_TYPE        CHAR(1)              NOT NULL
        CONSTRAINT CKC_BATCH_TYPE_NZR_DC CHECK (DC_BATCH_TYPE IN ('I','T', 'F')), 
   CONSTRAINT PK_NZR_DC_BATCH PRIMARY KEY (DC_BATCH_SKEY)
);

/*==============================================================*/
/* Index: NZR_BATCH_FK1_FK                                      */
/*==============================================================*/
CREATE INDEX NZR_BATCH_FK1_FK ON NZR_DC_BATCH (
   DC_SKEY ASC
);

/*==============================================================*/
/* Table: NZR_DC_BATCH_DETAIL                                   */
/*==============================================================*/
CREATE TABLE NZR_DC_BATCH_DETAIL 
(
   DC_BATCH_DTL_SKEY    INTEGER              NOT NULL,
   DC_BATCH_SKEY        INTEGER              NOT NULL,
   TABLE_SKEY           INTEGER              NOT NULL,
   SRC_OBJECT_ID        INTEGER              NOT NULL,
   START_TS             TIMESTAMP            NOT NULL,
   ENT_TS               TIMESTAMP,
   STATUS               VARCHAR2(10)         NOT NULL
     CONSTRAINT CKC_BATCH_DTL_STATUS_NZR_DC CHECK (STATUS IN ('RUNNING','COMPLETED', 'ERROR', 'INCOMPLETE')),
   INSERT_CNT           INTEGER              NOT NULL,
   DELETE_CNT           INTEGER              NOT NULL,
   DDL_FLAG             INTEGER              NOT NULL,
   LOW_STABLE_XID       INTEGER              NOT NULL,
   CONSTRAINT PK_NZR_DC_BATCH_DETAIL PRIMARY KEY (DC_BATCH_DTL_SKEY)
);

/*==============================================================*/
/* Index: NZR_BATCH_DETAIL_FK2_FK                               */
/*==============================================================*/
CREATE INDEX NZR_BATCH_DETAIL_FK2_FK ON NZR_DC_BATCH_DETAIL (
   DC_BATCH_SKEY ASC
);

/*==============================================================*/
/* Index: NZR_BATCH_DETAIL_FK1_FK                               */
/*==============================================================*/
CREATE INDEX NZR_BATCH_DETAIL_FK1_FK ON NZR_DC_BATCH_DETAIL (
   TABLE_SKEY ASC
);

/*==============================================================*/
/* Table: NZR_DC_CONSTRAINTS                                    */
/*==============================================================*/
CREATE TABLE NZR_DC_CONSTRAINT_HIST
(
   CONSTRAINT_SKEY           INTEGER              NOT NULL,
   TABLE_SKEY                    INTEGER              NOT NULL,
   DC_BATCH_DTL_SKEY        INTEGER                NOT NULL,
   DC_BATCH_SKEY               INTEGER                NOT NULL,
   CONSTRAINT_NAME           VARCHAR2(128)        NOT NULL,
   CONSTRAINT_TYPE            VARCHAR2(1)          NOT NULL,
   PK_TABLE_NAME                VARCHAR2(128),
   PK_TABLE_SKEY                 INTEGER,
   UPDATE_TYPE                   VARCHAR2(11),
   DELETE_TYPE                    VARCHAR2(11),
   MATCH_TYPE                     VARCHAR2(11),
   CONSTRAINT PK_NZR_DC_CONSTRAINTS PRIMARY KEY (CONSTRAINT_SKEY)
);

/*==============================================================*/
/* Index: NZR_DC_CONSTRAINT_FK1_FK                              */
/*==============================================================*/
CREATE INDEX NZR_DC_CONSTRAINT_FK1_FK ON NZR_DC_CONSTRAINT_HIST (
   TABLE_SKEY ASC
);

/*==============================================================*/
/* Index: NZR_DC_CONSTRAINT_FK2_FK                              */
/*==============================================================*/
CREATE INDEX NZR_DC_CONSTRAINT_FK2_FK ON NZR_DC_CONSTRAINT_HIST (
   PK_TABLE_SKEY ASC
);

/*==============================================================*/
/* Table: NZR_DC_CONSTRAINT_ATTRIBUTE                           */
/*==============================================================*/
CREATE TABLE NZR_DC_CONSTRAINT_ATTR_HIST
(
   TABLE_SKEY           INTEGER              NOT NULL,
   DC_BATCH_DTL_SKEY        INTEGER                NOT NULL,
   DC_BATCH_SKEY               INTEGER                NOT NULL,
   CONSTRAINT_SKEY      INTEGER              NOT NULL,
   ATTRIBUTE_NAME       VARCHAR2(128)        NOT NULL,
   CONSTRAINT_ATTRIBUTE_POSITION INTEGER              NOT NULL,
   PK_ATTRIBUTE_NAME    VARCHAR2(128),
   CONSTRAINT PK_NZR_DC_CONSTRAINT_ATTR PRIMARY KEY (CONSTRAINT_SKEY, ATTRIBUTE_NAME, DC_BATCH_SKEY)
);

/*==============================================================*/
/* Index: NZR_DC_CONSTRAINT_ATTR_FK1_FK                         */
/*==============================================================*/
CREATE INDEX NZR_DC_CONSTRAINT_ATTR_FK1_FK ON NZR_DC_CONSTRAINT_ATTR_HIST (
   CONSTRAINT_SKEY ASC
);

/*==============================================================*/
/* Index: NZR_DC_CONSTRAINT_ATTR_FK2_FK                         */
/*==============================================================*/
CREATE INDEX NZR_DC_CONSTRAINT_ATTR_FK2_FK ON NZR_DC_CONSTRAINT_ATTR_HIST (
   TABLE_SKEY ASC,
   ATTRIBUTE_NAME ASC
);

/*==============================================================*/
/* Table: NZR_DC_DETAIL                                         */
/*==============================================================*/
CREATE TABLE NZR_DC_DETAIL 
(
   TABLE_SKEY          INTEGER        NOT NULL,
   DC_SKEY              INTEGER                   NOT NULL,
   TABLE_NAME           VARCHAR2(128)        NOT NULL,
   ENABLED_FLAG         NUMBER     NOT NULL,
   WHERE_CLAUSE         VARCHAR2(1024),
   CONSTRAINT PK_NZR_DC_DETAIL PRIMARY KEY (TABLE_SKEY)
);


/*==============================================================*/
/* Table: NZR_DC_DETAIL_HIST                                         */
/*==============================================================*/
CREATE TABLE NZR_DC_DETAIL_HIST
(
   TABLE_SKEY                    INTEGER                NOT NULL,
   DC_BATCH_DTL_SKEY        INTEGER                NOT NULL,
   DC_BATCH_SKEY               INTEGER                NOT NULL,
   TABLE_NAME                    VARCHAR2(128)       NOT NULL,
   SRC_OBJECT_ID                INTEGER                NOT NULL,
   LAST_DDL_TS                  TIMESTAMP          ,
   COLUMN_CNT                   INTEGER            ,
   TABLE_COMMENT              VARCHAR2(4000),
   CONSTRAINT PK_NZR_DC_DETAIL_HIST PRIMARY KEY (TABLE_SKEY, DC_BATCH_SKEY)
);


/*==============================================================*/
/* Index: NZR_DC_DETAIL_FK1_FK                                  */
/*==============================================================*/
CREATE INDEX NZR_DC_DETAIL_FK1_FK ON NZR_DC_DETAIL (
   DC_SKEY ASC
);

/*==============================================================*/
/* Table: NZR_DC_DETAIL_ATTRIBUTES                              */
/*==============================================================*/
CREATE TABLE NZR_DC_DETAIL_ATTR_HIST
(
   TABLE_SKEY           INTEGER              NOT NULL,
   DC_BATCH_DTL_SKEY        INTEGER                NOT NULL,
   DC_BATCH_SKEY               INTEGER                NOT NULL,
   ATTRIBUTE_NAME       VARCHAR2(128)        NOT NULL,
   ATTRIBUTE_POSITION   INTEGER              NOT NULL,
   DATATYPE             VARCHAR2(200)        NOT NULL,
   DATA_LENGTH          INTEGER              NOT NULL,
   DATA_PRECISION       INTEGER              NOT NULL,
   DATA_SCALE           INTEGER              NOT NULL,
   NULLABLE             INTEGER              NOT NULL,
   DIST_POSITION        INTEGER,
   ORG_POSITION         INTEGER,
   DEFAULT_VALUE        VARCHAR2(200),
   COLUMN_COMMENT       VARCHAR2(4000),
   CONSTRAINT PK_NZR_DC_DETAIL_ATTRIBUTES PRIMARY KEY (TABLE_SKEY, ATTRIBUTE_NAME, DC_BATCH_SKEY)
);

/*==============================================================*/
/* Index: NZR_DC_DETAIL_ATTRIBUTE_FK1_FK                        */
/*==============================================================*/
CREATE INDEX NZR_DC_DETAIL_ATTRIBUTE_FK1_FK ON NZR_DC_DETAIL_ATTR_HIST (
   TABLE_SKEY ASC
);

/*==============================================================*/
/* Table: NZR_DC_SET                                            */
/*==============================================================*/
CREATE TABLE NZR_DC_SET 
(
   DC_SKEY              INTEGER              NOT NULL,
   SRC_HOST_SKEY        INTEGER              NOT NULL,
   DC_SET_NAME         VARCHAR2(128)        NOT NULL,
   SRC_DB_NAME          VARCHAR2(128)        NOT NULL,
   STG_LOCATION         VARCHAR2(10)         NOT NULL,
   STG_FORMAT           VARCHAR2(10)         NOT NULL,
   ENABLED_FLAG         INTEGER              NOT NULL
      CONSTRAINT NZR_DCSET_ENABLED_CHK CHECK (ENABLED_FLAG IN(1, 0)),
   PROCESS_DELETES_FLAG INTEGER              NOT NULL,
   CONSTRAINT PK_NZR_DC_SET PRIMARY KEY (DC_SKEY)
);

/*==============================================================*/
/* Index: NZR_DC_SET_FK1_FK                                     */
/*==============================================================*/
CREATE INDEX NZR_DC_SET_FK1_FK ON NZR_DC_SET (
   SRC_HOST_SKEY ASC
);

/*==============================================================*/
/* Table: NZR_HOST                                              */
/*==============================================================*/
CREATE TABLE NZR_HOST 
(
   HOST_SKEY            INTEGER              NOT NULL,
   HOST_NAME            VARCHAR2(128)        NOT NULL,
   DNS_IP               VARCHAR2(128)        NOT NULL,
   HOST_PORT            INTEGER              DEFAULT 5480 NOT NULL,
   USERNAME             VARCHAR2(30)         NOT NULL,
   PASSWORD             VARCHAR2(64)         NOT NULL,
   CONSTRAINT PK_NZR_HOST PRIMARY KEY (HOST_SKEY)
);



/*==============================================================*/
/* Table: NZR_STG_FILE                                          */
/*==============================================================*/
CREATE TABLE NZR_STG_FILE 
(
   FILE_SKEY                   INTEGER              NOT NULL,
   DC_BATCH_SKEY           INTEGER              NOT NULL,
   FOLDER_SKEY               INTEGER              NOT NULL,
   DC_BATCH_DTL_SKEY    INTEGER              NOT NULL,
   FILE_NAME                  VARCHAR2(128)        NOT NULL,
   FILE_FORMAT               VARCHAR2(10)               NOT NULL
      CONSTRAINT CKC_FILE_FORMAT_NZR_DC CHECK (FILE_FORMAT IN ('T', 'I', 'Z', 'SQL')),
   FILE_CONTENT              VARCHAR2(10)              NOT NULL
      CONSTRAINT CKC_FILE_CONTENT_NZR_DC CHECK (FILE_CONTENT IN ('DELETES','INSERTS', 'DDL_TABLE', 'DDL_PK', 'DDL_FK', 'DDL_MISC')),
   FILE_LOCATION             VARCHAR2(10)              NOT NULL
      CONSTRAINT CKC_FILE_LOCATION_NZR_DC CHECK (FILE_LOCATION IN ('LOCAL','REMOTE', 'NZR')),
   CONSTRAINT PK_NZR_STG_FILE PRIMARY KEY (FILE_SKEY)
);

	
/*==============================================================*/
/* Index: NZR_STG_FILE_FK3_FK                                   */
/*==============================================================*/
CREATE INDEX NZR_STG_FILE_FK3_FK ON NZR_STG_FILE (
   DC_BATCH_SKEY ASC
);

/*==============================================================*/
/* Index: NZR_STG_FILE_FK1_FK                                   */
/*==============================================================*/
CREATE INDEX NZR_STG_FILE_FK1_FK ON NZR_STG_FILE (
   DC_BATCH_DTL_SKEY ASC
);

/*==============================================================*/
/* Index: NZR_STG_FILE_FK2_FK                                   */
/*==============================================================*/
CREATE INDEX NZR_STG_FILE_FK2_FK ON NZR_STG_FILE (
   FOLDER_SKEY ASC
);

/*==============================================================*/
/* Table: NZR_STG_FOLDER                                        */
/*==============================================================*/
CREATE TABLE NZR_STG_FOLDER 
(
   FOLDER_SKEY          INTEGER              NOT NULL,
   DC_BATCH_SKEY        INTEGER              NOT NULL,
   FOLDER_NAME          VARCHAR2(256)        NOT NULL,
   CONSTRAINT PK_NZR_STG_FOLDER PRIMARY KEY (FOLDER_SKEY)
);

/*==============================================================*/
/* Index: NZR_STG_FOLDER_FK1_FK                                 */
/*==============================================================*/
CREATE INDEX NZR_STG_FOLDER_FK1_FK ON NZR_STG_FOLDER (
   DC_BATCH_SKEY ASC
);

/*==============================================================*/
/* Table: NZR_WRK_CONSTRAINTS                                   */
/*==============================================================*/
CREATE TABLE NZR_WRK_CONSTRAINTS 
(
   DC_SKEY              INTEGER              NOT NULL,
   DC_BATCH_SKEY        INTEGER              NOT NULL,
   TABLE_SKEY           INTEGER              NOT NULL,
   CONSTRAINT_NAME      VARCHAR2(128)        NOT NULL,
   CONSTRAINT_TYPE      VARCHAR2(1)          NOT NULL,
   PK_TABLE_NAME        VARCHAR2(128),
   UPDATE_TYPE          VARCHAR2(11),
   DELETE_TYPE          VARCHAR2(11),
   MATCH_TYPE           VARCHAR2(11),
   CONSTRAINT PK_NZR_WRK_CONSTRAINTS PRIMARY KEY (DC_BATCH_SKEY, TABLE_SKEY, DC_SKEY, CONSTRAINT_NAME)
);

/*==============================================================*/
/* Table: NZR_WRK_CONSTRAINT_ATTR                               */
/*==============================================================*/
CREATE TABLE NZR_WRK_CONSTRAINT_ATTR 
(
   DC_SKEY              INTEGER            NOT NULL,
   DC_BATCH_SKEY        INTEGER            NOT NULL,
   TABLE_SKEY           INTEGER            NOT NULL,
   CONSTRAINT_NAME      VARCHAR2(128)      NOT NULL,
   ATTRIBUTE_NAME       VARCHAR2(128)      NOT NULL,
   CONSTRAINT_ATTRIBUTE_POSITION INTEGER   NOT NULL,
   PK_ATTRIBUTE_NAME    VARCHAR2(128)
);

/*==============================================================*/
/* Table: NZR_WRK_TABLE                                         */
/*==============================================================*/
CREATE TABLE NZR_WRK_TABLE 
(
   DC_SKEY              INTEGER              NOT NULL,
   DC_BATCH_SKEY        INTEGER              NOT NULL,
   TABLE_SKEY           INTEGER              NOT NULL,
   TABLE_NAME           VARCHAR2(128)        NOT NULL,
   LAST_DDL_TS          TIMESTAMP            NOT NULL,
   SRC_OBJECT_ID        INTEGER              NOT NULL,
   COLUMN_CNT           INTEGER              NOT NULL,
   TABLE_COMMENT        VARCHAR2(4000),
   CONSTRAINT PK_NZR_WRK_TABLE PRIMARY KEY (DC_BATCH_SKEY, TABLE_SKEY, DC_SKEY)
);

/*==============================================================*/
/* Table: NZR_WRK_TABLE_ATTR                                    */
/*==============================================================*/
CREATE TABLE NZR_WRK_TABLE_ATTR 
(
   DC_SKEY              INTEGER              NOT NULL,
   DC_BATCH_SKEY        INTEGER              NOT NULL,
   TABLE_SKEY           INTEGER              NOT NULL,
   ATTRIBUTE_NAME       VARCHAR2(128)        NOT NULL,
   ATTRIBUTE_POSITION   INTEGER              NOT NULL,
   DATATYPE             VARCHAR2(200)        NOT NULL,
   DATA_LENGTH          INTEGER              NOT NULL,
   DATA_PRECISION       INTEGER              NOT NULL,
   DATA_SCALE           INTEGER              NOT NULL,
   NULLABLE             INTEGER              NOT NULL,
   DIST_POSITION        INTEGER,
   ORG_POSITION         INTEGER,
   DEFAULT_VALUE        VARCHAR2(200),
   COLUMN_COMMENT       VARCHAR2(4000),
   CONSTRAINT PK_NZR_WRK_TABLE_ATTR PRIMARY KEY (DC_BATCH_SKEY, TABLE_SKEY, DC_SKEY, ATTRIBUTE_NAME)
);

CREATE TABLE NZR_SCHEDULES
(
	SCHEDULER_SKEY     INTEGER      NOT NULL,
	DC_SKEY            INTEGER      NOT NULL,
	SCHEDULE_NAME      VARCHAR2(50) NOT NULL,
	START_HOUR         NUMBER  NOT NULL,	
	START_MINUTE      NUMBER NOT NULL,
	MONDAY_FLAG        CHAR(1)      NOT NULL,	-- Valid values: Y,N
	TUESDAY_FLAG       CHAR(1)      NOT NULL,	-- Valid values: Y,N
	WEDNESDAY_FLAG     CHAR(1)      NOT NULL,	-- Valid values: Y,N
	THURSDAY_FLAG      CHAR(1)      NOT NULL,	-- Valid values: Y,N
	FRIDAY_FLAG        CHAR(1)      NOT NULL,	-- Valid values: Y,N
	SATURDAY_FLAG      CHAR(1)      NOT NULL,	-- Valid values: Y,N
	SUNDAY_FLAG        CHAR(1)      NOT NULL,	-- Valid values: Y,N
	ENABLED_FLAG        NUMBER     NOT NULL	-- Valid values: Y,N
	       CONSTRAINT CKC_SCHEDULES_ENABLED CHECK	(ENABLED_FLAG IN(1, 0)),
	BATCH_TYPE         CHAR(1)      NULL,	-- Valid values: F,I,T
CONSTRAINT PK_NZR_SCHEDULES PRIMARY KEY (SCHEDULER_SKEY)
);
/*==============================================================*/
/* Index: NZR_SCHEDULE_FK1_FK                                   */
/*==============================================================*/
CREATE INDEX NZR_SCHEDULE_FK1_FK ON NZR_SCHEDULES (
   DC_SKEY ASC
);

ALTER TABLE NZR_SCHEDULES
   ADD CONSTRAINT NZR_SCHEDULE_FK1 FOREIGN KEY (DC_SKEY)
      REFERENCES NZR_DC_SET (DC_SKEY) ON DELETE CASCADE ;

ALTER TABLE NZR_DC_BATCH
   ADD CONSTRAINT NZR_BATCH_FK1 FOREIGN KEY (DC_SKEY)
      REFERENCES NZR_DC_SET (DC_SKEY) ON DELETE CASCADE ;

ALTER TABLE NZR_DC_BATCH_DETAIL
   ADD CONSTRAINT NZR_BATCH_DETAIL_FK1 FOREIGN KEY (TABLE_SKEY)
      REFERENCES NZR_DC_DETAIL (TABLE_SKEY) ON DELETE CASCADE;

ALTER TABLE NZR_DC_BATCH_DETAIL
   ADD CONSTRAINT NZR_BATCH_DETAIL_FK2 FOREIGN KEY (DC_BATCH_SKEY)
      REFERENCES NZR_DC_BATCH (DC_BATCH_SKEY) ON DELETE CASCADE;
      

ALTER TABLE NZR_DC_CONSTRAINT_HIST
   ADD CONSTRAINT NZR_DC_CONSTRAINT_FK1 FOREIGN KEY (TABLE_SKEY)
      REFERENCES NZR_DC_DETAIL (TABLE_SKEY) ON DELETE CASCADE;

ALTER TABLE NZR_DC_CONSTRAINT_HIST
   ADD CONSTRAINT NZR_DC_CONSTRAINT_FK2 FOREIGN KEY (DC_BATCH_SKEY)
      REFERENCES NZR_DC_BATCH (DC_BATCH_SKEY)
      ON DELETE CASCADE;
      
ALTER TABLE NZR_DC_CONSTRAINT_HIST
   ADD CONSTRAINT NZR_DC_CONSTRAINT_FK3 FOREIGN KEY (DC_BATCH_DTL_SKEY)
      REFERENCES NZR_DC_BATCH_DETAIL (DC_BATCH_DTL_SKEY)
      ON DELETE CASCADE;

ALTER TABLE NZR_DC_CONSTRAINT_ATTR_HIST
   ADD CONSTRAINT NZR_DC_CONSTRAINT_ATTR_FK1 FOREIGN KEY (CONSTRAINT_SKEY)
      REFERENCES NZR_DC_CONSTRAINT_HIST (CONSTRAINT_SKEY) ON DELETE CASCADE;

ALTER TABLE NZR_DC_CONSTRAINT_ATTR_HIST
   ADD CONSTRAINT NZR_DC_CONSTRAINT_ATTR_FK2 FOREIGN KEY (TABLE_SKEY, ATTRIBUTE_NAME, DC_BATCH_SKEY)
      REFERENCES NZR_DC_DETAIL_ATTR_HIST (TABLE_SKEY, ATTRIBUTE_NAME, DC_BATCH_SKEY) ON DELETE CASCADE;

ALTER TABLE NZR_DC_CONSTRAINT_ATTR_HIST
   ADD CONSTRAINT NZR_DC_CONSTRAINT_ATTR_FK3 FOREIGN KEY (DC_BATCH_SKEY)
      REFERENCES NZR_DC_BATCH (DC_BATCH_SKEY) ON DELETE CASCADE;
      
      
ALTER TABLE NZR_DC_CONSTRAINT_ATTR_HIST
   ADD CONSTRAINT NZR_DC_CONSTRAINT_ATTR_FK4 FOREIGN KEY (DC_BATCH_DTL_SKEY)
      REFERENCES NZR_DC_BATCH_DETAIL (DC_BATCH_DTL_SKEY) ON DELETE CASCADE;
      
ALTER TABLE NZR_DC_DETAIL
   ADD CONSTRAINT NZR_DC_DETAIL_FK1 FOREIGN KEY (DC_SKEY)
      REFERENCES NZR_DC_SET (DC_SKEY) ON DELETE CASCADE ;

ALTER TABLE NZR_DC_DETAIL_HIST
   ADD CONSTRAINT NZR_DC_DETAIL_HIST_FK1 FOREIGN KEY (DC_BATCH_SKEY)
      REFERENCES NZR_DC_BATCH (DC_BATCH_SKEY) ON DELETE CASCADE;


ALTER TABLE NZR_DC_DETAIL_HIST
   ADD CONSTRAINT NZR_DC_DETAIL_HIST_FK2 FOREIGN KEY (DC_BATCH_DTL_SKEY)
      REFERENCES NZR_DC_BATCH_DETAIL (DC_BATCH_DTL_SKEY) ON DELETE CASCADE;

ALTER TABLE NZR_DC_DETAIL_ATTR_HIST
   ADD CONSTRAINT NZR_DC_DETAIL_ATTRIBUTE_FK1 FOREIGN KEY (TABLE_SKEY)
      REFERENCES NZR_DC_DETAIL (TABLE_SKEY) ON DELETE CASCADE;
      
ALTER TABLE NZR_DC_DETAIL_ATTR_HIST
   ADD CONSTRAINT NZR_DC_DETAIL_ATTRIBUTE_FK2 FOREIGN KEY (DC_BATCH_SKEY)
      REFERENCES NZR_DC_BATCH (DC_BATCH_SKEY) ON DELETE CASCADE;

ALTER TABLE NZR_DC_DETAIL_ATTR_HIST
   ADD CONSTRAINT NZR_DC_DETAIL_ATTRIBUTE_FK3 FOREIGN KEY (DC_BATCH_DTL_SKEY)
      REFERENCES NZR_DC_BATCH_DETAIL (DC_BATCH_DTL_SKEY) ON DELETE CASCADE;
      
ALTER TABLE NZR_DC_SET
   ADD CONSTRAINT NZR_DC_SET_FK1 FOREIGN KEY (SRC_HOST_SKEY)
      REFERENCES NZR_HOST (HOST_SKEY);


ALTER TABLE NZR_STG_FILE
   ADD CONSTRAINT NZR_STG_FILE_FK1 FOREIGN KEY (DC_BATCH_DTL_SKEY)
      REFERENCES NZR_DC_BATCH_DETAIL (DC_BATCH_DTL_SKEY) ON DELETE CASCADE;

ALTER TABLE NZR_STG_FILE
   ADD CONSTRAINT NZR_STG_FILE_FK2 FOREIGN KEY (FOLDER_SKEY)
      REFERENCES NZR_STG_FOLDER (FOLDER_SKEY) ON DELETE CASCADE;

ALTER TABLE NZR_STG_FILE
   ADD CONSTRAINT NZR_STG_FILE_FK3 FOREIGN KEY (DC_BATCH_SKEY)
      REFERENCES NZR_DC_BATCH (DC_BATCH_SKEY) ON DELETE CASCADE;

ALTER TABLE NZR_STG_FOLDER
   ADD CONSTRAINT NZR_STG_FOLDER_FK1 FOREIGN KEY (DC_BATCH_SKEY)
      REFERENCES NZR_DC_BATCH (DC_BATCH_SKEY) ON DELETE CASCADE;

ALTER TABLE NZR_WRK_CONSTRAINTS
   ADD CONSTRAINT NZR_WRK_CONSTRAINTS_FK1 FOREIGN KEY (DC_BATCH_SKEY, TABLE_SKEY, DC_SKEY)
      REFERENCES NZR_WRK_TABLE (DC_BATCH_SKEY, TABLE_SKEY, DC_SKEY) ON DELETE CASCADE;

ALTER TABLE NZR_WRK_CONSTRAINT_ATTR
   ADD CONSTRAINT NZR_WRK_CONSTRAINT_ATTR_FK1 FOREIGN KEY (DC_BATCH_SKEY, TABLE_SKEY, DC_SKEY, CONSTRAINT_NAME)
      REFERENCES NZR_WRK_CONSTRAINTS (DC_BATCH_SKEY, TABLE_SKEY, DC_SKEY, CONSTRAINT_NAME) ON DELETE CASCADE;

ALTER TABLE NZR_WRK_TABLE
   ADD CONSTRAINT NZR_WRK_TABLE_FK1 FOREIGN KEY (DC_SKEY)
      REFERENCES NZR_DC_SET (DC_SKEY) ON DELETE CASCADE;

ALTER TABLE NZR_WRK_TABLE
   ADD CONSTRAINT NZR_WRK_TABLE_FK2 FOREIGN KEY (DC_BATCH_SKEY)
      REFERENCES NZR_DC_BATCH (DC_BATCH_SKEY) ON DELETE CASCADE;

ALTER TABLE NZR_WRK_TABLE
   ADD CONSTRAINT NZR_WRK_TABLE_FK3 FOREIGN KEY (TABLE_SKEY)
      REFERENCES NZR_DC_DETAIL (TABLE_SKEY) ON DELETE CASCADE;

ALTER TABLE NZR_WRK_TABLE_ATTR
   ADD CONSTRAINT NZR_WRK_TABLE_ATTR_FK1 FOREIGN KEY (DC_BATCH_SKEY, TABLE_SKEY, DC_SKEY)
      REFERENCES NZR_WRK_TABLE (DC_BATCH_SKEY, TABLE_SKEY, DC_SKEY) ON DELETE CASCADE;

