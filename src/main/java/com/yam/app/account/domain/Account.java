package com.yam.app.account.domain;

import com.yam.app.common.EntityStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(exclude = "password")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Account implements Serializable {

    private Long id;
    private Long memberId;
    private String email;
    private String password;
    private String emailCheckToken;
    private LocalDateTime emailCheckTokenGeneratedAt;
    private boolean emailVerified = false;
    private LocalDateTime joinedAt;
    private LocalDateTime lastModifiedAt;
    private LocalDateTime withdrawalAt;
    private boolean withdraw = false;
    private Role role;
    private EntityStatus status = EntityStatus.ALIVE;

    private Account(String email, String password) {
        this.email = email;
        this.password = password;
        this.role = Role.DEFAULT;
    }

    public static Account of(String email, String password) {
        Account account = new Account(email, password);
        account.generateEmailCheckToken();
        return account;
    }

    void setId(Long id) {
        this.id = id;
    }

    private void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public void completeRegister() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    public void addMember(Long memberId) {
        this.memberId = memberId;
    }

    public void changePassword(String password) {
        this.password = password;
    }
}
