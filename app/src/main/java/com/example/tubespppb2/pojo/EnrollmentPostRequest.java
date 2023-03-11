package com.example.tubespppb2.pojo;

public class EnrollmentPostRequest {
    public String course_id;
    public int academic_year;

    public EnrollmentPostRequest(String course_id, int academic_year){
        this.course_id = course_id;
        this.academic_year = academic_year;
    }
}
