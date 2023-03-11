package com.example.tubespppb2.model.appointment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.pojo.AppointmentPostPesertaRequest;
import com.example.tubespppb2.pojo.AppointmentPostPesertaResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.appointment.IAppointment;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class PostPesertaAppointment {
    private APIInterface apiInterface;
    private SharedPreferenceStore spStore;
    private Context context;
    private IError iError;
    private IAppointment iAppointment;
    public PostPesertaAppointment(Context context, IError iError, IAppointment iAppointment) {
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
        this.context = context;
        this.iAppointment = iAppointment;
        this.iError = iError;
    }

    public void execute(String appointmentId, String[] participant) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<List<AppointmentPostPesertaResponse>> responseCall = this.apiInterface.postPesertaAppointment(this.spStore.getToken(),
                    appointmentId, new AppointmentPostPesertaRequest(participant));
            responseCall.enqueue(new Callback<List<AppointmentPostPesertaResponse>>() {
                @Override
                public void onResponse(Call<List<AppointmentPostPesertaResponse>> call, Response<List<AppointmentPostPesertaResponse>> response) {
                    if (response.isSuccessful()) {
                        iAppointment.addParticipantsSuccess();
                    } else if (response.code() / 100 == 4) {
                        Converter<ResponseBody, AppointmentPostPesertaResponse> converter =
                                APIClient.getClient().responseBodyConverter(AppointmentPostPesertaResponse.class, new Annotation[0]);
                        AppointmentPostPesertaResponse appointmentPostPesertaResponse  = null;
                        try {
                            appointmentPostPesertaResponse = converter.convert(response.errorBody());
                            String errMsg = appointmentPostPesertaResponse.errcode;
                            if(errMsg.equals("E_EXIST")){
                                iError.showError("sudah ada participan dengan email yang sama");
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<List<AppointmentPostPesertaResponse>> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            iError.showError("tidak ada koneksi internet");
        }
    }
}
