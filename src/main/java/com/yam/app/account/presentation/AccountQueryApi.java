package com.yam.app.account.presentation;

import com.yam.app.account.application.AccountFacade;
import com.yam.app.account.infrastructure.AccountPrincipal;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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

    @PostMapping("/api/accounts/login")
    public ResponseEntity<Void> login(
        @Valid @RequestBody LoginAccountRequestCommand request) {
        try {
            accountFacade.login(request);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/accounts/me")
    public ResponseEntity<AccountResponse> getAccount(
        @LoginAccount AccountPrincipal accountPrincipal) {
        if (accountPrincipal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        AccountResponse accountResponse;
        try {
            accountResponse = accountFacade.getLoginAccount(accountPrincipal.getEmail());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(accountResponse);
    }

}
