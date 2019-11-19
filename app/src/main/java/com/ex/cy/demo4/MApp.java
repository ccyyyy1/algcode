package com.ex.cy.demo4;

import android.app.Application;
import android.content.Context;

public class MApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
