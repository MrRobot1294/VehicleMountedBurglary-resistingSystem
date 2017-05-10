package com.example.safecar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cjy.safecar.login.LoginActivity;
import com.cjy.safecar.service.MyService;


public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(MainActivity.this , LoginActivity.class);
        startActivity(i);

        startService(new Intent(this,MyService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopService(new Intent(MainActivity.this ,MyService.class));
    }
}
