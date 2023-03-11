package com.example.tubespppb2.model.appointment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.pojo.AppointmentGetDaftarPesertaResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.appointment.IAppointmentDetailPeserta;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDaftarPesertaAppointment {
    private APIInterface apiInterface;
    private SharedPreferenceStore spStore;
    private IAppointmentDetailPeserta iAppointmentDetailPeserta;
    private Context context;
    private IError iError;
    public GetDaftarPesertaAppointment(Context context, IError iError, IAppointmentDetailPeserta iAppointmentDetailPeserta) {
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
        this.context = context;
        this.iError = iError;
        this.iAppointmentDetailPeserta = iAppointmentDetailPeserta;
    }

    public void execute(String appointmentId) {
        ConnectivityManager connMgr = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<List<AppointmentGetDaftarPesertaResponse>> responseCall = this.apiInterface.getDaftarPesertaAppointment(this.spStore.getToken(), appointmentId);

            responseCall.enqueue(new Callback<List<AppointmentGetDaftarPesertaResponse>>() {
                @Override
                public void onResponse(Call<List<AppointmentGetDaftarPesertaResponse>> call, Response<List<AppointmentGetDaftarPesertaResponse>> response) {
                    if (response.isSuccessful()) {
                        iAppointmentDetailPeserta.loadDaftarPesertaDetail(response.body());
                    } else if (response.code() / 100 == 4) {
                        iError.showError("appintment tidak ditemukan");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<List<AppointmentGetDaftarPesertaResponse>> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            iError.showError("tidak ada koneksi internet");
        }
    }
}
