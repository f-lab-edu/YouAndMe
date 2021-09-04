package com.yam.app.account.domain;

public interface AccountRepository {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Account save(Account entity);

    Account update(Account entity);

}
