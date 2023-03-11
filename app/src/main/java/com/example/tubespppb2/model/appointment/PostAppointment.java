package com.example.tubespppb2.model.appointment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.util.Log;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.exception.InvalidDateException;
import com.example.tubespppb2.exception.InvalidFieldException;
import com.example.tubespppb2.pojo.AppointmentPostRequest;
import com.example.tubespppb2.pojo.AppointmentPostResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.appointment.IAppointment;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class PostAppointment {
    private APIInterface apiInterface;
    private SharedPreferenceStore spStore;
    private Context context;
    private IError iError;
    private IAppointment iAppointment;
    public PostAppointment(Context context, IError iError, IAppointment iAppointment) {
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
        this.context = context;
        this.iError = iError;
        this.iAppointment = iAppointment;
    }

    public void execute(String title, String description, String start_datetime, String end_datetime) throws InvalidFieldException, InvalidDateException {
        if(title.trim().isEmpty()){
            throw new InvalidFieldException("title tidak boleh kosong");
        }

        if(description.trim().isEmpty()){
            description = null;
        }

        if(start_datetime == null || end_datetime == null){
            throw new InvalidFieldException("tanggal atau waktu tidak boleh kosong");
        }

        this.validate(start_datetime, end_datetime);

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<AppointmentPostResponse> responseCall = apiInterface.postAppointment(this.spStore.getToken(),
                    new AppointmentPostRequest(title, description, start_datetime, end_datetime));

            responseCall.enqueue(new Callback<AppointmentPostResponse>() {
                @Override
                public void onResponse(Call<AppointmentPostResponse> call, Response<AppointmentPostResponse> response) {
                    Log.d("msg", response.code()+"");
                    if (response.isSuccessful()) {
                        iAppointment.addAppointmentSuccess(response.body().id);
                    } else if (response.code() / 100 == 4) {
                        Converter<ResponseBody, AppointmentPostResponse> converter =
                                APIClient.getClient().responseBodyConverter(AppointmentPostResponse.class, new Annotation[0]);
                        AppointmentPostResponse appointmentPostResponse = null;
                        try {
                            appointmentPostResponse = converter.convert(response.errorBody());
                            String errMsg = appointmentPostResponse.errcode;
                            if(errMsg.equals("E_INVALID_VALUE")){
                                iError.showError("tidak boleh lebih awal dari waktu saat ini");
                            }else{
                                iError.showError("ada jadwal yang bentrok");
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<AppointmentPostResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            iError.showError("tidak ada koneksi internet");
        }
    }

    private void validate(String startDate, String endDate) throws InvalidDateException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mmZ");
        Date _startDate = null;
        Date _endDate = null;
        Date _startTime = null;
        Date _endTime = null;
        try {
            _startDate = dateFormat.parse(startDate);
            _endDate = dateFormat.parse(endDate);
            _startTime = timeFormat.parse(startDate);
            _endTime = timeFormat.parse(endDate);
            Log.d("msg",_startTime.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(_startDate.after(_endDate)){
            throw new InvalidDateException("tanggal akhir tidak boleh lebih awal dari tanggal mulai");
        }else if(_startTime.after(_endTime)){
            throw new InvalidDateException("waktu akhir tidak boleh lebih awal dari waktu mulai");
        }
    }
}
