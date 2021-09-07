package com.yam.app.account.presentation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public final class LoginAccountRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
