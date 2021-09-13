package com.yam.app.account.application;

import com.yam.app.account.domain.AccountReader;
import com.yam.app.account.domain.ConfirmRegisterAccountProcessor;
import com.yam.app.account.domain.LoginAccountProcessor;
import com.yam.app.account.domain.RegisterAccountEvent;
import com.yam.app.account.domain.RegisterAccountProcessor;
import com.yam.app.account.presentation.AccountResponse;
import com.yam.app.account.presentation.ConfirmRegisterAccountRequestCommand;
import com.yam.app.account.presentation.LoginAccountRequestCommand;
import com.yam.app.account.presentation.RegisterAccountRequestCommand;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountFacade {

    private final RegisterAccountProcessor registerProcessor;
    private final AccountTranslator translator;
    private final ApplicationEventPublisher publisher;
    private final ConfirmRegisterAccountProcessor confirmRegisterProcessor;
    private final LoginAccountProcessor loginProcessor;
    private final AccountReader accountReader;

    public AccountFacade(RegisterAccountProcessor registerProcessor,
        AccountTranslator translator, ApplicationEventPublisher publisher,
        ConfirmRegisterAccountProcessor confirmRegisterProcessor,
        LoginAccountProcessor loginProcessor, AccountReader accountReader) {
        this.registerProcessor = registerProcessor;
        this.translator = translator;
        this.publisher = publisher;
        this.confirmRegisterProcessor = confirmRegisterProcessor;
        this.loginProcessor = loginProcessor;
        this.accountReader = accountReader;
    }

    @Transactional
    public AccountResponse register(RegisterAccountRequestCommand request) {
        var entity = registerProcessor.process(
            request.getEmail(),
            request.getNickname(),
            request.getPassword()
        );
        publisher.publishEvent(new RegisterAccountEvent(entity));
        return translator.toResponse(entity);
    }

    @Transactional
    public void registerConfirm(ConfirmRegisterAccountRequestCommand request) {
        confirmRegisterProcessor.registerConfirm(request.getToken(), request.getEmail());
    }

    @Transactional(readOnly = true)
    public void login(LoginAccountRequestCommand request) {
        loginProcessor.login(request.getEmail(), request.getPassword());
    }

    @Transactional(readOnly = true)
    public AccountResponse getLoginAccount(String email) {
        return translator.toResponse(accountReader.findByEmail(email)
            .orElseThrow(IllegalStateException::new));
    }

}
