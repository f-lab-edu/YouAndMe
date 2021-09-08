package com.yam.app.account.presentation;

import com.yam.app.account.application.AccountFacade;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/api/accounts/login")
    public ResponseEntity<Void> login(
        @Valid @RequestBody LoginAccountRequest request) {
        try {
            accountFacade.login(request);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok().build();
    }
}
