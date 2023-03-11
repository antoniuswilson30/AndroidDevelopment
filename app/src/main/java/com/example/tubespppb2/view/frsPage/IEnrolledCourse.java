package com.example.tubespppb2.view.frsPage;

import com.example.tubespppb2.pojo.EnrolledCourseGetResponse;

import java.util.List;

public interface IEnrolledCourse {
    void loadEnrolment(List<EnrolledCourseGetResponse> data);
    void deleteEnrolmentSuccess();
}
