package com.example.tubespppb2.model.appointment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.pojo.AppointmentGetDaftarUndanganResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.appointment.IUndanganAppointment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDaftarUndanganAppointment {
    private SharedPreferenceStore spStore;
    private APIInterface apiInterface;
    private Context context;
    private IError iError;
    private IUndanganAppointment iUndanganAppointment;

    public GetDaftarUndanganAppointment(Context context, IError iError, IUndanganAppointment iUndanganAppointment) {
        this.spStore = new SharedPreferenceStore(context);
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.context = context;
        this.iUndanganAppointment = iUndanganAppointment;
        this.iError = iError;
    }

    public void execute() {
        ConnectivityManager connMgr = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<List<AppointmentGetDaftarUndanganResponse>> responseCall = this.apiInterface.getDaftarUndanganAppointment(this.spStore.getToken());

            responseCall.enqueue(new Callback<List<AppointmentGetDaftarUndanganResponse>>() {
                @Override
                public void onResponse(Call<List<AppointmentGetDaftarUndanganResponse>> call, Response<List<AppointmentGetDaftarUndanganResponse>> response) {
                    if (response.isSuccessful()) {
                        iUndanganAppointment.getDaftarUndanganSuccess(response.body());
                    } else if (response.code() / 100 == 4) {
                        iError.showError("akun tidak memiliki akses");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<List<AppointmentGetDaftarUndanganResponse>> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            iError.showError("tidak ada koneksi internet");
        }
    }
}
