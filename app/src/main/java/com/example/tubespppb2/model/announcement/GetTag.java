package com.example.tubespppb2.model.announcement;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.util.Log;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.pojo.TagGetResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.announcement.ITag;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetTag {
    private APIInterface apiInterface;
    private SharedPreferenceStore spStore;
    private Context context;
    private IError iError;
    private ITag iTag;

    public GetTag(Context context, IError iError, ITag iTag) {
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
        this.context = context;
        this.iTag = iTag;
        this.iError = iError;
    }

    public void execute() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<List<TagGetResponse>> responseCall = this.apiInterface.getTag(this.spStore.getToken());
            responseCall.enqueue(new Callback<List<TagGetResponse>>() {
                @Override
                public void onResponse(Call<List<TagGetResponse>> call, Response<List<TagGetResponse>> response) {
                    Log.d("msg", response.code() + "");
                    if (response.isSuccessful()) {
                        iTag.loadTag(response.body());
                    } else if (response.code() / 100 == 4) {
                        iError.showError("ada kesalahan pengisian data");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<List<TagGetResponse>> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            iError.showError("tidak ada koneksi internet");
        }
    }
}
