package com.example.tubespppb2.model.frs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.pojo.EnrollmentDeleteResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.frsPage.IEnrolledCourse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteEnrolment {
    private APIInterface apiInterface;
    private SharedPreferenceStore spStore;
    private IError iError;
    private Context context;
    private IEnrolledCourse iEnrolledCourse;

    public DeleteEnrolment(Context context, IError iError, IEnrolledCourse iEnrolledCourse) {
        this.context = context;
        this.iError = iError;
        this.iEnrolledCourse = iEnrolledCourse;
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
    }

    public void execute(String courseId, int academic_years) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<EnrollmentDeleteResponse> responseCall = this.apiInterface.deleteEnrollment(this.spStore.getToken(), courseId, academic_years);
            responseCall.enqueue(new Callback<EnrollmentDeleteResponse>() {
                @Override
                public void onResponse(Call<EnrollmentDeleteResponse> call, Response<EnrollmentDeleteResponse> response) {
                    if (response.isSuccessful()) {
                        iEnrolledCourse.deleteEnrolmentSuccess();
                    } else if (response.code() / 100 == 4) {
                        iError.showError("tidak dapat menghapus mata kuliah");
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<EnrollmentDeleteResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            this.iError.showError("tidak ada koneksi internet");
        }
    }
}
