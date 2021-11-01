create table member
(
    id       bigint not null auto_increment,
    nickname varchar(255) not null,
    image    varchar(255) not null,
    status   varchar(8) not null,
    primary key (id)
) engine=InnoDB;

create table account
(
    id                             bigint not null auto_increment,
    email                          varchar(255) not null,
    email_check_token              varchar(255),
    email_check_token_generated_at datetime(6),
    email_verified                 boolean      not null,
    joined_at                      datetime(6),
    last_modified_at               datetime(6),
    role                           varchar(10) not null,
    password                       varchar(255) not null,
    withdraw                       boolean      not null,
    withdrawal_at                  datetime(6),
    member_id                      bigint,
    status                         varchar(8) not null,
    primary key (id)
) engine=InnoDB;

create table tag
(
    id   bigint not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) engine=InnoDB;

create table article
(
    id          bigint not null auto_increment,
    content     varchar(255) not null,
    created_at  datetime(6),
    image       varchar(255) not null,
    modified_at datetime(6),
    status      varchar(8) not null,
    title       varchar(255) not null,
    member_id   bigint,
    primary key (id)
) engine=InnoDB;

create table article_likes
(
    id         bigint not null auto_increment,
    article_id bigint,
    member_id  bigint,
    primary key (id)
) engine=InnoDB;

create table article_tag
(
    id         bigint not null auto_increment,
    article_id bigint not null,
    tag_id     bigint not null,
    primary key (id)
) engine=InnoDB;

create table comment
(
    id          bigint not null auto_increment,
    content     varchar(255) not null,
    created_at  datetime(6),
    modified_at datetime(6),
    status      varchar(8),
    article_id  bigint,
    member_id   bigint,
    primary key (id)
) engine=InnoDB;

create table comment_likes
(
    id         bigint not null auto_increment,
    comment_id bigint,
    member_id  bigint,
    primary key (id)
) engine=InnoDB;

create index idx_account_member on account (member_id);
create index idx_article_title on article (title);
create index idx_article_author on article (member_id);
create index idx_comment_author on comment (member_id);
create index idx_comment_article on comment (article_id);

create index idx_articleTag_article on article_tag (article_id);
create index idx_articleTag_tag on article_tag (tag_id);
create index idx_commentLikes_comment on comment_likes (comment_id);
create index idx_commentLikes_member on comment_likes (member_id);
create index idx_articleLikes_comment on article_likes (article_id);
create index idx_articleLikes_member on article_likes (member_id);

alter table account
    add constraint uk_account_email unique (email);
alter table tag
    add constraint uk_tag_name unique (name);
alter table member
    add constraint uk_member_nickname unique (nickname);
