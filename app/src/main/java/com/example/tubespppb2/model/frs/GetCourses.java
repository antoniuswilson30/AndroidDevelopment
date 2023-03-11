package com.example.tubespppb2.model.frs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.exception.InvalidFieldException;
import com.example.tubespppb2.pojo.CoursesGetDaftarResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.frsPage.IMatKul;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetCourses {
    private APIInterface apiInterface;
    private SharedPreferenceStore spStore;
    private Context context;
    private IError iError;
    private IMatKul iMatKul;

    public GetCourses(Context context, IError iError, IMatKul iMatKul){
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
        this.iError = iError;
        this.context = context;
        this.iMatKul = iMatKul;
    }

    public void execute(String namaMatkul, int choice) throws InvalidFieldException {
        if(namaMatkul.trim().isEmpty()){
            throw new InvalidFieldException("nama matkul tidak boleh kosong");
        }
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<List<CoursesGetDaftarResponse>> responseCall = this.apiInterface.getCourses(this.spStore.getToken(),namaMatkul);

            responseCall.enqueue(new Callback<List<CoursesGetDaftarResponse>>() {
                @Override
                public void onResponse(Call<List<CoursesGetDaftarResponse>> call, Response<List<CoursesGetDaftarResponse>> response) {
                    if (response.isSuccessful()) {
                        iMatKul.getCourseSuccess(response.body(),choice);
                    } else if (response.code() / 100 == 4) {
                        iError.showError("mata kuliah tidak ada");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<List<CoursesGetDaftarResponse>> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            this.iError.showError("tidak ada koneksi internet");
        }
    }
}
