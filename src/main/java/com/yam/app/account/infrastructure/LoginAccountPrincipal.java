package com.yam.app.account.infrastructure;

import com.yam.app.account.domain.AccountPrincipal;
import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public final class LoginAccountPrincipal implements AccountPrincipal, Serializable {

    @NotBlank
    @Email
    private final String email;
}
