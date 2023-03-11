package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class AnnouncementPostRequest {
    @SerializedName("title")
    public String title;
    @SerializedName("content")
    public String content;
    @SerializedName("tags")
    public String [] tags;

    public AnnouncementPostRequest(String title, String content, String[] tags){
        this.title = title;
        this.content = content;
        this.tags = tags;
    }
}
