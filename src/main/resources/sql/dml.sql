insert into account(email, email_check_token, email_check_token_generated_at, email_verified,
                    joined_at, last_modified_at, password, withdraw, role, status)
values ('jiwonDev@gmail.com', 'emailchecktoken', now(), false, now(), now(), 'password!',
        false, 'DEFAULT', 'ALIVE'),
       ('loginCheck@gmail.com', 'emailchecktoken1', now(), true, now(), now(),
        '$2a$10$EqbMbYB0vcZnuA5CClqa9uiLDnjA6pCjxn208ZchzA2q3ofqnkhcq',
        false, 'DEFAULT', 'ALIVE');

insert into member(nickname, image, status)
values ('rebwon', 'temp.png', 'ALIVE');

insert into account(email, email_check_token, email_check_token_generated_at, email_verified,
                    joined_at, last_modified_at, password, withdraw, role, member_id, status)
values ('rebwon@gmail.com', 'emailchecktoken1', now(), true, now(), now(),
        '$2a$10$EqbMbYB0vcZnuA5CClqa9uiLDnjA6pCjxn208ZchzA2q3ofqnkhcq',
        false, 'DEFAULT', 1, 'ALIVE');
