package com.yam.app.member.domain;

import com.yam.app.common.EntityStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Member {

    private Long id;
    private String nickname;
    private String image;
    private EntityStatus status = EntityStatus.ALIVE;

    public Member(String nickname, String image) {
        this.nickname = nickname;
        this.image = image;
    }

    public void changeProfile(String nickname, String image) {
        this.nickname = nickname;
        this.image = image;
    }
}
