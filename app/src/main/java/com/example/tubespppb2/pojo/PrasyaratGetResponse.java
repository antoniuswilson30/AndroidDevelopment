package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class PrasyaratGetResponse {
    @SerializedName("course_id")
    public String course_id;
    @SerializedName("prereq_id")
    public String prereq_id;
    @SerializedName("score")
    public String score;
    @SerializedName("prerequisite_code")
    public String prerequisite_code;
    @SerializedName("prerequisite_name")
    public String prerequisite_name;
    @SerializedName("prerequisite_semester")
    public String prerequisite_semester;
    @SerializedName("prerequisite_archived_at")
    public String prerequisite_archived_at;

    public PrasyaratGetResponse(String code, String name, String score){
        this.prerequisite_code = code;
        this.prerequisite_name = name;
        this.score = score;
    }

}
