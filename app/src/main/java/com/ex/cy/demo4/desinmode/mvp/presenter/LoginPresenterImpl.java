package com.ex.cy.demo4.desinmode.mvp.presenter;

import android.text.TextUtils;

import com.ex.cy.demo4.desinmode.mvp.model.ILoginModel;
import com.ex.cy.demo4.desinmode.mvp.view.ILoginView;

public class LoginPresenterImpl implements ILoginPresenter {
    ILoginView loginView;
    ILoginModel loginModel;

    public LoginPresenterImpl(ILoginView loginView, ILoginModel loginModel) {
        this.loginView = loginView;
        this.loginModel = loginModel;
    }

    @Override
    public void login() {
        final String user = loginView.getUser();
        final String pwd = loginView.getPwd();

        if (TextUtils.isEmpty(user)) {
            loginView.showOtherMsg("user is Null!");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            loginView.showOtherMsg("pwd is Null!");
            return;
        }

        loginView.startShowProgress();
        new Thread() {
            @Override
            public void run() {
                loginModel.doLogin(user, pwd, new ILoginModel.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        loginView.showOtherMsg("login Success!");
                        loginView.stopShowProgress();
                        loginView.showLoginSuccess();
                    }

                    @Override
                    public void onLoginFail() {
                        loginView.showOtherMsg("login Fail!");
                        loginView.stopShowProgress();
                        loginView.showLoginFail();
                    }
                });
            }
        }.start();

    }
}
