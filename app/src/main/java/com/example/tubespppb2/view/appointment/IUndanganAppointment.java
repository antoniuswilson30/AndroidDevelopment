package com.example.tubespppb2.view.appointment;

import com.example.tubespppb2.pojo.AppointmentGetDaftarUndanganResponse;
import com.example.tubespppb2.pojo.AppointmentPatchMengubahAppointmentResponse;

import java.util.List;

public interface IUndanganAppointment {
    void getDaftarUndanganSuccess(List<AppointmentGetDaftarUndanganResponse> data);
    void patchAppointmentRSVPSuccess();
}
