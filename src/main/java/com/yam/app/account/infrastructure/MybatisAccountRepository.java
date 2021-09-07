package com.yam.app.account.infrastructure;

import com.yam.app.account.domain.Account;
import com.yam.app.account.domain.AccountReader;
import com.yam.app.account.domain.AccountRepository;
import java.util.Optional;
import org.mybatis.spring.SqlSessionTemplate;

public final class MybatisAccountRepository implements AccountRepository, AccountReader {

    private final SqlSessionTemplate template;

    private static final String SAVE_FQCN = "com.yam.app.account.domain.AccountRepository.save";
    private static final String UPDATE_FQCN = "com.yam.app.account.domain.AccountRepository.update";

    public MybatisAccountRepository(SqlSessionTemplate template) {
        this.template = template;
    }

    @Override
    public boolean existsByEmail(String email) {
        return template.getMapper(AccountReader.class).existsByEmail(email);
    }

    @Override
    public Account update(Account entity) {
        int result = template.update(UPDATE_FQCN, entity);
        if (result != 1) {
            throw new RuntimeException(
                String.format("There was a problem updating the object : %s", entity));
        }
        return findByEmail(entity.getEmail())
            .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return template.getMapper(AccountReader.class).existsByNickname(nickname);
    }

    @Override
    public Account save(Account entity) {
        int result = template.insert(SAVE_FQCN, entity);
        if (result != 1) {
            throw new RuntimeException(
                String.format("There was a problem saving the object : %s", entity));
        }
        return findByEmail(entity.getEmail())
            .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return template.getMapper(AccountReader.class).findByEmail(email);
    }
}
