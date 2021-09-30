package com.yam.app.member.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class Member {

    private Long id;
    private String nickname;
    private String image;

    public Member(String nickname, String image) {
        this.nickname = nickname;
        this.image = image;
    }

    public void changeProfile(String nickname, String image) {
        this.nickname = nickname;
        this.image = image;
    }
}
