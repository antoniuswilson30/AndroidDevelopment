package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class AppointmentDeleteResponse {
    @SerializedName("id")
    public String id;
    @SerializedName("title")
    public String title;
    @SerializedName("description")
    public String description;
    @SerializedName("start_datetime")
    public String start_datetime;
    @SerializedName("end_datetime")
    public String end_datetime;
    @SerializedName("organizer_id")
    public String organizer_id;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;
}
