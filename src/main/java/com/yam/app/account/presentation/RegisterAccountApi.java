package com.yam.app.account.presentation;

import com.yam.app.account.application.AccountFacade;
import javax.validation.Valid;
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
public final class RegisterAccountApi {

    private final AccountFacade accountFacade;

    public RegisterAccountApi(AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }

    @PostMapping("/api/accounts")
    public ResponseEntity<AccountResponse> register(
        @RequestBody @Valid RegisterAccountRequest request) {
        return ResponseEntity.ok(accountFacade.register(request));
    }
}
