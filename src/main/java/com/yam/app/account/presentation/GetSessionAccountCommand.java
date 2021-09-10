package com.yam.app.account.presentation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public final class GetSessionAccountCommand {

    @NotBlank
    @Email
    private String email;

}
