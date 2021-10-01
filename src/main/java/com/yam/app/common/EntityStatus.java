package com.yam.app.common;

import java.util.Arrays;

public enum EntityStatus {
    ALIVE, DELETED;

    public static EntityStatus findStatus(String status) {
        return Arrays.stream(EntityStatus.values())
            .filter(s -> s.name().equals(status))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
