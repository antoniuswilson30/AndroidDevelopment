package com.example.tubespppb2.view.appointment;

import com.example.tubespppb2.pojo.LecturerGetTimeSlotsResponse;
import com.example.tubespppb2.pojo.UsersGetByEmailResponse;

import java.util.List;

public interface IAppointmentJadwalDosen {
    void getLecturerId(UsersGetByEmailResponse data);
    void loadLecturerTimeSlots(List<LecturerGetTimeSlotsResponse> data);
}
