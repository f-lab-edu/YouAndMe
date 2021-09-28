package com.yam.app.adapter;

import com.yam.app.account.domain.RegisterAccountConfirmEvent;
import com.yam.app.member.domain.GenerateMemberEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class DomainEventTranslator {

    private final ApplicationEventPublisher publisher;

    public DomainEventTranslator(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Async
    @TransactionalEventListener
    public void translate(RegisterAccountConfirmEvent event) {
        publisher.publishEvent(
            new com.yam.app.member.domain.RegisterAccountConfirmEvent(event.getEmail()));
    }

    @Async
    @TransactionalEventListener
    public void translate(GenerateMemberEvent event) {
        publisher.publishEvent(
            new com.yam.app.account.domain.GenerateMemberEvent(event.getMemberId(),
                event.getEmail()));
    }

}
