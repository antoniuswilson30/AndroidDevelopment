package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class AnnouncementGetDaftarResponse {
    @SerializedName("metadata")
    public Metadata metadata;
    @SerializedName("data")
    public Data[] data;
}
