package com.example.tubespppb2.model.timeSlots;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.pojo.LecturerDeleteTimeSlotsResponse;
import com.example.tubespppb2.pojo.LecturerGetTimeSlotsResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.appointment.IAppointmentJadwalDosen;
import com.example.tubespppb2.view.timeSlot.ITimeSlot;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteLecturerTimeSlot {
    private SharedPreferenceStore spStore;
    private APIInterface apiInterface;
    private IError iError;
    private ITimeSlot iTimeSlot;
    private Context context;

    public DeleteLecturerTimeSlot(Context context, IError iError, ITimeSlot iTimeSlot){
        this.spStore = new SharedPreferenceStore(context);
        this.iTimeSlot = iTimeSlot;
        this.context = context;
        this.iError = iError;
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    public void execute(String timeSlotId){
        ConnectivityManager connMgr = (ConnectivityManager)this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<LecturerDeleteTimeSlotsResponse> responseCall = this.apiInterface.deleteLecturerTimeSlots(this.spStore.getToken(), timeSlotId);
            responseCall.enqueue(new Callback<LecturerDeleteTimeSlotsResponse>() {
                @Override
                public void onResponse(Call<LecturerDeleteTimeSlotsResponse> call, Response<LecturerDeleteTimeSlotsResponse> response) {
                    if (response.isSuccessful()) {
                        iTimeSlot.deleteTimeSlotSuccess();
                    } else if (response.code() / 100 == 4) {
                        iError.showError("akun tidak memiliki hak akses");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<LecturerDeleteTimeSlotsResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            iError.showError("tidak ada koneksi internet");
        }
    }
}
