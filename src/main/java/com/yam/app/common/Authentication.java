package com.yam.app.common;

import java.io.Serializable;

public interface Authentication extends Serializable {

    String getCredentials();

    String getRole();

    Long getMemberId();
}
