package com.example.tubespppb2.presenter;

import android.content.Context;

import com.example.tubespppb2.service.SharedPreferenceStore;

public class PresenterSplashScreenActivity {
    private SharedPreferenceStore spStore;
    public PresenterSplashScreenActivity(Context context){
        this.spStore = new SharedPreferenceStore(context);
    }

    public String getToken(){
        return this.spStore.getToken();
    }
}
