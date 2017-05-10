package com.cjy.safecar.register;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cjy.safecar.service.MyService;
import com.example.safecar.R;

import user.User;

public class RegisterActivity extends AppCompatActivity implements ServiceConnection {

    private EditText username;
    private EditText account;
    private EditText password;
    private EditText passwordConform;
    private Button signup;

    private TextView username_info;
    private TextView account_info;
    private TextView password_info;

    private ProgressDialog progressDialog;

    private MyService.MyBinder binder;
    private MyService myService;

    private IntentFilter signUpIntentFilter = new IntentFilter("com.broadcast.SIGNUP");
    private SignUpReceiver signUpReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username_edit);
        account = (EditText) findViewById(R.id.account_edit);
        password = (EditText) findViewById(R.id.password_edit);
        passwordConform = (EditText) findViewById(R.id.password_conform_edit);

        username_info = (TextView) findViewById(R.id.username_info);
        account_info = (TextView) findViewById(R.id.account_info);
        password_info = (TextView) findViewById(R.id.password_info);

        signup = (Button) findViewById(R.id.signup);

        signUpReceiver = new SignUpReceiver();
        bindService(new Intent(RegisterActivity.this, MyService.class), RegisterActivity.this, BIND_AUTO_CREATE);
        registerReceiver(signUpReceiver, signUpIntentFilter);

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (username.getText().toString().trim().equals("")) {
                        username_info.setText("账号不能为空");
                        username_info.setVisibility(View.VISIBLE);
                    } else {
                        username_info.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (account.getText().toString().trim().equals("")) {
                        account_info.setText("账号不能为空");
                        account_info.setVisibility(View.VISIBLE);
                    } else {
                        account_info.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (password.getText().toString().trim().equals("")) {
                        password_info.setText("密码不能为空");
                        password_info.setVisibility(View.VISIBLE);
                    } else {
                        password_info.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        passwordConform.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (passwordConform.getText().toString().trim().equals(password.getText().toString().trim())) {
                        password_info.setText("密码不一致");
                        password_info.setVisibility(View.VISIBLE);
                    } else {
                        password_info.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account.getText().toString() != null && password.getText().toString() != null && password.getText().toString().trim().equals(passwordConform.getText().toString().trim())) {
                    User user = new User(username.getText().toString().trim(), account.getText().toString().trim(), password.getText().toString().trim(), null);
                    myService.userSignUp(user);

                    //这里的话参数依次为,上下文,标题,内容,是否显示进度,是否可以用取消按钮关闭
                    progressDialog = ProgressDialog.show(RegisterActivity.this, "正在注册", "正在注册中,请稍后...", false, true);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binder != null) {
            unbindService(RegisterActivity.this);
            myService = null;
            binder = null;
            unregisterReceiver(signUpReceiver);
//            Toast.makeText(getApplicationContext(),"unbindservice",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder = (MyService.MyBinder) service;
        myService = binder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        binder = null;
        myService = null;
    }

    public class SignUpReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("signup").equals("success")) {
                progressDialog.dismiss();
                new AlertDialog.Builder(RegisterActivity.this).setTitle("注册").setMessage("注册成功").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RegisterActivity.this.finish();
                    }
                }).show();
            } else if (intent.getStringExtra("signup").equals("error")) {
                account_info.setText("账号已存在");
                account_info.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
        }
    }
}
