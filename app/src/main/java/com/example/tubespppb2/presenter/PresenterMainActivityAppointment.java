package com.example.tubespppb2.presenter;

import android.content.Context;

import com.example.tubespppb2.IError;
import com.example.tubespppb2.exception.InvalidDateException;
import com.example.tubespppb2.exception.InvalidFieldException;
import com.example.tubespppb2.model.appointment.DeleteAppointment;
import com.example.tubespppb2.model.appointment.GetDaftarAppointmentOwnerAndParticipant;
import com.example.tubespppb2.model.appointment.GetDaftarPesertaAppointment;
import com.example.tubespppb2.model.appointment.GetDaftarUndanganAppointment;
import com.example.tubespppb2.model.appointment.GetLecturerByEmail;
import com.example.tubespppb2.model.appointment.GetUserByEmail;
import com.example.tubespppb2.model.appointment.PatchPesertaAppointmentRsvp;
import com.example.tubespppb2.model.appointment.PostAppointment;
import com.example.tubespppb2.model.appointment.PostPesertaAppointment;
import com.example.tubespppb2.model.shared.GetLecturerTimeSlots;
import com.example.tubespppb2.view.appointment.IAppointment;
import com.example.tubespppb2.view.appointment.IAppointmentDetailPeserta;
import com.example.tubespppb2.view.appointment.IAppointmentJadwalDosen;
import com.example.tubespppb2.view.appointment.IUndanganAppointment;

public class PresenterMainActivityAppointment {
    private GetDaftarAppointmentOwnerAndParticipant getDaftarAppointmentOwner;
    private PostAppointment postAppointment;
    private GetUserByEmail getUserByEmail;
    private Context context;
    private IError iError;
    private PostPesertaAppointment postPesertaAppointment;
    private GetLecturerByEmail getLecturerByEmail;
    private GetLecturerTimeSlots getLecturerTimeSlots;
    private GetDaftarUndanganAppointment getDaftarUndanganAppointment;
    private PatchPesertaAppointmentRsvp patchPesertaAppointmentRsvp;
    private GetDaftarPesertaAppointment getDaftarPesertaAppointment;
    private DeleteAppointment deleteAppointment;
    public PresenterMainActivityAppointment(Context context, IError iError, IAppointment iAppointment, IAppointmentJadwalDosen iAppointmentJadwalDosen, IUndanganAppointment iUndanganAppointment, IAppointmentDetailPeserta iAppointmentDetailPeserta){
        this.iError = iError;
        this.context = context;
        this.getDaftarAppointmentOwner = new GetDaftarAppointmentOwnerAndParticipant(context,iError,iAppointment);
        this.postAppointment = new PostAppointment(context,iError,iAppointment);
        this.getUserByEmail = new GetUserByEmail(context,iError,iAppointment);
        this.postPesertaAppointment = new PostPesertaAppointment(context,iError,iAppointment);
        this.getLecturerByEmail = new GetLecturerByEmail(context,iError,iAppointmentJadwalDosen);
        this.getLecturerTimeSlots = new GetLecturerTimeSlots(context,iError,iAppointmentJadwalDosen);
        this.getDaftarUndanganAppointment = new GetDaftarUndanganAppointment(context,iError,iUndanganAppointment);
        this.patchPesertaAppointmentRsvp = new PatchPesertaAppointmentRsvp(context,iError,iUndanganAppointment);
        this.getDaftarPesertaAppointment = new GetDaftarPesertaAppointment(context,iError,iAppointmentDetailPeserta);
        this.deleteAppointment = new DeleteAppointment(context,iError,iAppointment);
    }

    public void loadData(String startDate, String endDate){
        try {
            this.getDaftarAppointmentOwner.execute(startDate,endDate);
        }catch (InvalidDateException e){
            iError.showError(e.getMessage());
        }
    }

    public void addAppointment(String title, String desc, String start_datetime, String end_datetime){
        try {
            this.postAppointment.execute(title,desc,start_datetime,end_datetime);
        } catch (InvalidFieldException e) {
            iError.showError(e.getMessage());
        } catch (InvalidDateException e) {
            iError.showError(e.getMessage());
        }
    }

    public void isEmailExist(String email){
        this.getUserByEmail.execute(email);
    }

    public void getLecturerId(String email){
        this.getLecturerByEmail.execute(email);
    }
    public void addParticipants(String[] idParticipants, String appointmentId ){
        this.postPesertaAppointment.execute(appointmentId,idParticipants);
    }

    public void getLecturerTimeSlots(String lecturerId){
        this.getLecturerTimeSlots.execute(lecturerId,1);
    }

    public void getDaftarUndanganAppointment(){
        this.getDaftarUndanganAppointment.execute();
    }

    public void patchPesertaAppointmentRsvp(String appointmentId, boolean attending){
        this.patchPesertaAppointmentRsvp.execute(appointmentId,attending);
    }

    public void deleteAppointment(String appointmentId){
        this.deleteAppointment.execute(appointmentId);
    }

    public void loadDaftarPesertaDetail(String appointmentId){
        this.getDaftarPesertaAppointment.execute(appointmentId);
    }
}
