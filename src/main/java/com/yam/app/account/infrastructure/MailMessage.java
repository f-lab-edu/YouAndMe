package com.yam.app.account.infrastructure;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class MailMessage {

    private String to;
    private String subject;
    private String message;
}
