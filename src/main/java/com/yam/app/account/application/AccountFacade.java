package com.yam.app.account.application;

import com.yam.app.account.domain.RegisterAccountProcessor;
import com.yam.app.account.presentation.RegisterAccountRequest;
import com.yam.app.account.presentation.RegisterAccountResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountFacade {

    private final RegisterAccountProcessor processor;
    private final AccountResponseTranslator translator;

    public AccountFacade(RegisterAccountProcessor processor,
        AccountResponseTranslator translator) {
        this.processor = processor;
        this.translator = translator;
    }

    public RegisterAccountResponse register(RegisterAccountRequest request) {
        var command = request.toCommand();
        return translator.translate(
            processor.process(command.getEmail(), command.getNickname(), command.getPassword()));
    }
}
