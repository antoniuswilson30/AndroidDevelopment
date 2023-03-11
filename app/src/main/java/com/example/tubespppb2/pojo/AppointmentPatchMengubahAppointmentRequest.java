package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class AppointmentPatchMengubahAppointmentRequest {
    @SerializedName("attending")
    public boolean attending;

    public AppointmentPatchMengubahAppointmentRequest(boolean attending){
        this.attending = attending;
    }
}
