package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class LecturerPostTimeSlotsResponse {
    @SerializedName("id")
    public String id;
    @SerializedName("lecturer_id")
    public String lecturer_id;
    @SerializedName("day")
    public String day;
    @SerializedName("start_time")
    public String start_time;
    @SerializedName("end_time")
    public String end_time;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;
    @SerializedName("errcode")
    public String errcode;
}
