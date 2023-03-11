package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class AppointmentPostRequest {
    @SerializedName("title")
    public String title;
    @SerializedName("description")
    public String description;
    @SerializedName("start_datetime")
    public String start_datetime;
    @SerializedName("end_datetime")
    public String end_datetime;

    public AppointmentPostRequest(String title, String description, String start_datetime, String end_datetime){
        this.title = title;
        this.description = description;
        this.start_datetime = start_datetime;
        this.end_datetime = end_datetime;
    }
}
