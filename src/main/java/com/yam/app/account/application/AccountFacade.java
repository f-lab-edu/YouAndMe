package com.yam.app.account.application;

import com.yam.app.account.domain.RegisterAccountEvent;
import com.yam.app.account.domain.RegisterAccountProcessor;
import com.yam.app.account.presentation.AccountResponse;
import com.yam.app.account.presentation.RegisterAccountRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountFacade {

    private final RegisterAccountProcessor processor;
    private final AccountTranslator translator;
    private final ApplicationEventPublisher publisher;

    public AccountFacade(RegisterAccountProcessor processor,
        AccountTranslator translator,
        ApplicationEventPublisher publisher) {
        this.processor = processor;
        this.translator = translator;
        this.publisher = publisher;
    }

    @Transactional
    public AccountResponse register(RegisterAccountRequest request) {
        var entity = processor.process(translator.toCommand(request));
        publisher.publishEvent(new RegisterAccountEvent(entity));
        return translator.toResponse(entity);
    }

    public boolean verify(String token, String email) {
        return false;
    }
}
