package com.example.tubespppb2.model.frs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.pojo.EnrolledCourseGetResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.frsPage.IEnrolledCourse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetEnrollment {
    private APIInterface apiInterface;
    private SharedPreferenceStore spStore;
    private IError iError;
    private Context context;
    private IEnrolledCourse iEnrolledCourse;

    public GetEnrollment(Context context, IError iError, IEnrolledCourse iEnrolledCourse) {
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
        this.iError = iError;
        this.context = context;
        this.iEnrolledCourse = iEnrolledCourse;
    }

    public void execute(String academic_year){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<List<EnrolledCourseGetResponse> > responseCall = this.apiInterface.getEnrolledCourse(this.spStore.getToken(),academic_year);
            responseCall.enqueue(new Callback<List<EnrolledCourseGetResponse>>() {
                @Override
                public void onResponse(Call<List<EnrolledCourseGetResponse>> call, Response<List<EnrolledCourseGetResponse>> response) {
                    if (response.isSuccessful()) {
                        iEnrolledCourse.loadEnrolment(response.body());
                    } else if (response.code() / 100 == 4) {
                        iError.showError("akun tidak ada");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<List<EnrolledCourseGetResponse>> call, Throwable t) {
                    call.cancel();
                }
            });
        }else{
            this.iError.showError("tidak ada koneksi internet");
        }
    }


}
