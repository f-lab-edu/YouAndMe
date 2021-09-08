package com.yam.app.account.presentation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public final class RegisterAccountRequestCommand {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String nickname;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z1-9~!@#$%^&*()+|=]{8,12}$",
        message = "비밀번호는 영어와 숫자, 특수문자로 8~12자리 이내로 입력해주세요.")
    private String password;

}
