package com.yam.app.account.presentation;

import com.yam.app.account.application.AccountFacade;
import com.yam.app.common.ApiResult;
import com.yam.app.common.Authentication;
import com.yam.app.common.AuthenticationPrincipal;
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
    public ResponseEntity<ApiResult<?>> findInfo(
        @AuthenticationPrincipal Authentication authentication) {
        return ResponseEntity.ok(
            ApiResult.success(accountFacade.findInfo(authentication)));
    }

}
