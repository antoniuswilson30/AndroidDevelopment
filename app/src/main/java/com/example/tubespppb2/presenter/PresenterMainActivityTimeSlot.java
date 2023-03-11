package com.example.tubespppb2.presenter;

import android.content.Context;

import com.example.tubespppb2.IError;
import com.example.tubespppb2.exception.InvalidDateException;
import com.example.tubespppb2.exception.InvalidFieldException;
import com.example.tubespppb2.model.shared.GetLecturerTimeSlots;
import com.example.tubespppb2.model.timeSlots.DeleteLecturerTimeSlot;
import com.example.tubespppb2.model.timeSlots.PostLecturerTimeSlot;
import com.example.tubespppb2.view.timeSlot.ITimeSlot;

public class PresenterMainActivityTimeSlot {
    private Context context;
    private IError iError;
    private ITimeSlot iTimeSlot;
    private GetLecturerTimeSlots getLecturerTimeSlots;
    private DeleteLecturerTimeSlot deleteLecturerTimeSlot;
    private PostLecturerTimeSlot postLecturerTimeSlot;
    public PresenterMainActivityTimeSlot(Context context, IError iError, ITimeSlot iTimeSlot){
        this.context = context;
        this.iError = iError;
        this.iTimeSlot = iTimeSlot;
        this.getLecturerTimeSlots = new GetLecturerTimeSlots(context,iError,iTimeSlot);
        this.deleteLecturerTimeSlot = new DeleteLecturerTimeSlot(context,iError,iTimeSlot);
        this.postLecturerTimeSlot = new PostLecturerTimeSlot(context,iError,iTimeSlot);
    }

    public void loadLecturerTimeSlot(){
        this.getLecturerTimeSlots.execute(null,2);
    }

    public void deleteTimeSlot(String timeSlotId){
        this.deleteLecturerTimeSlot.execute(timeSlotId);
    }

    public void postLecturerTimeSlot(String day, String start_time, String end_time){
        try {
            this.postLecturerTimeSlot.execute(day,start_time,end_time);
        } catch (InvalidDateException e) {
            iError.showError(e.getMessage());
        } catch (InvalidFieldException e) {
            iError.showError(e.getMessage());
        }
    }
}
