package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class EnrollmentReason {
    @SerializedName("id")
    public String id;
    @SerializedName("code")
    public String code;
    @SerializedName("name")
    public String name;
    @SerializedName("acquired_score")
    public String acquired_score;
    @SerializedName("required_score")
    public String required_score;
}
