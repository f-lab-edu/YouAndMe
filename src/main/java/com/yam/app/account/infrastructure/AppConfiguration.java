package com.yam.app.account.infrastructure;

import com.yam.app.account.domain.AccountReader;
import com.yam.app.account.domain.AccountRepository;
import com.yam.app.account.domain.RegisterAccountProcessor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public AccountRepository accountRepository(SqlSessionTemplate sqlSessionTemplate) {
        return new MybatisAccountRepository(sqlSessionTemplate);
    }

    @Bean
    public AccountReader accountReader(SqlSessionTemplate sqlSessionTemplate) {
        return new MybatisAccountRepository(sqlSessionTemplate);
    }

    @Bean
    public RegisterAccountProcessor registerAccountProcessor(AccountRepository accountRepository) {
        return new RegisterAccountProcessor(accountRepository);
    }
}
