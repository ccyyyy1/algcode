package com.ex.cy.demo4.desinmode.mvp.aty;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ex.cy.demo4.R;
import com.ex.cy.demo4.desinmode.mvp.model.LoginModelImpl;
import com.ex.cy.demo4.desinmode.mvp.presenter.ILoginPresenter;
import com.ex.cy.demo4.desinmode.mvp.presenter.LoginPresenterImpl;
import com.ex.cy.demo4.desinmode.mvp.view.ILoginView;

public class LoginAty1 extends Activity implements ILoginView {
    private ILoginPresenter loginPresenter;

    private EditText login_user_et;
    private EditText login_pwd_et;
    private Button login_btn;
    private ProgressBar login_pb;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_aty);

        //TODO IOC 此处可用IOC
        loginPresenter = new LoginPresenterImpl(this, new LoginModelImpl());

        login_user_et = (EditText) findViewById(R.id.login_user_et);
        login_pwd_et = (EditText) findViewById(R.id.login_pwd_et);
        login_btn = (Button) findViewById(R.id.login_btn);
        login_pb = (ProgressBar) findViewById(R.id.login_pb);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.login();
            }
        });
    }

    @Override
    public String getUser() {
        return login_user_et.getText().toString();
    }

    @Override
    public String getPwd() {
        return login_pwd_et.getText().toString();
    }

    @Override
    public void startShowProgress() {
        login_pb.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopShowProgress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                login_pb.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showLoginSuccess() {
        Toast.makeText(LoginAty1.this, "login success!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginFail() {
        Toast.makeText(LoginAty1.this, "login fail!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showOtherMsg(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginAty1.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
