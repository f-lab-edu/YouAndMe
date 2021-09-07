package com.yam.app.account.domain;

import java.util.Optional;

public interface AccountReader {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Account> findByEmail(String email);
}
