package com.example.tubespppb2.model.appointment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.pojo.UsersGetByEmailResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.appointment.IAppointment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUserByEmail {
    private APIInterface apiInterface;
    private Context context;
    private IError iError;
    private SharedPreferenceStore spStore;
    private IAppointment iAppointment;

    public GetUserByEmail(Context context, IError iError, IAppointment iAppointment) {
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.iError = iError;
        this.spStore = new SharedPreferenceStore(context);
        this.context = context;
        this.iAppointment = iAppointment;
    }

    public void execute(String email) {
        ConnectivityManager connMgr = (ConnectivityManager)this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<UsersGetByEmailResponse> responseCall = this.apiInterface.getUserByEmail(this.spStore.getToken(), email);

            responseCall.enqueue(new Callback<UsersGetByEmailResponse>() {
                @Override
                public void onResponse(Call<UsersGetByEmailResponse> call, Response<UsersGetByEmailResponse> response) {
                    if (response.isSuccessful()) {
                        iAppointment.emailExist(response.body());
                    } else if (response.code() / 100 == 4) {
                        iError.showError("akun tidak ada");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<UsersGetByEmailResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            iError.showError("tidak ada koneksi internet");
        }
    }
}
