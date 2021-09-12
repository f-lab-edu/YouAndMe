package com.yam.app.account.infrastructure;

import com.yam.app.account.domain.AccountReader;
import com.yam.app.account.domain.AccountRepository;
import com.yam.app.account.domain.AccountService;
import com.yam.app.account.domain.ConfirmRegisterAccountProcessor;
import com.yam.app.account.domain.LoginAccountProcessor;
import com.yam.app.account.domain.PasswordEncrypter;
import com.yam.app.account.domain.RegisterAccountProcessor;
import com.yam.app.account.domain.TokenVerifier;
import javax.servlet.http.HttpSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfiguration {

    @Bean
    @Profile("prod")
    public MailDispatcher mailDispatcher(JavaMailSender javaMailSender) {
        return new SmtpMailDispatcher(javaMailSender);
    }

    @Bean
    public PasswordEncrypter passwordEncrypter(PasswordEncoder passwordEncoder) {
        return new DelegatePasswordEncrypter(passwordEncoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccountRepository accountRepository(SqlSessionTemplate sqlSessionTemplate) {
        return new MybatisAccountRepository(sqlSessionTemplate);
    }

    @Bean
    public AccountReader accountReader(SqlSessionTemplate sqlSessionTemplate) {
        return new MybatisAccountRepository(sqlSessionTemplate);
    }

    @Bean
    public RegisterAccountProcessor registerAccountProcessor(AccountRepository accountRepository,
        AccountReader accountReader,
        PasswordEncrypter passwordEncrypter) {
        return new RegisterAccountProcessor(accountRepository, accountReader, passwordEncrypter);
    }

    @Bean
    public TokenVerifier tokenVerifier(AccountReader accountReader) {
        return new TokenVerifier(accountReader);
    }

    @Bean
    public ConfirmRegisterAccountProcessor confirmRegisterAccountProcessor(
        AccountReader accountReader, AccountRepository accountRepository,
        TokenVerifier tokenVerifier) {
        return new ConfirmRegisterAccountProcessor(accountReader, accountRepository, tokenVerifier);
    }

    @Bean
    public LoginAccountProcessor loginAccountProcessor(AccountReader accountReader,
        PasswordEncrypter passwordEncrypter, HttpSession httpSession) {
        return new SessionBasedLoginAccountProcessor(accountReader, passwordEncrypter, httpSession);
    }

    @Bean
    public AccountService accountService(AccountReader accountReader) {
        return new AccountService(accountReader);
    }
}
