package com.yam.app.account.presentation;

import com.yam.app.account.application.AccountFacade;
import com.yam.app.account.infrastructure.AccountApiUri;
import com.yam.app.account.infrastructure.AccountPrincipal;
import com.yam.app.account.infrastructure.LoginAccount;
import com.yam.app.common.ApiResult;
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

    @GetMapping(AccountApiUri.FIND_INFO)
    public ResponseEntity<ApiResult<?>> findInfo(
        @LoginAccount AccountPrincipal accountPrincipal) {
        return ResponseEntity
            .ok(ApiResult.success(accountFacade.findInfo(accountPrincipal.getEmail())));
    }

}
