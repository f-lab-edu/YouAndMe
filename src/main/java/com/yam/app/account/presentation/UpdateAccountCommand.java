package com.yam.app.account.presentation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public final class UpdateAccountCommand {

    @NotBlank
    @Pattern(regexp = "^[A-Za-z1-9~!@#$%^&*()+|=]{8,12}$",
        message = "Please enter the password in English, numbers, "
            + "and special characters within 8-12 digits.")
    private String password;

    @NotBlank
    private String nickname;

    @NotBlank
    private String image;
}
