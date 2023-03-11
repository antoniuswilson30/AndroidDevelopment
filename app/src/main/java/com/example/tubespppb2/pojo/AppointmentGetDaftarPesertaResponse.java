package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class AppointmentGetDaftarPesertaResponse {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("attending")
    public boolean attending;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;
}
