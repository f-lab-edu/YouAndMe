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

    private final RegisterAccountProcessor processor;
    private final AccountTranslator translator;
    private final ApplicationEventPublisher publisher;
    private final ConfirmRegisterAccountProcessor confirmRegisterProcessor;
    private final LoginAccountProcessor loginAccountProcessor;

    public AccountFacade(RegisterAccountProcessor processor,
        AccountTranslator translator, ApplicationEventPublisher publisher,
        ConfirmRegisterAccountProcessor confirmRegisterProcessor,
        LoginAccountProcessor loginAccountProcessor) {
        this.processor = processor;
        this.translator = translator;
        this.publisher = publisher;
        this.confirmRegisterProcessor = confirmRegisterProcessor;
        this.loginAccountProcessor = loginAccountProcessor;
    }

    @Transactional
    public AccountResponse register(RegisterAccountRequest request) {
        var entity = processor.process(translator.toCommand(request));
        publisher.publishEvent(new RegisterAccountEvent(entity));
        return translator.toResponse(entity);
    }

    public void registerConfirm(ConfirmRegisterAccountRequest request) {
        confirmRegisterProcessor.registerConfirm(translator.toCommand(request));
    }

    public void login(LoginAccountRequest request) {
        loginAccountProcessor.login(translator.toCommand(request));
    }
}
