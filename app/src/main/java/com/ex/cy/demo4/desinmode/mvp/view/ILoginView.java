package com.ex.cy.demo4.desinmode.mvp.view;

public interface ILoginView {
    String getUser();
    String getPwd();

    void startShowProgress();
    void stopShowProgress();

    void showLoginSuccess();
    void showLoginFail();

    void showOtherMsg(String msg);
}
