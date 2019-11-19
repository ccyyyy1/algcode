package com.ex.cy.demo4.desinmode.mvp.model;

public class LoginModelImpl implements ILoginModel {
    @Override
    public void doLogin(String user, String pwd, OnLoginListener onLoginListener){
        try {
            Thread.sleep(1000); //Network block
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (user.equals("1") && pwd.equals("1")) {
            onLoginListener.onLoginSuccess();
        } else {
            onLoginListener.onLoginFail();
        }
    }
}
