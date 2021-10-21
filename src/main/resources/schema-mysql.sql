create table member
(
    id       bigint not null auto_increment,
    nickname varchar(255) not null,
    image    varchar(255) not null,
    status   varchar(255) not null,
    primary key (id)
) engine=InnoDB;

create table account
(
    id                             bigint not null auto_increment,
    email                          varchar(255) not null,
    email_check_token              varchar(255),
    email_check_token_generated_at timestamp,
    email_verified                 boolean      not null,
    joined_at                      timestamp,
    last_modified_at               timestamp,
    role                           varchar(255) not null,
    password                       varchar(255) not null,
    withdraw                       boolean      not null,
    withdrawal_at                  timestamp,
    member_id                      bigint,
    status                         varchar(255) not null,
    primary key (id),
    foreign key (member_id) references member (id)
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
    created_at  timestamp,
    image       varchar(255) not null,
    modified_at timestamp,
    status      varchar(255) not null,
    title       varchar(255) not null,
    member_id   bigint,
    primary key (id),
    foreign key (member_id) references member (id)
) engine=InnoDB;

create table article_likes
(
    id         bigint not null auto_increment,
    article_id bigint,
    member_id  bigint,
    primary key (id),
    foreign key (member_id) references member (id),
    foreign key (article_id) references article (id)
) engine=InnoDB;

create table article_tag
(
    id         bigint not null auto_increment,
    article_id bigint not null,
    tag_id     bigint not null,
    primary key (id),
    foreign key (article_id) references article (id),
    foreign key (tag_id) references tag (id)
) engine=InnoDB;

create table comment
(
    id          bigint not null auto_increment,
    content     varchar(255) not null,
    created_at  timestamp,
    modified_at timestamp,
    status      varchar(255),
    article_id  bigint,
    member_id   bigint,
    primary key (id),
    foreign key (member_id) references member (id),
    foreign key (article_id) references article (id)
) engine=InnoDB;

create table comment_likes
(
    id         bigint not null auto_increment,
    comment_id bigint,
    member_id  bigint,
    primary key (id),
    foreign key (member_id) references member (id),
    foreign key (comment_id) references comment (id)
) engine=InnoDB;

alter table account
    add constraint UK_q0uja26qgu1atulenwup9rxyr unique (email);
alter table tag
    add constraint UK_1wdpsed5kna2y38hnbgrnhi5b unique (name);
alter table article
    add constraint UK_571gx7oqo5xpmgocegaidlcu9 unique (title);
