package com.yam.app.account.domain;

public interface AccountRepository {

    void save(Account entity);

    void update(Account entity);

}
