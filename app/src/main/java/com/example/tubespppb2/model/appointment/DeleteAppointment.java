package com.example.tubespppb2.model.appointment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.pojo.AppointmentDeleteResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.appointment.IAppointment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteAppointment {
    private APIInterface apiInterface;
    private SharedPreferenceStore spStore;
    private Context context;
    private IError iError;
    private IAppointment iAppointment;

    public DeleteAppointment(Context context, IError iError, IAppointment iAppointment) {
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
        this.context = context;
        this.iAppointment = iAppointment;
        this.iError = iError;
    }

    public void execute(String appointmentId) {
        ConnectivityManager connMgr = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<AppointmentDeleteResponse> responseCall = this.apiInterface.deleteAppointment(this.spStore.getToken(), appointmentId);
            responseCall.enqueue(new Callback<AppointmentDeleteResponse>() {
                @Override
                public void onResponse(Call<AppointmentDeleteResponse> call, Response<AppointmentDeleteResponse> response) {
                    if (response.isSuccessful()) {
                        iAppointment.deleteAppointmentSuccess();
                    } else if (response.code() / 100 == 4) {
                        iError.showError("appintment tidak ditemukan");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<AppointmentDeleteResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            iError.showError("tidak ada koneksi internet");
        }
    }
}
