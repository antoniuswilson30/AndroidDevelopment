package com.example.tubespppb2.model.appointment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.exception.InvalidDateException;
import com.example.tubespppb2.pojo.AppointmentGetDaftarOwnerAndParticipantResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.appointment.IAppointment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDaftarAppointmentOwnerAndParticipant {
    private APIInterface apiInterface;
    private SharedPreferenceStore spStore;
    private IError iError;
    private Context context;
    private IAppointment iAppointment;
    public GetDaftarAppointmentOwnerAndParticipant(Context context, IError iError, IAppointment iAppointment) {
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
        this.context = context;
        this.iError = iError;
        this.iAppointment = iAppointment;
    }

    public void execute(String startDate, String endDate) throws InvalidDateException{
        this.validateDate(startDate,endDate);
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<List<AppointmentGetDaftarOwnerAndParticipantResponse>> responseCall = this.apiInterface.getDaftarAppointmentOwnerParticipant(
                    this.spStore.getToken(), startDate, endDate);
            responseCall.enqueue(new Callback<List<AppointmentGetDaftarOwnerAndParticipantResponse>>() {
                @Override
                public void onResponse(Call<List<AppointmentGetDaftarOwnerAndParticipantResponse>> call, Response<List<AppointmentGetDaftarOwnerAndParticipantResponse>> response) {
                    if (response.isSuccessful()) {
                        iAppointment.addData(response.body());
                    } else if (response.code() / 100 == 4) {
                        iError.showError("range tanggal lebih dari 7 hari");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<List<AppointmentGetDaftarOwnerAndParticipantResponse>> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            iError.showError("tidak ada koneksi internet");
        }
    }

    private void validateDate(String startDate, String endDate) throws InvalidDateException{
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;
        Date now = new Date();
        try {
            start = dateFormat.parse(startDate);
            end = dateFormat.parse(endDate);
            long diff = (end.getTime()-start.getTime())/(1000 * 60 * 60 * 24);
            if(start.after(end)){
                throw  new InvalidDateException("end date lebih akhir daripada start date");
            }else if(diff > 7){
                throw  new InvalidDateException("range tanggal lebih dari 7 hari");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
