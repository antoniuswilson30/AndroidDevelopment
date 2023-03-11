package com.example.tubespppb2.model.announcement;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.util.Log;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.pojo.AnnouncementGetDaftarResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.announcement.IAnnouncement;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDaftarAnnouncement {
    private APIInterface apiInterface;
    private SharedPreferenceStore spStore;
    private IError iError;
    private Context context;
    private IAnnouncement iAnnouncement;
    public GetDaftarAnnouncement(Context context, IError iError, IAnnouncement iAnnouncement){
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
        this.iError = iError;
        this.context = context;
        this.iAnnouncement = iAnnouncement;
    }

    public void execute(String title, List<String> tags, String cursor, String limit){
        if(title!= null && title.trim().isEmpty()){
           title = null;
        }
        if(tags != null && tags.size() == 0){
            tags = null;
        }
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<AnnouncementGetDaftarResponse> responseCall = this.apiInterface.getDaftarAnnoucement(this.spStore.getToken(), title, tags, cursor, limit);
            responseCall.enqueue(new Callback<AnnouncementGetDaftarResponse>() {
                @Override
                public void onResponse(Call<AnnouncementGetDaftarResponse> call, Response<AnnouncementGetDaftarResponse> response) {
                    Log.d("msg", response.code() + "");
                    if (response.isSuccessful()) {
                        iAnnouncement.addData(response.body());
                    } else if (response.code() / 100 == 4) {
                        iError.showError("ada kesalahan pengisian data");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<AnnouncementGetDaftarResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        }else{
            iError.showError("tidak ada koneksi internet");
        }
    }
}
