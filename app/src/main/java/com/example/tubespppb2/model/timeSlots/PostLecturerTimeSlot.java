package com.example.tubespppb2.model.timeSlots;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.util.Log;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.exception.InvalidDateException;
import com.example.tubespppb2.exception.InvalidFieldException;
import com.example.tubespppb2.pojo.LecturerGetTimeSlotsResponse;
import com.example.tubespppb2.pojo.LecturerPostTimeSlotsRequest;
import com.example.tubespppb2.pojo.LecturerPostTimeSlotsResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.appointment.IAppointmentJadwalDosen;
import com.example.tubespppb2.view.timeSlot.ITimeSlot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostLecturerTimeSlot {
    private SharedPreferenceStore spStore;
    private APIInterface apiInterface;
    private IError iError;
    private ITimeSlot iTimeSlot;
    private Context context;

    public PostLecturerTimeSlot(Context context, IError iError, ITimeSlot iTimeSlot){
        this.spStore = new SharedPreferenceStore(context);
        this.iTimeSlot = iTimeSlot;
        this.context = context;
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.iError = iError;
    }

    public void execute(String day, String start_time, String end_time) throws InvalidDateException, InvalidFieldException {
        if(start_time == null || end_time == null){
            throw new InvalidFieldException("waktu harus diisi");
        }
        this.validate(start_time,end_time);
        if(day.trim().isEmpty()|| day.equals("Pilih Hari")){
            throw new InvalidFieldException("hari harus diisi");
        }
        ConnectivityManager connMgr = (ConnectivityManager)this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<LecturerPostTimeSlotsResponse> responseCall = this.apiInterface.postLecturerTimeSlots(this.spStore.getToken(),
                    new LecturerPostTimeSlotsRequest(day,start_time,end_time));

            responseCall.enqueue(new Callback<LecturerPostTimeSlotsResponse>() {
                @Override
                public void onResponse(Call<LecturerPostTimeSlotsResponse> call, Response<LecturerPostTimeSlotsResponse> response) {
                    if (response.isSuccessful()) {
                        iTimeSlot.postTimeSlotSuccess();
                    } else if (response.code() / 100 == 4) {
                        iError.showError("ada jadwal yang bentrok");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<LecturerPostTimeSlotsResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            iError.showError("tidak ada koneksi internet");
        }
    }

    private void validate(String start_time, String end_time) throws InvalidDateException {
        DateFormat timeFormat = new SimpleDateFormat("HH:mmZ");
        Date _startTime = null;
        Date _endTime = null;
        try {
            _startTime = timeFormat.parse(start_time);
            _endTime = timeFormat.parse(end_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(_startTime.after(_endTime)){
            throw new InvalidDateException("waktu akhir tidak boleh lebih awal dari waktu mulai");
        }
    }
}
