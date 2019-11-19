package com.ex.cy.demo4.desinmode.mvp.model;

public interface ILoginModel {
    interface OnLoginListener{
        void onLoginSuccess();
        void onLoginFail();
    }
    void doLogin(String user, String pwd, OnLoginListener onLoginListener);
}
