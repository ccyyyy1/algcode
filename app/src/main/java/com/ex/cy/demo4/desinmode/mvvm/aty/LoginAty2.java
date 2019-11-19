package com.ex.cy.demo4.desinmode.mvvm.aty;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.ex.cy.demo4.R;
import com.ex.cy.demo4.databinding.LoginAty2Binding;
import com.ex.cy.demo4.desinmode.mvvm.viewmodel.User;
import com.ex.cy.demo4.desinmode.mvvm.viewmodel.UserHandler;


//MVVM

//M 数据
//VM 视图模型 ，修改VIEW，响应View事件（写业务处理事件）
//V

//业务逻辑写在哪里？ activity？ VM？ 另外的类（presenter）？
//activity  VM  都可以 （或者像这个例子，用一个抽象方法，让activity所在文件，定义具体VM的回调内容）

//C
public class LoginAty2 extends Activity {
    private Handler handler = new Handler();
    LoginAty2Binding binding;

    User user;
    UserHandler userHandler;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.login_aty);
        user = new User();
        userHandler = new UserHandler() {//VM
            @Override
            public void onLoginClick() {
                if (TextUtils.isEmpty(user.getName())) {
                    Toast.makeText(LoginAty2.this, "user is null!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(user.getPwd())) {
                    Toast.makeText(LoginAty2.this, "pwd is null!", Toast.LENGTH_SHORT).show();
                    return;
                }

                userHandler.progressVisiable = View.VISIBLE;
                login(user.getName(), user.getPwd());
            }
        };

        binding.setUser(user);
        binding.setUserHandler(userHandler);
    }

    public void login(final String user, final String pwd) {
        //in network Thread, need handler to modify UI
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (user.equals("1") && pwd.equals("1"))
                    showLoginSuccess();
                else
                    showLoginFail();
            }
        }.start();
    }

    public void showLoginSuccess() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginAty2.this, "login success!", Toast.LENGTH_SHORT).show();
                userHandler.progressVisiable = View.GONE;
            }
        });
    }

    public void showLoginFail() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginAty2.this, "login fail!", Toast.LENGTH_SHORT).show();
                userHandler.progressVisiable = View.GONE;
            }
        });
    }

}
