package com.yam.app.account.infrastructure;

import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public final class AccountPrincipal implements Serializable {

    @NotBlank
    @Email
    private final String email;
}
