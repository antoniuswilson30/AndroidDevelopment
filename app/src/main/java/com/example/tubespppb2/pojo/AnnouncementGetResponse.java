package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class AnnouncementGetResponse {
    @SerializedName("id")
    public String id;
    @SerializedName("title")
    public String title;
    @SerializedName("content")
    public String content;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;
    @SerializedName("author")
    public Author author;
    @SerializedName("tags")
    public Tags[] tags;
}
