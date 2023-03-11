package com.example.tubespppb2.model.frs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.pojo.PrasyaratGetResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.frsPage.IMatKul;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetPrasyaratMatkul {
    private APIInterface apiInterface;
    private SharedPreferenceStore spStore;
    private IError iError;
    private Context context;
    private IMatKul iMatKul;

    public GetPrasyaratMatkul(Context context, IError iError, IMatKul iMatKul) {
        this.context = context;
        this.iError = iError;
        this.iMatKul = iMatKul;
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
    }

    public void execute(String courseId, int choice) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<List<PrasyaratGetResponse>> responseCall = this.apiInterface.getPrasyaratMatkul(this.spStore.getToken(), courseId);
            responseCall.enqueue(new Callback<List<PrasyaratGetResponse>>() {
                @Override
                public void onResponse(Call<List<PrasyaratGetResponse>> call, Response<List<PrasyaratGetResponse>> response) {
                    if (response.isSuccessful()) {
                        iMatKul.loadPrasyarat(response.body(),choice);
                    } else if (response.code() / 100 == 4) {
                        iError.showError("mata kuliah tidak ada");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<List<PrasyaratGetResponse>> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            this.iError.showError("tidak ada koneksi internet");
        }
    }
}
