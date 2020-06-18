create schema "subject";

-- user
create table "subject"."user" (
	userid bigint not null generated always as identity (start with 1, increment by 1),
	name varchar(1024) not null,
	email varchar(1024) not NULL unique,
	salt varchar(1024) not null,
	hash varchar(1024) not null,
	created timestamp default current_timestamp not null,
	updated timestamp default current_timestamp,
	constraint user_pk primary key (userid)
);

-- space
create table "subject"."space" (
	spaceid bigint not null generated always as identity (start with 1, increment by 1),
	name varchar(1024) not null,
	description varchar(1024),
	created timestamp default current_timestamp not null,
	updated timestamp default current_timestamp,
	constraint space_pk primary key (spaceid)
);

-- spaceuser
create table "subject"."spaceuser" (
	spaceuserid bigint not null generated always as identity (start with 1, increment by 1),
	spaceid bigint not null,
	userid bigint not null,
	constraint spaceuser_pk primary key (spaceuserid),
	constraint spaceuser_space_fk foreign key (spaceid) references "subject"."space"(spaceid),
	constraint spaceuser_user_fk foreign key (userid) references "subject"."user"(userid)
);

-- page
create table "subject"."page" (
	pageid bigint not null generated always as identity (start with 1, increment by 1),
	userid bigint not null,
	spaceid bigint not null,
	parentid bigint,
	name varchar(1024) not null,
	content clob,
	created timestamp default current_timestamp not null,
	updated timestamp default current_timestamp,
	constraint page_pk primary key (pageid),
	constraint page_user_fk foreign key (userid) references "subject"."user"(userid),
	constraint page_space_fk foreign key (spaceid) references "subject"."space"(spaceid)
);

-- attachment
create table "subject"."attachment" (
	attachmentid bigint not null generated always as identity (start with 1, increment by 1),
	userid bigint not null,
	pageid bigint not null,
	name varchar(1024) not null,
	content clob,
	created timestamp default current_timestamp not null,
	updated timestamp default current_timestamp,
	constraint attachment_pk primary key (attachmentid),
	constraint attachment_user_fk foreign key (userid) references "subject"."user"(userid),
	constraint attachment_page_fk foreign key (pageid) references "subject"."page"(pageid)
);

-- comment
create table "subject"."comment" (
	commentid bigint not null generated always as identity (start with 1, increment by 1),
	userid bigint not null,
	pageid bigint not null,
	parentid bigint,
	content clob,
	created timestamp default current_timestamp not null,
	updated timestamp default current_timestamp,
	constraint comment_pk primary key (commentid),
	constraint comment_user_fk foreign key (userid) references "subject"."user"(userid),
	constraint comment_page_fk foreign key (pageid) references "subject"."page"(pageid)
);

CREATE INDEX "subject"."comment_user_idx" ON "subject"."comment" ("USERID" ASC);
CREATE UNIQUE INDEX "page_unq" ON "subject"."page" ("PAGEID" ASC);
CREATE INDEX "subject"."ATTACHMENT_USER_IDX" ON "subject"."attachment" ("USERID" ASC);
CREATE UNIQUE INDEX "subject"."SPACE_UNQ" ON "subject"."space" ("SPACEID" ASC);
CREATE INDEX "subject"."ATTACHMENT_USER_IDX" ON "subject"."attachment" ("PAGEID" ASC);
CREATE INDEX "subject"."COMMENT_PAGE_IDX" ON "subject"."comment" ("PAGEID" ASC);
CREATE UNIQUE INDEX "subject"."ATTACHMENT_UNQ" ON "subject"."attachment" ("ATTACHMENTID" ASC);
CREATE INDEX "subject"."SPACEUSER_SPACE_IDX" ON "subject"."spaceuser" ("SPACEID" ASC);
CREATE UNIQUE INDEX "subject"."COMMENT_UNQ" ON "subject"."comment" ("COMMENTID" ASC);
CREATE INDEX "subject"."PAGE_SPACE_IDX" ON "subject"."page" ("SPACEID" ASC);
CREATE INDEX "subject"."SPACEUSER_USER_IDX" ON "subject"."spaceuser" ("USERID" ASC);
CREATE UNIQUE INDEX "subject"."SPACEUSER_UNQ" ON "subject"."spaceuser" ("SPACEUSERID" ASC);
CREATE UNIQUE INDEX "subject"."USER_USER_UNQ" ON "subject"."user" ("USERID" ASC);
CREATE UNIQUE INDEX "subject"."USER_EMAIL_UNQ" ON "subject"."user" ("EMAIL" ASC);
CREATE INDEX "subject"."PAGE_USER_IDX" ON "subject"."page" ("USERID" ASC);
