package com.yam.app.member.infrastructure;

import com.yam.app.member.domain.GenerateMemberEvent;
import com.yam.app.member.domain.Member;
import com.yam.app.member.domain.MemberReader;
import com.yam.app.member.domain.MemberRepository;
import com.yam.app.member.domain.RegisterAccountConfirmEvent;
import com.yam.app.member.domain.UpdateAccountEvent;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MemberEventListener {

    private final MemberReader memberReader;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher publisher;

    public MemberEventListener(MemberReader memberReader,
        MemberRepository memberRepository,
        ApplicationEventPublisher publisher) {
        this.memberReader = memberReader;
        this.memberRepository = memberRepository;
        this.publisher = publisher;
    }

    @EventListener
    public void handle(RegisterAccountConfirmEvent event) {
        var nickname = event.getEmail().split("@")[0];
        Long savedEntityId;
        if (memberReader.existsByNickname(nickname)) {
            savedEntityId = memberRepository.save(
                new Member(UUID.randomUUID().toString(), "temp.png"));
        } else {
            savedEntityId = memberRepository.save(new Member(nickname, "temp.png"));
        }
        publisher.publishEvent(new GenerateMemberEvent(savedEntityId, event.getEmail()));
    }

    @EventListener
    public void handle(UpdateAccountEvent event) {
        var member = memberReader.findById(event.getMemberId())
            .orElseThrow(IllegalArgumentException::new);
        member.changeProfile(event.getNickname(), event.getImage());
        memberRepository.update(member);
    }
}
