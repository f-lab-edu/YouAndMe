package com.yam.app.adapter;

import com.yam.app.account.domain.RegisterAccountConfirmEvent;
import com.yam.app.member.domain.GenerateMemberEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class DomainEventTranslator {

    private final ApplicationEventPublisher publisher;

    public DomainEventTranslator(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @EventListener
    public void translate(RegisterAccountConfirmEvent event) {
        publisher.publishEvent(
            new com.yam.app.member.domain.RegisterAccountConfirmEvent(event.getEmail()));
    }

    @EventListener
    public void translate(GenerateMemberEvent event) {
        publisher.publishEvent(
            new com.yam.app.account.domain.GenerateMemberEvent(event.getMemberId(),
                event.getEmail()));
    }

}
