package com.yam.app.account.presentation;

import com.yam.app.account.application.AccountFacade;
import com.yam.app.account.infrastructure.AccountPrincipal;
import com.yam.app.account.infrastructure.LoginAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
)
public final class AccountQueryApi {

    private final AccountFacade accountFacade;

    public AccountQueryApi(AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }

    @GetMapping("/api/accounts/me")
    public ResponseEntity<AccountResponse> findInfo(
        @LoginAccount AccountPrincipal accountPrincipal) {
        if (accountPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            return ResponseEntity.ok(accountFacade.findInfo(
                accountPrincipal.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
