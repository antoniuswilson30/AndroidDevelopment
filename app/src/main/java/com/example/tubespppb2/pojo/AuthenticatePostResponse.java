package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class AuthenticatePostResponse {
    @SerializedName("token")
    public String token;
    @SerializedName("errcode")
    public String errcode;
}
