package com.example.tubespppb2.presenter;

import android.content.Context;

import com.example.tubespppb2.IError;
import com.example.tubespppb2.model.account_auth.GetUsersSelf;

public class PresenterMainActivityAccountPage {
    private Context context;
    private GetUsersSelf getUsersSelf;
    private IError iError;
    public PresenterMainActivityAccountPage(Context context, IError iError){
        this.getUsersSelf = new GetUsersSelf(context, iError);
        this.iError = iError;
        this.context = context;
    }

    public void setGetUsersSelf(){
        this.getUsersSelf.execute();
    }
}
