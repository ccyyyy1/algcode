package com.ex.cy.demo4.desinmode.mvvm.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

//既是model 又是 view model
public class User extends BaseObservable {
    @Bindable
    public String name;
    public String pwd;

    public User() {
    }

    public User(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
        notifyChange();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(com.ex.cy.demo4.BR.name);
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
