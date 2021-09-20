package com.yam.app.account.application;

import com.yam.app.account.domain.AccountNotFoundException;
import com.yam.app.account.domain.AccountReader;
import com.yam.app.account.domain.ConfirmRegisterAccountProcessor;
import com.yam.app.account.domain.LoginAccountProcessor;
import com.yam.app.account.domain.RegisterAccountEvent;
import com.yam.app.account.domain.RegisterAccountProcessor;
import com.yam.app.account.presentation.AccountResponse;
import com.yam.app.account.presentation.ConfirmRegisterAccountCommand;
import com.yam.app.account.presentation.LoginAccountCommand;
import com.yam.app.account.presentation.RegisterAccountCommand;
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
    public void register(RegisterAccountCommand command) {
        var entity = registerProcessor.process(
            command.getEmail(),
            command.getNickname(),
            command.getPassword()
        );
        publisher.publishEvent(new RegisterAccountEvent(entity));
    }

    @Transactional
    public void registerConfirm(ConfirmRegisterAccountCommand command) {
        confirmRegisterProcessor.registerConfirm(command.getToken(), command.getEmail());
    }

    @Transactional(readOnly = true)
    public void login(LoginAccountCommand command) {
        loginProcessor.login(command.getEmail(), command.getPassword());
    }

    @Transactional(readOnly = true)
    public AccountResponse findInfo(String email) {
        return translator.toResponse(accountReader.findByEmail(email)
            .orElseThrow(() -> new AccountNotFoundException(email)));
    }

}
