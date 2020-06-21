--DROP TABLE "SUBJECT"."ATTACHMENT";
--DROP TABLE "SUBJECT"."COMMENT";
--DROP TABLE "SUBJECT"."PAGE";
--DROP TABLE "SUBJECT"."SPACEUSER";
--DROP TABLE "SUBJECT"."SPACE";
--DROP TABLE "SUBJECT"."USER";
--DROP SCHEMA SUBJECT RESTRICT;

CREATE SCHEMA SUBJECT;

CREATE TABLE "SUBJECT"."SPACE" (
	"SPACEID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
	"NAME" VARCHAR(1024) NOT NULL,
	"URL" VARCHAR(1024) NOT NULL,
	"CREATED" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	"UPDATED" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"DESCRIPTION" VARCHAR(1024),
	CONSTRAINT "SPACE_PK" PRIMARY KEY ("SPACEID")
);
CREATE UNIQUE INDEX "SPACE_SPACE_IDX" ON "SUBJECT"."SPACE" ("SPACEID");
CREATE UNIQUE INDEX "SPACE_URL_IDX" ON "SUBJECT"."SPACE" ("URL");

CREATE TABLE "SUBJECT"."USER" (
	"USERID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
	"NAME" VARCHAR(1024) NOT NULL,
	"EMAIL" VARCHAR(1024) NOT NULL,
	"SALT" VARCHAR(1024) NOT NULL,
	"HASH" VARCHAR(1024) NOT NULL,
	"CREATED" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	"UPDATED" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT "USER_PK" PRIMARY KEY ("USERID")
);
CREATE UNIQUE INDEX "USER_USER_IDX" ON "SUBJECT"."USER" ("USERID");
CREATE UNIQUE INDEX "USER_EMAIL_IDX" ON "SUBJECT"."USER" ("EMAIL");

CREATE TABLE "SUBJECT"."PAGE" (
	"PAGEID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
	"USERID" INTEGER NOT NULL,
	"SPACEID" INTEGER NOT NULL,
	"PARENTID" INTEGER,
	"NAME" VARCHAR(1024) NOT NULL,
	"URL" VARCHAR(1024) NOT NULL,
	"CONTENT" CLOB,
	"CREATED" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	"UPDATED" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT "PAGE_PK" PRIMARY KEY ("PAGEID"),
	CONSTRAINT "PAGE_SPACE_FK" FOREIGN KEY ("SPACEID") REFERENCES "SUBJECT"."SPACE"("SPACEID"),
	CONSTRAINT "PAGE_USER_FK" FOREIGN KEY ("USERID") REFERENCES "SUBJECT"."USER"("USERID")
);
CREATE UNIQUE INDEX "PAGE_PAGE_IDX" ON "SUBJECT"."PAGE" ("PAGEID");
CREATE UNIQUE INDEX "PAGE_URL_IDX" ON "SUBJECT"."PAGE" ("URL");
CREATE INDEX "PAGE_SPACE_IDX" ON "SUBJECT"."PAGE" ("SPACEID");
CREATE INDEX "PAGE_USER_IDX" ON "SUBJECT"."PAGE" ("USERID");

CREATE TABLE "SUBJECT"."SPACEUSER" (
	"SPACEUSERID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
	"SPACEID" INTEGER NOT NULL,
	"USERID" INTEGER NOT NULL,
	CONSTRAINT "SPACEUSER_PK" PRIMARY KEY ("SPACEUSERID"),
	CONSTRAINT "SPACEUSER_SPACE_FK" FOREIGN KEY ("SPACEID") REFERENCES "SUBJECT"."SPACE"("SPACEID"),
	CONSTRAINT "SPACEUSER_USER_FK" FOREIGN KEY ("USERID") REFERENCES "SUBJECT"."USER"("USERID")
);
CREATE UNIQUE INDEX "SPACEUSER_SPACEUSER_IDX" ON "SUBJECT"."SPACEUSER" ("SPACEUSERID");
CREATE INDEX "SPACEUSER_SPACE_IDX" ON "SUBJECT"."SPACEUSER" ("SPACEID");
CREATE INDEX "SPACEUSER_USER_IDX" ON "SUBJECT"."SPACEUSER" ("USERID");

CREATE TABLE "SUBJECT"."ATTACHMENT" (
	"ATTACHMENTID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
	"USERID" INTEGER NOT NULL,
	"PAGEID" INTEGER NOT NULL,
	"NAME" VARCHAR(1024) NOT NULL,
	"CONTENT" CLOB,
	"CREATED" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	"UPDATED" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT "ATTACHMENT_PK" PRIMARY KEY ("ATTACHMENTID"),
	CONSTRAINT "ATTACHMENT_PAGE_FK" FOREIGN KEY ("PAGEID") REFERENCES "SUBJECT"."PAGE"("PAGEID"),
	CONSTRAINT "ATTACHMENT_USER_FK" FOREIGN KEY ("USERID") REFERENCES "SUBJECT"."USER"("USERID")
);
CREATE UNIQUE INDEX "ATTACHMENT_ATTACHMENT_IDX" ON "SUBJECT"."ATTACHMENT" ("ATTACHMENTID");
CREATE INDEX "ATTACHMENT_PAGE_IDX" ON "SUBJECT"."ATTACHMENT" ("PAGEID");
CREATE INDEX "ATTACHMENT_USER_IDX" ON "SUBJECT"."ATTACHMENT" ("USERID");

CREATE TABLE "SUBJECT"."COMMENT" (
	"COMMENTID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
	"USERID" INTEGER NOT NULL,
	"PAGEID" INTEGER NOT NULL,
	"PARENTID" INTEGER,
	"CONTENT" CLOB,
	"CREATED" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	"UPDATED" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT "COMMENT_PK" PRIMARY KEY ("COMMENTID"),
	CONSTRAINT "COMMENT_PAGE_FK" FOREIGN KEY ("PAGEID") REFERENCES "SUBJECT"."PAGE"("PAGEID"),
	CONSTRAINT "COMMENT_USER_FK" FOREIGN KEY ("USERID") REFERENCES "SUBJECT"."USER"("USERID")
);
CREATE UNIQUE INDEX "COMMENT_COMMENT_IDX" ON "SUBJECT"."COMMENT" ("COMMENTID");
CREATE INDEX "COMMENT_PAGE_IDX" ON "SUBJECT"."COMMENT" ("PAGEID");
CREATE INDEX "COMMENT_USER_IDX" ON "SUBJECT"."COMMENT" ("USERID");