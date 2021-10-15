package com.yam.app.account.domain;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class FakeAccountRepository implements AccountRepository, AccountReader {

    private final Map<Long, Account> data = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Optional<Account> findByEmail(String email) {
        return data.values().stream()
            .filter(account -> email.equals(account.getEmail()))
            .findAny();
    }

    @Override
    public MemberAccount findByEmailAndMemberId(String email, Long memberId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean existsByEmail(String email) {
        return data.values().stream()
            .anyMatch(account -> account.getEmail().equals(email));
    }

    @Override
    public void update(Account entity) {
        data.putIfAbsent(entity.getId(), entity);
    }

    @Override
    public void save(Account entity) {
        entity.setId(idGenerator.incrementAndGet());
        data.put(entity.getId(), entity);
    }
}
