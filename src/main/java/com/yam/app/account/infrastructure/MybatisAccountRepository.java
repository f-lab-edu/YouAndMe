package com.yam.app.account.infrastructure;

import com.yam.app.account.domain.Account;
import com.yam.app.account.domain.AccountReader;
import com.yam.app.account.domain.AccountRepository;
import org.mybatis.spring.SqlSessionTemplate;

public final class MybatisAccountRepository implements AccountRepository, AccountReader {

    private final SqlSessionTemplate template;

    private static final String COMMAND_NAMESPACE = "com.yam.app.account.domain.AccountRepository.";
    private static final String READER_NAMESPACE = "com.yam.app.account.domain.AccountReader.";

    public MybatisAccountRepository(SqlSessionTemplate template) {
        this.template = template;
    }

    @Override
    public boolean existsByEmail(String email) {
        int result = template.selectOne(COMMAND_NAMESPACE + "existsByEmail", email);
        return result != 0;
    }

    @Override
    public Account update(Account entity) {
        int result = template.update(COMMAND_NAMESPACE + "update", entity);
        if (result != 1) {
            throw new RuntimeException(
                String.format("There was a problem updating the object : %s", entity));
        }
        return findByEmail(entity.getEmail());
    }

    @Override
    public boolean existsByNickname(String nickname) {
        int result = template.selectOne(COMMAND_NAMESPACE + "existsByNickname", nickname);
        return result != 0;
    }

    @Override
    public Account save(Account entity) {
        int result = template.insert(COMMAND_NAMESPACE + "save", entity);
        if (result != 1) {
            throw new RuntimeException(
                String.format("There was a problem saving the object : %s", entity));
        }
        return findByEmail(entity.getEmail());
    }

    @Override
    public Account findByEmail(String email) {
        return template.selectOne(READER_NAMESPACE + "findByEmail", email);
    }
}
