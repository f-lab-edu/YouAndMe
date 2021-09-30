insert into account(email, email_check_token, email_check_token_generated_at, email_verified,
                    joined_at, last_modified_at, password, withdraw, role)
values ('jiwonDev@gmail.com', 'emailchecktoken', now(), false, now(), now(), 'password!',
        false, 'DEFAULT'),
       ('loginCheck@gmail.com', 'emailchecktoken1', now(), true, now(), now(),
        '$2a$10$EqbMbYB0vcZnuA5CClqa9uiLDnjA6pCjxn208ZchzA2q3ofqnkhcq',
        false, 'DEFAULT');

insert into member(nickname, image)
values ('rebwon', 'temp.png');

insert into account(email, email_check_token, email_check_token_generated_at, email_verified,
                    joined_at, last_modified_at, password, withdraw, role, member_id)
values ('rebwon@gmail.com', 'emailchecktoken1', now(), true, now(), now(),
        '$2a$10$EqbMbYB0vcZnuA5CClqa9uiLDnjA6pCjxn208ZchzA2q3ofqnkhcq',
        false, 'DEFAULT', 1);
