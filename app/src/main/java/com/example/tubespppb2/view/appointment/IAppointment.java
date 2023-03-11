package com.example.tubespppb2.view.appointment;

import com.example.tubespppb2.pojo.AppointmentGetDaftarOwnerAndParticipantResponse;
import com.example.tubespppb2.pojo.UsersGetByEmailResponse;

import java.util.List;

public interface IAppointment {
    void addData(List<AppointmentGetDaftarOwnerAndParticipantResponse> data);
    void addAppointmentSuccess(String appointmentId);
    void emailExist(UsersGetByEmailResponse data);
    void addParticipantsSuccess();
    void deleteAppointmentSuccess();
}
