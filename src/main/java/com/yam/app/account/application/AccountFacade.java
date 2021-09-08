package com.yam.app.account.application;

import com.yam.app.account.domain.ConfirmRegisterAccountProcessor;
import com.yam.app.account.domain.LoginAccountProcessor;
import com.yam.app.account.domain.RegisterAccountEvent;
import com.yam.app.account.domain.RegisterAccountProcessor;
import com.yam.app.account.presentation.AccountResponse;
import com.yam.app.account.presentation.ConfirmRegisterAccountRequest;
import com.yam.app.account.presentation.LoginAccountRequest;
import com.yam.app.account.presentation.RegisterAccountRequest;
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

    public AccountFacade(RegisterAccountProcessor registerProcessor,
        AccountTranslator translator, ApplicationEventPublisher publisher,
        ConfirmRegisterAccountProcessor confirmRegisterProcessor,
        LoginAccountProcessor loginProcessor) {
        this.registerProcessor = registerProcessor;
        this.translator = translator;
        this.publisher = publisher;
        this.confirmRegisterProcessor = confirmRegisterProcessor;
        this.loginProcessor = loginProcessor;
    }

    @Transactional
    public AccountResponse register(RegisterAccountRequest request) {
        var entity = registerProcessor.process(translator.toCommand(request));
        publisher.publishEvent(new RegisterAccountEvent(entity));
        return translator.toResponse(entity);
    }

    @Transactional
    public void registerConfirm(ConfirmRegisterAccountRequest request) {
        confirmRegisterProcessor.registerConfirm(translator.toCommand(request));
    }

    public void login(LoginAccountRequest request) {
        loginProcessor.login(translator.toCommand(request));
    }
}
