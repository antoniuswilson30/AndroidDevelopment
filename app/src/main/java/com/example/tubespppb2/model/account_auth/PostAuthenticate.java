package com.example.tubespppb2.model.account_auth;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.ILoginActivity;
import com.example.tubespppb2.exception.InvalidEmailException;
import com.example.tubespppb2.exception.InvalidRoleException;
import com.example.tubespppb2.pojo.AuthenticatePostRequest;
import com.example.tubespppb2.pojo.AuthenticatePostResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAuthenticate {
    private APIInterface apiInterface;
    private SharedPreferenceStore spStore;
    private IError iError;
    private ILoginActivity iLoginActivity;
    private Context context;

    public PostAuthenticate(Context context, IError iError, ILoginActivity iLoginActivity) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
        this.context = context;
        this.iError = iError;
        this.iLoginActivity = iLoginActivity;
    }

    public void execute(String email, String password, String role) throws InvalidEmailException, InvalidRoleException {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            if (validateEmail(email) == false) {
                throw new InvalidEmailException("email tidak sesuai");
            }

            if (role.equals("")) {
                throw new InvalidRoleException("role tidak diisi");
            }
            Call<AuthenticatePostResponse> responseCall = apiInterface.getUserToken(
                    new AuthenticatePostRequest(email, password, role)
            );

            responseCall.enqueue(new Callback<AuthenticatePostResponse>() {
                @Override
                public void onResponse(Call<AuthenticatePostResponse> call, Response<AuthenticatePostResponse> response) {
                    if (response.isSuccessful()) {
                        spStore.setToken("Bearer " + response.body().token);
                        spStore.setRole(role);
                        iLoginActivity.goToHomePage();
                    } else if (response.code() / 100 == 4) {
                        iError.showError("akun tidak ada");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<AuthenticatePostResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            this.iError.showError("tidak ada koneksi internet");
        }
    }

    private boolean validateEmail(String email) {
        String regex = "^(.+)@(\\S+)$";
        return Pattern.compile(regex)
                .matcher(email)
                .matches();
    }
}
