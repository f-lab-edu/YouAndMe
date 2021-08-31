package com.yam.app.account.application;

import com.yam.app.account.domain.RegisterAccountEvent;
import com.yam.app.account.domain.RegisterAccountProcessor;
import com.yam.app.account.presentation.RegisterAccountRequest;
import com.yam.app.account.presentation.RegisterAccountResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountFacade {

    private final RegisterAccountProcessor processor;
    private final AccountResponseTranslator translator;
    private final ApplicationEventPublisher publisher;

    public AccountFacade(RegisterAccountProcessor processor,
        AccountResponseTranslator translator,
        ApplicationEventPublisher publisher) {
        this.processor = processor;
        this.translator = translator;
        this.publisher = publisher;
    }

    @Transactional
    public RegisterAccountResponse register(RegisterAccountRequest request) {
        var command = request.toCommand();
        var entity = processor.process(command.getEmail(), command.getNickname(),
            command.getPassword());
        publisher.publishEvent(new RegisterAccountEvent(entity));
        return translator.translate(entity);
    }
}
