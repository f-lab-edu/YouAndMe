package com.yam.app.account.infrastructure;

import com.yam.app.account.domain.AccountNotFoundException;
import com.yam.app.account.domain.AccountReader;
import com.yam.app.account.domain.AccountRepository;
import com.yam.app.account.domain.GenerateMemberEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AccountEventListener {

    private final AccountReader accountReader;
    private final AccountRepository accountRepository;

    public AccountEventListener(AccountReader accountReader,
        AccountRepository accountRepository) {
        this.accountReader = accountReader;
        this.accountRepository = accountRepository;
    }

    @EventListener
    public void handle(GenerateMemberEvent event) {
        var account = accountReader.findByEmail(event.getEmail())
            .orElseThrow(() -> new AccountNotFoundException(event.getEmail()));
        account.addMember(event.getMemberId());
        accountRepository.update(account);
    }
}
