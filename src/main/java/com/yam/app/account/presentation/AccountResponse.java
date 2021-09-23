package com.yam.app.account.presentation;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public final class AccountResponse {

    private final Long id;
    private final String email;
    private final boolean emailVerified;
    private final LocalDateTime joinedAt;
    private final LocalDateTime lastModifiedAt;
    private final LocalDateTime withdrawalAt;
    private final boolean withdraw;
    private final String role;

    public AccountResponse(Long id, String email, boolean emailVerified,
        LocalDateTime joinedAt, LocalDateTime lastModifiedAt, LocalDateTime withdrawalAt,
        boolean withdraw, String role) {
        this.id = id;
        this.email = email;
        this.emailVerified = emailVerified;
        this.joinedAt = joinedAt;
        this.lastModifiedAt = lastModifiedAt;
        this.withdrawalAt = withdrawalAt;
        this.withdraw = withdraw;
        this.role = role;
    }
}
