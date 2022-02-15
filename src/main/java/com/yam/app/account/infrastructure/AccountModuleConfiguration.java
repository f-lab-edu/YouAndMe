package com.yam.app.account.infrastructure;

import com.yam.app.account.domain.ConfirmRegisterAccountProcessor;
import com.yam.app.account.domain.RegisterAccountProcessor;
import com.yam.app.account.domain.TokenVerifier;
import com.yam.app.account.domain.UpdateAccountProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Import(value = {
    MybatisAccountRepository.class,
    DelegatePasswordEncrypter.class,
    RegisterAccountProcessor.class,
    TokenVerifier.class,
    ConfirmRegisterAccountProcessor.class,
    SessionBasedLoginAccountProcessor.class,
    UpdateAccountProcessor.class
})
@Configuration
public class AccountModuleConfiguration {

    @Bean
    @Profile(value = {"local", "prod"})
    public MailDispatcher mailDispatcher(JavaMailSender javaMailSender) {
        return new SmtpMailDispatcher(javaMailSender);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
