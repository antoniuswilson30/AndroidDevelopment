package com.example.tubespppb2.pojo;

import com.google.gson.annotations.SerializedName;

public class AcademicYearGetResponse {
    @SerializedName("active_year")
    public String active_year;
    @SerializedName("academic_years")
    public String [] academic_years;
}
