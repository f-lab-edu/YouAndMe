package com.yam.app.account.infrastructure;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public final class DummyMailDispatcher implements MailDispatcher {

    @Override
    public void send(MailMessage mailMessage) {

    }
}
