package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class AnnouncementPostResponse {
    @SerializedName("id")
    public String id;
    @SerializedName("title")
    public String title;
    @SerializedName("content")
    public String content;
    @SerializedName("author_id")
    public String author_id;
    @SerializedName("creadted_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;
    @SerializedName("tags")
    public TagsAnnouncementPost[] tags;
    @SerializedName("errcode")
    public String errcode;
}
