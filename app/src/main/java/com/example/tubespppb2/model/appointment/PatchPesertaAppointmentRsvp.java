package com.example.tubespppb2.model.appointment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.pojo.AppointmentPatchMengubahAppointmentRequest;
import com.example.tubespppb2.pojo.AppointmentPatchMengubahAppointmentResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.appointment.IUndanganAppointment;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class PatchPesertaAppointmentRsvp {
    private APIInterface apiInterface;
    private SharedPreferenceStore spStore;
    private Context context;
    private IError iError;
    private IUndanganAppointment iUndanganAppointment;
    public PatchPesertaAppointmentRsvp(Context context, IError iError, IUndanganAppointment iUndanganAppointment) {
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
        this.iError = iError;
        this.context = context;
        this.iUndanganAppointment = iUndanganAppointment;
    }

    public void execute(String appointmentId, boolean attending) {
        ConnectivityManager connMgr = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<AppointmentPatchMengubahAppointmentResponse> responseCall = this.apiInterface.patchUbahAppointment(this.spStore.getToken(),
                    appointmentId, this.spStore.getUserId(),new AppointmentPatchMengubahAppointmentRequest(attending));

            responseCall.enqueue(new Callback<AppointmentPatchMengubahAppointmentResponse>() {
                @Override
                public void onResponse(Call<AppointmentPatchMengubahAppointmentResponse> call, Response<AppointmentPatchMengubahAppointmentResponse> response) {
                    if (response.isSuccessful()) {
                        iUndanganAppointment.patchAppointmentRSVPSuccess();
                    } else if (response.code() / 100 == 4) {
                        Converter<ResponseBody, AppointmentPatchMengubahAppointmentResponse> converter =
                                APIClient.getClient().responseBodyConverter(AppointmentPatchMengubahAppointmentResponse.class, new Annotation[0]);
                        AppointmentPatchMengubahAppointmentResponse appointmentPatchMengubahAppointmentResponse  = null;
                        try {
                            appointmentPatchMengubahAppointmentResponse = converter.convert(response.errorBody());
                            String errMsg = appointmentPatchMengubahAppointmentResponse.errcode;
                            if(errMsg.equals("E_NOT_EXIST")){
                                iError.showError("pertemuan tidak ditemukan");
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
                public void onFailure(Call<AppointmentPatchMengubahAppointmentResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            iError.showError("tidak ada koneksi internet");
        }
    }
}
