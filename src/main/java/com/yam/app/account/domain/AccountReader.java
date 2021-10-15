package com.yam.app.account.domain;

import java.util.Optional;
import org.apache.ibatis.annotations.Param;

public interface AccountReader {

    boolean existsByEmail(String email);

    Optional<Account> findByEmail(String email);

    MemberAccount findByEmailAndMemberId(@Param("email") String email,
        @Param("memberId") Long memberId);
}
