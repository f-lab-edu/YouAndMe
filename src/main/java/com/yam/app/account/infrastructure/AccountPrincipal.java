package com.yam.app.account.infrastructure;

import java.io.Serializable;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class AccountPrincipal implements Serializable {

    private final String email;

    public AccountPrincipal(String email) {
        this.email = email;
    }
}
