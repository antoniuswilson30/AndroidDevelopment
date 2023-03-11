package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class StudentByIdGetResponse {
    @SerializedName("user_id")
    public String user_id;
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("npm")
    public String npm;
    @SerializedName("initial_year")
    public String initial_year;
}
