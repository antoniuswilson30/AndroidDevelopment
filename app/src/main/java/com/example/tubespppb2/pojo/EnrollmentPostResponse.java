package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EnrollmentPostResponse {
    @SerializedName("student_id")
    public String student_id;
    @SerializedName("course_id")
    public String course_id;
    @SerializedName("academic_year")
    public int academic_year;
    @SerializedName("score")
    public String score;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("errcode")
    public String errcode;
    @SerializedName("reason")
    public List<EnrollmentReason> reason;
}
