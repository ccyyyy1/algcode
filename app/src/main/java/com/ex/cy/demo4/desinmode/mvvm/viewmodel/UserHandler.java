package com.ex.cy.demo4.desinmode.mvvm.viewmodel;


import android.view.View;

public abstract class UserHandler {
    public int progressVisiable = View.GONE;

    public abstract void onLoginClick();
}
