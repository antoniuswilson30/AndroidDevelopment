package com.example.tubespppb2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tubespppb2.presenter.PresenterSplashScreenActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private PresenterSplashScreenActivity presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_loading);

        this.presenter = new PresenterSplashScreenActivity(getApplicationContext());
        new Handler().postDelayed(()->{
            if(this.presenter.getToken().equals("")){
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            }else {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            }
            finish();
        },3000);
    }
}
