package com.yam.app.account.domain;

public interface LoginAccountProcessor {

    void login(String email, String password);

    void logout();
}
