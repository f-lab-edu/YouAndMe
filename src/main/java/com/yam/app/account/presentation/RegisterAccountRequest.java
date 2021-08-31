package com.yam.app.account.presentation;

import com.yam.app.account.application.RegisterAccountCommand;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public final class RegisterAccountRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String nickname;

    @Size(min = 8, max = 12, message = "비밀번호는 최소 8자, 최대 12자까지 허용됩니다.")
    @NotBlank
    private String password;

    public RegisterAccountCommand toCommand() {
        return new RegisterAccountCommand(email, nickname, password);
    }
}
