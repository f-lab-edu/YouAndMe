package com.yam.app.account.integration;

import com.yam.app.account.infrastructure.MailDispatcher;
import com.yam.app.account.infrastructure.MailMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public final class DummyMailDispatcher implements MailDispatcher {

    @Override
    public void send(MailMessage mailMessage) {

    }
}
