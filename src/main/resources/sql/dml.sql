insert into member(nickname, image, status)
values ('rebwon', 'temp.png', 'ALIVE'),
       ('loginCheck', 'temp.png', 'ALIVE');

insert into account(email, email_check_token, email_check_token_generated_at, email_verified,
                    joined_at, last_modified_at, password, withdraw, role, status)
values ('jiwonDev@gmail.com', 'emailchecktoken', now(), false, now(), now(), 'password!',
        false, 'DEFAULT', 'ALIVE');

insert into account(email, email_check_token, email_check_token_generated_at, email_verified,
                    joined_at, last_modified_at, password, withdraw, role, member_id, status)
values ('rebwon@gmail.com', 'emailchecktoken1', now(), true, now(), now(),
        '$2a$10$g1V1fmucOwRZX.bWCb9k5uFc/EylGvoYw6N8m90RxIcvyYVhFBl1C',
        false, 'DEFAULT', 1, 'ALIVE'),
       ('loginCheck@gmail.com', 'emailchecktoken1', now(), true, now(), now(),
        '$2a$10$EqbMbYB0vcZnuA5CClqa9uiLDnjA6pCjxn208ZchzA2q3ofqnkhcq',
        false, 'DEFAULT', 2, 'ALIVE');

insert into article(title, content, image, status, created_at, modified_at, member_id)
values ('sample-title', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 1);

insert into tag(name)
values ('hibernate'),
       ('spring'),
       ('jpa');

insert into article_tag(article_id, tag_id)
values (1, 1),
       (1, 2),
       (1, 3);

insert into member(nickname, image, status)
values ('comment', 'temp.png', 'ALIVE');

insert into account(email, email_check_token, email_check_token_generated_at, email_verified,
                    joined_at, last_modified_at, password, withdraw, role, member_id, status)
values ('comment@gmail.com', 'emailchecktoken1', now(), true, now(), now(),
        '$2a$10$EqbMbYB0vcZnuA5CClqa9uiLDnjA6pCjxn208ZchzA2q3ofqnkhcq',
        false, 'DEFAULT', 3, 'ALIVE');

INSERT INTO comment(content, created_at, modified_at, status, article_id, member_id)
VALUES ('sample content1', now(), now(), 'ALIVE', 1, 3);
