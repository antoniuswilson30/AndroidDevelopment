package com.example.tubespppb2.presenter;

import android.content.Context;

import com.example.tubespppb2.IError;
import com.example.tubespppb2.ILoginActivity;
import com.example.tubespppb2.exception.InvalidEmailException;
import com.example.tubespppb2.exception.InvalidRoleException;
import com.example.tubespppb2.model.account_auth.PostAuthenticate;

public class PresenterLoginActivity {
    private PostAuthenticate postAuthenticate;
    private ILoginActivity iLoginActivity;
    private IError iError;
    private Context context;

    public PresenterLoginActivity(Context context, IError iError, ILoginActivity iLoginActivity) {
        this.postAuthenticate = new PostAuthenticate(context, iError, iLoginActivity);
        this.iLoginActivity = iLoginActivity;
        this.iError = iError;
        this.context = context;
    }

    public void setPostAuthenticate(String email, String password, String role) {
        try {
            this.iLoginActivity.removeRoleError();
            this.iLoginActivity.removeEmailError();
            this.postAuthenticate.execute(email, password, role);
        } catch (InvalidEmailException e) {
            this.iLoginActivity.showEmailError();
        } catch (InvalidRoleException e) {
            this.iLoginActivity.showRoleError();
        }
    }
}
