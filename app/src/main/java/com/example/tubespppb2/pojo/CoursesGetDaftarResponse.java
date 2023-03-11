package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class CoursesGetDaftarResponse {
    @SerializedName("id")
    public String id;
    @SerializedName("code")
    public String code;
    @SerializedName("name")
    public String name;
    @SerializedName("semester")
    public String semester;
    @SerializedName("archived_at")
    public String archived_at;
}
