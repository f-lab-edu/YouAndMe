insert into member(nickname, image, status)
values ('rebwon', 'temp.png', 'ALIVE'),
       ('loginCheck', 'temp.png', 'ALIVE'),
       ('comment', 'temp.png', 'ALIVE');

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
        false, 'DEFAULT', 2, 'ALIVE'),
       ('comment@gmail.com', 'emailchecktoken1', now(), true, now(), now(),
        '$2a$10$EqbMbYB0vcZnuA5CClqa9uiLDnjA6pCjxn208ZchzA2q3ofqnkhcq',
        false, 'DEFAULT', 3, 'ALIVE');

insert into article(title, content, image, status, created_at, modified_at, member_id)
values ('sample-title', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 1),
       ('sample-title1', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 2),
       ('sample-title2', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 3),
       ('sample-title3', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 3),
       ('sample-title4', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 2),
       ('sample-title5', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 1),
       ('sample-title6', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 2),
       ('sample-title7', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 3),
       ('sample-title8', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 2),
       ('sample-title9', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 1),
       ('sample-title10', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 2),
       ('sample-title11', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 3),
       ('sample-title12', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 2),
       ('sample-title13', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 1),
       ('sample-title14', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 2),
       ('sample-title15', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 3),
       ('sample-title16', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 2),
       ('sample-title17', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 1),
       ('sample-title18', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 2),
       ('sample-title19', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 3),
       ('sample-title20', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 2),
       ('sample-title21', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 1),
       ('sample-title22', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 2),
       ('sample-title23', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 3),
       ('sample-title24', 'sample-content', 'sample.png', 'ALIVE', now(), now(), 1);

insert into tag(name)
values ('hibernate'),
       ('spring'),
       ('jpa');

insert into article_tag(article_id, tag_id)
values (1, 1), (1, 2), (1, 3), (2, 2), (2, 3), (3, 1), (3, 2), (4, 1), (4, 2), (5, 1), (5, 2), (6, 2), (6, 3), (7, 1), (7, 2), (7, 3),
       (8, 1), (8, 2), (9, 1), (9, 2), (9, 3), (10, 1), (10, 2), (11, 1), (11, 2), (11, 3), (12, 1), (12, 3), (13, 1), (13, 2), (14, 1),
       (15, 2), (16, 1), (17, 1), (18, 2), (19, 1), (19, 2), (19, 3), (20, 1), (20, 2);

INSERT INTO comment(content, created_at, modified_at, status, article_id, member_id)
VALUES ('sample content1', now(), now(), 'ALIVE', 1, 3);
