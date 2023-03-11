package com.example.tubespppb2.view.announcement;

import com.example.tubespppb2.pojo.AnnouncementGetDaftarResponse;
import com.example.tubespppb2.pojo.AnnouncementGetResponse;
import com.example.tubespppb2.pojo.Data;
import com.example.tubespppb2.pojo.TagGetResponse;

import java.util.List;

public interface IAnnouncement {
    void addData(AnnouncementGetDaftarResponse data);
    void showDetail(AnnouncementGetResponse data);
    void makeAnnouncementSuccess();
}
