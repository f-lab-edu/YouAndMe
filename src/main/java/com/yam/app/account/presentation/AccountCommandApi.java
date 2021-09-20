package com.yam.app.account.presentation;

import com.yam.app.account.application.AccountFacade;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
)
public final class AccountCommandApi {

    private final AccountFacade accountFacade;

    public AccountCommandApi(AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }

    @PostMapping("/api/accounts")
    public ResponseEntity<Void> register(
        @RequestBody @Valid RegisterAccountCommand command) {
        accountFacade.register(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 회원가입 이메일 검증 컨트롤러
     * - 임시로 "http://localhost:3000/login"로 리다이렉트 되도록 설정.
     * - 임시로 검증에 실패해서 예외가 발생하면 400 HTTP 을 반환하도록 설정.
     */
    @GetMapping("/api/accounts/authorize")
    public ResponseEntity<Void> registerConfirm(
        @ModelAttribute @Valid ConfirmRegisterAccountCommand command) throws Exception {
        accountFacade.registerConfirm(command);

        var uri = new URI("http://localhost:3000/login");
        var header = new HttpHeaders();
        header.setLocation(uri);
        return new ResponseEntity<>(header, HttpStatus.SEE_OTHER);
    }

    @PostMapping("/api/accounts/login")
    public ResponseEntity<Void> login(
        @RequestBody @Valid LoginAccountCommand request) {
        accountFacade.login(request);
        return ResponseEntity.ok().build();
    }
}
