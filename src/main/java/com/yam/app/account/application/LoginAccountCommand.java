package com.yam.app.account.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class LoginAccountCommand {

    private final String email;
    private final String password;

}
