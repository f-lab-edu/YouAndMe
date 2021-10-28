package com.yam.app.member.domain;

public interface MemberRepository {

    Long save(Member entity);

    void update(Member entity);
}
