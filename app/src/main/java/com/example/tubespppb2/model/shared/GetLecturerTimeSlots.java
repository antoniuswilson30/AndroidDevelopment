package com.example.tubespppb2.model.shared;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.pojo.LecturerGetTimeSlotsResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.appointment.IAppointmentJadwalDosen;
import com.example.tubespppb2.view.timeSlot.ITimeSlot;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetLecturerTimeSlots {
    private SharedPreferenceStore spStore;
    private APIInterface apiInterface;
    private IError iError;
    private IAppointmentJadwalDosen iAppointmentJadwalDosen;
    private ITimeSlot iTimeSlot;
    private Context context;

    public GetLecturerTimeSlots(Context context, IError iError, IAppointmentJadwalDosen iAppointmentJadwalDosen){
        this.spStore = new SharedPreferenceStore(context);
        this.iAppointmentJadwalDosen = iAppointmentJadwalDosen;
        this.iError = iError;
        this.context = context;
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    public GetLecturerTimeSlots(Context context, IError iError, ITimeSlot iTimeSlot){
        this.spStore = new SharedPreferenceStore(context);
        this.iTimeSlot = iTimeSlot;
        this.context = context;
        this.iError = iError;
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    public void execute(String lecturerId, int choice){
        ConnectivityManager connMgr = (ConnectivityManager)this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if(choice == 2){
            lecturerId = this.spStore.getUserId();
        }
        if (networkInfo != null) {
            Call<List<LecturerGetTimeSlotsResponse>> responseCall = this.apiInterface.getLecturerTimeSlots(this.spStore.getToken(), lecturerId);

            responseCall.enqueue(new Callback<List<LecturerGetTimeSlotsResponse>>() {
                @Override
                public void onResponse(Call<List<LecturerGetTimeSlotsResponse>> call, Response<List<LecturerGetTimeSlotsResponse>> response) {
                    if (response.isSuccessful()) {
                        if(choice == 1) {
                            iAppointmentJadwalDosen.loadLecturerTimeSlots(response.body());
                        }else{
                            iTimeSlot.loadTimeSlots(response.body());
                        }
                    } else if (response.code() / 100 == 4) {
                        iError.showError("akun tidak ada");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<List<LecturerGetTimeSlotsResponse>> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            iError.showError("tidak ada koneksi internet");
        }
    }
}
