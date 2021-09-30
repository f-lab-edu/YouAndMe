package com.yam.app.member.domain;

import java.util.Optional;

public interface MemberReader {

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findById(Long memberId);
}
