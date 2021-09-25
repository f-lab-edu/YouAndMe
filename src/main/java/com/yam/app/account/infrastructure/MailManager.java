package com.yam.app.account.infrastructure;

import com.yam.app.account.domain.RegisterAccountEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
final class MailManager {

    private final MailDispatcher mailDispatcher;
    private final TemplateEngine templateEngine;
    private final String host;

    public MailManager(@Value("${app.mail.host}") String host,
        MailDispatcher mailDispatcher, TemplateEngine templateEngine) {
        this.mailDispatcher = mailDispatcher;
        this.templateEngine = templateEngine;
        this.host = host;
    }

    @TransactionalEventListener
    public void handle(RegisterAccountEvent event) {
        var newAccount = event.getAccount();
        var context = new Context();
        context.setVariable("link",
            "/api/accounts/authorize?token=" + newAccount.getEmailCheckToken()
                + "&email=" + newAccount.getEmail());
        var username = newAccount.getEmail().split("@")[0];
        context.setVariable("username", username);
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message", "YouAndMe 서비스를 사용하려면 링크를 클릭하세요.");
        context.setVariable("host", host);
        String message = templateEngine.process("mail/check-token", context);

        var mailMessage = MailMessage.builder()
            .to(newAccount.getEmail())
            .subject("YouAndMe, 회원 가입 인증")
            .message(message)
            .build();

        mailDispatcher.send(mailMessage);
    }
}
