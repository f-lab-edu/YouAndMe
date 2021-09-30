package com.yam.app.member.domain;

public interface MemberRepository {

    void save(Member entity);

    void update(Member entity);
}
