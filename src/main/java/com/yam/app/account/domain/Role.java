package com.yam.app.account.domain;

import java.util.Arrays;

public enum Role {
    DEFAULT, LOCALIZED, ADMIN;

    public static Role findRole(String role) {
        return Arrays.stream(Role.values())
            .filter(r -> r.name().equals(role))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
