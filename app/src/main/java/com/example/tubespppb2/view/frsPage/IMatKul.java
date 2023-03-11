package com.example.tubespppb2.view.frsPage;

import com.example.tubespppb2.pojo.CoursesGetDaftarResponse;
import com.example.tubespppb2.pojo.EnrollmentPostResponse;
import com.example.tubespppb2.pojo.EnrollmentReason;
import com.example.tubespppb2.pojo.PrasyaratGetResponse;

import java.util.List;

public interface IMatKul {
    void loadPrasyarat(List<PrasyaratGetResponse> data, int choice);
    void addEnrollment(EnrollmentPostResponse data, String nama);
    void showPrasyaratGagal(List<EnrollmentReason> dataErr);
    void getCourseSuccess(List<CoursesGetDaftarResponse> data, int choice);
}
