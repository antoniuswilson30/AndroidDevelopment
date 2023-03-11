package com.example.tubespppb2.presenter;

import android.content.Context;

import com.example.tubespppb2.IError;
import com.example.tubespppb2.exception.InvalidFieldException;
import com.example.tubespppb2.model.announcement.GetAnnouncementDetail;
import com.example.tubespppb2.model.announcement.GetDaftarAnnouncement;
import com.example.tubespppb2.model.announcement.GetTag;
import com.example.tubespppb2.model.announcement.PostAnnouncement;
import com.example.tubespppb2.view.announcement.IAnnouncement;
import com.example.tubespppb2.view.announcement.ITag;

import java.util.List;

public class PresenterMainActivityAnnouncement {
    private Context context;
    private IError iError;
    private IAnnouncement iAnnouncement;
    private ITag iTag;
    private GetAnnouncementDetail announcementDetail;
    private GetDaftarAnnouncement daftarAnnouncement;
    private PostAnnouncement postAnnouncement;
    private GetTag getTag;
    public PresenterMainActivityAnnouncement(Context context, IError iError, IAnnouncement iAnnouncement, ITag iTag){
        this.context = context;
        this.iError = iError;
        this.iTag = iTag;
        this.iAnnouncement = iAnnouncement;
        this.announcementDetail = new GetAnnouncementDetail(context,iError,iAnnouncement);
        this.daftarAnnouncement = new GetDaftarAnnouncement(context,iError,iAnnouncement);
        this.postAnnouncement = new PostAnnouncement(context,iError,iAnnouncement);
        this.getTag = new GetTag(context,iError,iTag);
    }

    public void loadData(String title, List<String> tags, String cursor, String limit){
        this.daftarAnnouncement.execute(title,tags,cursor,limit);
    }

    public void loadDetail(String id){
        this.announcementDetail.execute(id);
    }

    public void loadTag(){
        this.getTag.execute();
    }

    public void makeAnnouncement(String title, String content, String[] tags){
        try {
            this.postAnnouncement.execute(title, content, tags);
        }catch (InvalidFieldException e){
            iError.showError(e.getMessage());
        }
    }

}
