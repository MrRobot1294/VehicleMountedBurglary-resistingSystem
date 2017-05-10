package com.cjy.safecar.login;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cjy.safecar.register.RegisterActivity;
import com.cjy.safecar.service.MyService;
import com.example.safecar.R;

import user.User;
import user.UserActivity;

public class LoginActivity extends AppCompatActivity implements ServiceConnection {

    private TextView info;
    private EditText userNumber;
    private EditText userPassword;
    private Button signIn;
    private Button signUp;

    private ProgressDialog progressDialog;

    private MyService.MyBinder binder;
    private MyService myService;

    private IntentFilter signInIntentFilter = new IntentFilter("com.broadcast.SIGNIN");
    private SignInReceiver signInReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        info = (TextView) findViewById(R.id.info);
        userNumber = (EditText) findViewById(R.id.telephonnumber);
        userPassword = (EditText) findViewById(R.id.password);
        signIn = (Button)findViewById(R.id.signin);
        signUp = (Button)findViewById(R.id.signup);

        signInReceiver = new SignInReceiver();
        bindService(new Intent(LoginActivity.this,MyService.class),LoginActivity.this,BIND_AUTO_CREATE);
        registerReceiver(signInReceiver,signInIntentFilter);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userNumber.getText().toString() != null && userPassword.getText().toString() != null) {
                    User user = new User(null, userNumber.getText().toString().trim(), userPassword.getText().toString().trim(), null);
                    myService.userSignIn(user);

                    //这里的话参数依次为,上下文,标题,内容,是否显示进度,是否可以用取消按钮关闭
                    progressDialog = ProgressDialog.show(LoginActivity.this, "正在登录", "正在登录中,请稍后...", false, true);
                } else {
                    info.setVisibility(View.VISIBLE);
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(binder != null) {
            unbindService(LoginActivity.this);
            myService = null;
            binder = null;
            unregisterReceiver(signInReceiver);
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

    public class SignInReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("signin").equals("success")) {
                Intent i = new Intent(LoginActivity.this , UserActivity.class);
                startActivity(i);
                progressDialog.dismiss();
                LoginActivity.this.finish();
            } else if(intent.getStringExtra("signin").equals("error")) {
                info.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
        }
    }
}
