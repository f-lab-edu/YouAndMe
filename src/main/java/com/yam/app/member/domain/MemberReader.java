package com.yam.app.member.domain;

import java.util.Optional;

public interface MemberReader {

    boolean existsByNickname(String nickname);

    Optional<Member> findById(Long memberId);
}
