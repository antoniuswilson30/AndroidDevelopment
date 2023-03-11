package com.example.tubespppb2.view.timeSlot;

import com.example.tubespppb2.pojo.LecturerGetTimeSlotsResponse;

import java.util.List;

public interface ITimeSlot {
    void loadTimeSlots(List<LecturerGetTimeSlotsResponse> data);
    void deleteTimeSlotSuccess();
    void postTimeSlotSuccess();
}
