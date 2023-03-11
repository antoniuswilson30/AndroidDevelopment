package com.example.tubespppb2.pojo;

public class LecturerPostTimeSlotsRequest {
    public String day;
    public String start_time;
    public String end_time;

    public LecturerPostTimeSlotsRequest(String day, String start_time, String end_time){
        this.day = day;
         this.start_time = start_time;
         this.end_time = end_time;
    }
}
