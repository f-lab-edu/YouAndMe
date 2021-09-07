package com.yam.app.account.domain;

public interface AccountRepository {

    Account save(Account entity);

    Account update(Account entity);

}
