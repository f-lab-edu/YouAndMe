package com.yam.app.account.presentation;

import com.yam.app.account.application.AccountFacade;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
        @LoginAccount AccountResponse response) {
        return ResponseEntity.ok(response);
    }

    // LoginAccountMethodArgumentResolver 테스트를 위해 임시로 만든 @ExceptionHandler
    @ExceptionHandler
    public ResponseEntity<Void> defaultException(IllegalStateException e) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
