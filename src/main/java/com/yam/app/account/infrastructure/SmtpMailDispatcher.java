package com.yam.app.account.infrastructure;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Slf4j
public final class SmtpMailDispatcher implements MailDispatcher {

    private final JavaMailSender javaMailSender;

    public SmtpMailDispatcher(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void send(MailMessage mailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setTo(mailMessage.getTo());
            helper.setSubject(mailMessage.getSubject());
            helper.setText(mailMessage.getMessage(), true);

            javaMailSender.send(mimeMessage);

            log.info("sent email: {}", mailMessage.getMessage());
        } catch (MessagingException e) {
            log.error("failed to send email", e);
            throw new RuntimeException(e);
        }
    }
}
