package com.yam.app.account.application;

import com.yam.app.account.domain.AccountNotFoundException;
import com.yam.app.account.domain.AccountReader;
import com.yam.app.account.domain.ConfirmRegisterAccountProcessor;
import com.yam.app.account.domain.LoginAccountProcessor;
import com.yam.app.account.domain.RegisterAccountConfirmEvent;
import com.yam.app.account.domain.RegisterAccountEvent;
import com.yam.app.account.domain.RegisterAccountProcessor;
import com.yam.app.account.domain.UpdateAccountEvent;
import com.yam.app.account.domain.UpdateAccountProcessor;
import com.yam.app.account.presentation.AccountResponse;
import com.yam.app.account.presentation.ConfirmRegisterAccountCommand;
import com.yam.app.account.presentation.LoginAccountCommand;
import com.yam.app.account.presentation.RegisterAccountCommand;
import com.yam.app.account.presentation.UpdateAccountCommand;
import com.yam.app.common.Authentication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountFacade {

    private final RegisterAccountProcessor registerProcessor;
    private final ApplicationEventPublisher publisher;
    private final ConfirmRegisterAccountProcessor confirmRegisterProcessor;
    private final LoginAccountProcessor loginProcessor;
    private final AccountReader accountReader;
    private final UpdateAccountProcessor updateProcessor;

    public AccountFacade(RegisterAccountProcessor registerProcessor,
        ApplicationEventPublisher publisher,
        ConfirmRegisterAccountProcessor confirmRegisterProcessor,
        LoginAccountProcessor loginProcessor, AccountReader accountReader,
        UpdateAccountProcessor updateProcessor) {
        this.registerProcessor = registerProcessor;
        this.publisher = publisher;
        this.confirmRegisterProcessor = confirmRegisterProcessor;
        this.loginProcessor = loginProcessor;
        this.accountReader = accountReader;
        this.updateProcessor = updateProcessor;
    }

    @Transactional
    public void register(RegisterAccountCommand command) {
        registerProcessor.register(command.getEmail(), command.getPassword());
        var entity = accountReader.findByEmail(command.getEmail())
            .orElseThrow(() -> new AccountNotFoundException(command.getEmail()));
        publisher.publishEvent(new RegisterAccountEvent(entity));
    }

    @Transactional
    public void registerConfirm(ConfirmRegisterAccountCommand command) {
        confirmRegisterProcessor.registerConfirm(command.getToken(), command.getEmail());
        publisher.publishEvent(new RegisterAccountConfirmEvent(command.getEmail()));
    }

    @Transactional(readOnly = true)
    public void login(LoginAccountCommand command) {
        loginProcessor.login(command.getEmail(), command.getPassword());
    }

    @Transactional(readOnly = true)
    public AccountResponse findInfo(Authentication authentication) {
        var memberAccount = accountReader.findByEmailAndMemberId(
            authentication.getCredentials(), authentication.getMemberId());
        return new AccountResponse(memberAccount.getId(), memberAccount.getEmail(),
            memberAccount.getNickname(), memberAccount.getImage());
    }

    @Transactional
    public void update(Authentication authentication, UpdateAccountCommand command) {
        updateProcessor.update(authentication.getCredentials(), command.getPassword());
        publisher.publishEvent(new UpdateAccountEvent(
            authentication.getMemberId(),
            command.getNickname(),
            command.getImage()));
    }
}
