package com.yam.app.account.infrastructure;

import com.yam.app.account.domain.Account;
import com.yam.app.account.domain.AccountReader;
import com.yam.app.account.domain.AccountRepository;
import com.yam.app.account.domain.MemberAccount;
import java.util.Optional;
import org.mybatis.spring.SqlSessionTemplate;

public final class MybatisAccountRepository implements AccountRepository, AccountReader {

    private static final String SAVE_FQCN = "com.yam.app.account.domain.AccountRepository.save";
    private static final String UPDATE_FQCN = "com.yam.app.account.domain.AccountRepository.update";

    private final SqlSessionTemplate template;

    public MybatisAccountRepository(SqlSessionTemplate template) {
        this.template = template;
    }

    @Override
    public boolean existsByEmail(String email) {
        return template.getMapper(AccountReader.class).existsByEmail(email);
    }

    @Override
    public void update(Account entity) {
        int result = template.update(UPDATE_FQCN, entity);
        if (result != 1) {
            throw new IllegalStateException(String.format(
                "Unintentionally, more records were updated than expected. : %s", entity));
        }
    }

    @Override
    public void save(Account entity) {
        int result = template.insert(SAVE_FQCN, entity);
        if (result != 1) {
            throw new IllegalStateException(String.format(
                "Unintentionally, more records were saved than expected. : %s", entity));
        }
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return template.getMapper(AccountReader.class).findByEmail(email);
    }

    @Override
    public MemberAccount findByEmailAndMemberId(String email,
        Long memberId) {
        return template.getMapper(AccountReader.class).findByEmailAndMemberId(email, memberId);
    }
}
