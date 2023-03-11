package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class AppointmentGetDaftarUndanganResponse {
    @SerializedName("appointment_id")
    public String appointment_id;
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
    @SerializedName("organizer_name")
    public String organizer_name;
    @SerializedName("attending")
    public boolean attending;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;
}
