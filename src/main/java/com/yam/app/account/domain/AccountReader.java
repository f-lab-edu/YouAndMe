package com.yam.app.account.domain;

import java.util.Optional;

public interface AccountReader {

    boolean existsByEmail(String email);

    Optional<Account> findByEmail(String email);
}
