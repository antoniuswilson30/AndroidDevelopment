package com.example.tubespppb2.model.frs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.pojo.AcademicYearGetResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.frsPage.IYear;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetActiveAcademicYears {
    private APIInterface apiInterface;
    private SharedPreferenceStore spStore;
    private IError iError;
    private Context context;
    private IYear iYear;

    public GetActiveAcademicYears(Context context, IError iError, IYear iYear) {
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
        this.iError = iError;
        this.context = context;
        this.iYear = iYear;
    }

    public void execute() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<AcademicYearGetResponse> responseCall = this.apiInterface.getActiveAcademicYear(this.spStore.getToken());
            responseCall.enqueue(new Callback<AcademicYearGetResponse>() {
                @Override
                public void onResponse(Call<AcademicYearGetResponse> call, Response<AcademicYearGetResponse> response) {
                    if (response.isSuccessful()) {
                        iYear.loadActiveYear(response.body().active_year);
                    } else if (response.code() / 100 == 4) {
                        iError.showError("server error");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<AcademicYearGetResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            this.iError.showError("tidak ada koneksi internet");
        }

    }
}
