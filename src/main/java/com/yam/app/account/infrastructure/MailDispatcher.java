package com.yam.app.account.infrastructure;

public interface MailDispatcher {

    void send(MailMessage mailMessage);
}
