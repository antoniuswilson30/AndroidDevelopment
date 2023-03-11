package com.example.tubespppb2.model.frs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.example.tubespppb2.APIClient;
import com.example.tubespppb2.APIInterface;
import com.example.tubespppb2.IError;
import com.example.tubespppb2.pojo.EnrollmentPostRequest;
import com.example.tubespppb2.pojo.EnrollmentPostResponse;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.frsPage.IMatKul;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class PostEnrollment {
    private APIInterface apiInterface;
    private SharedPreferenceStore spStore;
    private IError iError;
    private Context context;
    private IMatKul iMatKul;

    public PostEnrollment(Context context, IError iError, IMatKul iMatKul) {
        this.context = context;
        this.iError = iError;
        this.iMatKul = iMatKul;
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
        this.spStore = new SharedPreferenceStore(context);
    }

    public void execute(String courseId, int academic_year, String nama) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            Call<EnrollmentPostResponse> responseCall = this.apiInterface.postEnrollment(this.spStore.getToken(),
                    new EnrollmentPostRequest(courseId,academic_year));
            responseCall.enqueue(new Callback<EnrollmentPostResponse>() {
                @Override
                public void onResponse(Call<EnrollmentPostResponse> call, Response<EnrollmentPostResponse> response) {
                    if (response.isSuccessful()) {
                        iMatKul.addEnrollment(response.body(), nama);
                    } else if (response.code() / 100 == 4) {
                        Converter<ResponseBody, EnrollmentPostResponse> converter =
                                APIClient.getClient().responseBodyConverter(EnrollmentPostResponse.class, new Annotation[0]);
                        EnrollmentPostResponse enrollmentPostResponse = null;
                        try {
                            enrollmentPostResponse = converter.convert(response.errorBody());
                            String errMsg = enrollmentPostResponse.errcode;
                            if (errMsg.equals("E_INVALID_SETTINGS")) {
                                iError.showError("server error");
                            } else if (errMsg.equals("E_LOCKED")) {
                                iError.showError("Sudah tidak bisa menambahkan mata kuliah");
                            } else if (errMsg.equals("E_EXIST")) {
                                iError.showError("Mata kuliah " + nama + " sudah pernah diambil");
                            } else if (errMsg.equals("E_UNSATISFIED_PREREQUISITE")) {
                                iMatKul.showPrasyaratGagal(enrollmentPostResponse.reason);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() / 100 == 5) {
                        iError.showError("server error");
                    }
                }

                @Override
                public void onFailure(Call<EnrollmentPostResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            this.iError.showError("tidak ada koneksi internet");
        }
    }
}
