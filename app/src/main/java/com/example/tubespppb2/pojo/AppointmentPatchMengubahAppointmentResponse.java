package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class AppointmentPatchMengubahAppointmentResponse {
    @SerializedName("appointment_id")
    public String appointment_id;
    @SerializedName("participant_id")
    public String participant_id;
    @SerializedName("attending")
    public boolean attending;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;
    @SerializedName("errcode")
    public String errcode;
}