package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class UsersGetSelfResponse {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("roles")
    public String []roles;
    @SerializedName("archived_at")
    public String archived_at;
}
