package com.example.tubespppb2.model.account_auth;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.pojo.UsersGetSelfResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUsersSelf {
    private APIInterface apiInterface;
    private SharedPreferenceStore spStore;
    private IError iError;
    private Context context;

    public GetUsersSelf(Context context, IError iError) {
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
        this.iError = iError;
        this.context = context;
    }

    public void execute() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<UsersGetSelfResponse> responseCall = this.apiInterface.getUsersSelf(this.spStore.getToken());

            responseCall.enqueue(new Callback<UsersGetSelfResponse>() {
                @Override
                public void onResponse(Call<UsersGetSelfResponse> call, Response<UsersGetSelfResponse> response) {
                    if (response.isSuccessful()) {
                        spStore.setUserId(response.body().id);
                        spStore.setNama(response.body().name);
                        spStore.setEmail(response.body().email);
                    } else if (response.code() / 100 == 4) {
                        iError.showError("akun tidak ada");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<UsersGetSelfResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            this.iError.showError("tidak ada koneksi internet");
        }
    }
}
