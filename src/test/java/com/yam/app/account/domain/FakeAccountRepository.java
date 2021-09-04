package com.yam.app.account.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class FakeAccountRepository implements AccountRepository, AccountReader {

    private final Map<Long, Account> data = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Account findByEmail(String email) {
        return data.values().stream()
            .filter(account -> email.equals(account.getEmail()))
            .findAny()
            .orElse(null);
    }

    @Override
    public boolean existsByEmail(String email) {
        return data.values().stream()
            .anyMatch(account -> account.getEmail().equals(email));
    }

    @Override
    public Account update(Account entity) {
        return data.putIfAbsent(entity.getId(), entity);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return data.values().stream()
            .anyMatch(account -> account.getNickname().equals(nickname));
    }

    @Override
    public Account save(Account entity) {
        entity.setId(idGenerator.incrementAndGet());
        data.put(entity.getId(), entity);
        return entity;
    }
}
