package com.example.tubespppb2.model.announcement;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.util.Log;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.exception.InvalidFieldException;
import com.example.tubespppb2.pojo.AnnouncementPostRequest;
import com.example.tubespppb2.pojo.AnnouncementPostResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.announcement.IAnnouncement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAnnouncement {
    private SharedPreferenceStore spStore;
    private APIInterface apiInterface;
    private Context context;
    private IError iError;
    private IAnnouncement iAnnouncement;
    public PostAnnouncement(Context context, IError iError, IAnnouncement iAnnouncement){
        this.context = context;
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
        this.iError = iError;
        this.iAnnouncement = iAnnouncement;
    }

    public void execute(String title, String content, String[] tags) throws InvalidFieldException {
        if(title.trim().isEmpty()){
            throw new InvalidFieldException("judul harus diisi");
        }
        if(content.trim().isEmpty()){
            throw  new InvalidFieldException("content harus diisi");
        }
        if(tags.length == 0){
            throw new InvalidFieldException("tags harus diisi");
        }

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<AnnouncementPostResponse> responseCall = this.apiInterface.postAnnouncement(this.spStore.getToken(),
                    new AnnouncementPostRequest(title,content,tags));

            responseCall.enqueue(new Callback<AnnouncementPostResponse>() {
                @Override
                public void onResponse(Call<AnnouncementPostResponse> call, Response<AnnouncementPostResponse> response) {
                    Log.d("msg", response.code()+"");
                    if (response.isSuccessful()) {
                        iAnnouncement.makeAnnouncementSuccess();
                    } else if (response.code() / 100 == 4) {
                        iError.showError("ada kesalahan pengisian data");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<AnnouncementPostResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            iError.showError("tidak ada koneksi internet");
        }
    }
}
