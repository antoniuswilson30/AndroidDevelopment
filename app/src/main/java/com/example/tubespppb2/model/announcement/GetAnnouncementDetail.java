package com.example.tubespppb2.model.announcement;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.pojo.AnnouncementGetResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.announcement.IAnnouncement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAnnouncementDetail {
    private APIInterface apiInterface;
    private SharedPreferenceStore spStore;
    private IError iError;
    private IAnnouncement iAnnouncement;
    private Context context;

    public GetAnnouncementDetail(Context context, IError iError, IAnnouncement iAnnouncement) {
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
        this.iError = iError;
        this.iAnnouncement = iAnnouncement;
        this.context = context;
    }

    public void execute(String announcement_id) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<AnnouncementGetResponse> responseCall = this.apiInterface.getAnnouncementDetail(this.spStore.getToken(), announcement_id);
            responseCall.enqueue(new Callback<AnnouncementGetResponse>() {
                @Override
                public void onResponse(Call<AnnouncementGetResponse> call, Response<AnnouncementGetResponse> response) {
                    if (response.isSuccessful()) {
                        iAnnouncement.showDetail(response.body());
                    } else if (response.code() / 100 == 4) {
                        iError.showError("ada kesalahan pengisian data");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<AnnouncementGetResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            iError.showError("tidak ada koneksi internet");
        }
    }
}
