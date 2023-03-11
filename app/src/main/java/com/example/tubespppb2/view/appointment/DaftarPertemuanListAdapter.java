package com.example.tubespppb2.view.appointment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.fragment.app.FragmentManager;

import com.example.tubespppb2.databinding.ItemListAppointmentBinding;
import com.example.tubespppb2.pojo.AppointmentGetDaftarOwnerAndParticipantResponse;
import com.example.tubespppb2.pojo.AppointmentGetDaftarPesertaResponse;
import com.example.tubespppb2.presenter.PresenterMainActivityAppointment;
import com.example.tubespppb2.service.SharedPreferenceStore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DaftarPertemuanListAdapter extends BaseAdapter implements IAppointmentDetailPeserta {
    private List<AppointmentGetDaftarOwnerAndParticipantResponse> list;
    private List<AppointmentGetDaftarOwnerAndParticipantResponse> listAll;
    private Activity activity;
    private SharedPreferenceStore spStore;
    private String myId;
    private boolean isFilterActive;
    private PresenterMainActivityAppointment presenter;
    private DetailAppointmentDialogFragment detailAppointmentDialogFragment;
    private FragmentManager fm;
    public DaftarPertemuanListAdapter(Activity activity, PresenterMainActivityAppointment presenter, FragmentManager fm){
        this.list = new ArrayList<>();
        this.listAll = new ArrayList<>();
        this.activity = activity;
        this.spStore = new SharedPreferenceStore(activity.getApplicationContext());
        this.myId = this.spStore.getUserId();
        this.isFilterActive = false;
        this.presenter = presenter;
        this.fm = fm;
    }

    public void filterByMe(boolean isByMe){
        if(isByMe == true){
            this.isFilterActive = true;
            list.clear();
            for(int i = 0 ; i < listAll.size(); i++) {
                if(listAll.get(i).organizer_id.equals(myId)){
                    list.add(listAll.get(i));
                }
            }
        }else{
            this.isFilterActive = false;
            list.clear();
            list.addAll(listAll);
        }
        notifyDataSetChanged();
    }

    public void loadData(List<AppointmentGetDaftarOwnerAndParticipantResponse> data){
        this.list.clear();
        this.listAll.clear();
        this.listAll.addAll(data);
        this.list.addAll(data);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemListAppointmentBinding binding = ItemListAppointmentBinding.inflate(activity.getLayoutInflater());
        ViewHolder viewHolder = new ViewHolder(binding, position);
        View itemView = binding.getRoot();
        viewHolder.updateView();
        return itemView;
    }

    @Override
    public void loadDaftarPesertaDetail(List<AppointmentGetDaftarPesertaResponse> data) {
        this.detailAppointmentDialogFragment.loadDaftarPesertaDetail(data);
    }

    private class ViewHolder implements View.OnClickListener{
        private int idx;
        private ItemListAppointmentBinding binding;

        public ViewHolder(ItemListAppointmentBinding binding, int position){
            this.binding = binding;
            this.idx = position;
            this.binding.btnDelete.setOnClickListener(this::onClick);
            if(isFilterActive) {
                this.binding.appointmentContainer.setOnClickListener(this::onClick);
            }
        }

        private void updateView(){
            DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mmZ");
            DateFormat outputFormatterDate = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat outputFormatterTime = new SimpleDateFormat("HH:mm");
            Date dateInputStart = null;
            Date dateInputEnd = null;
            try {
                dateInputStart = inputFormatter.parse(list.get(idx).start_datetime);
                dateInputEnd = inputFormatter.parse(list.get(idx).end_datetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String startDate = outputFormatterDate.format(dateInputStart);
            String startTime = outputFormatterTime.format(dateInputStart);
            String endDate = outputFormatterDate.format(dateInputEnd);
            String endTime = outputFormatterTime.format(dateInputEnd);

            String title = list.get(idx).title;
            if(title.length() >= 25){
                title = title.substring(0,26);
                title += "...";
            }
            this.binding.tvTitle.setText(title);
            this.binding.tvDescription.setText(list.get(idx).description);
            this.binding.tvOrganizer.setText(list.get(idx).organizer_name);
            this.binding.tvStartDate.setText(startDate);
            this.binding.tvEndDate.setText(endDate);
            this.binding.tvStartTime.setText(startTime);
            this.binding.tvEndTime.setText(endTime);
            if(!isFilterActive){
                this.binding.btnDelete.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == this.binding.btnDelete.getId()){
                presenter.deleteAppointment(list.get(idx).id);
            }else if(v.getId() == this.binding.appointmentContainer.getId()){
                DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mmZ");
                DateFormat outputFormatterDate = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat outputFormatterTime = new SimpleDateFormat("HH:mm");
                Date dateInputStart = null;
                Date dateInputEnd = null;
                try {
                    dateInputStart = inputFormatter.parse(list.get(idx).start_datetime);
                    dateInputEnd = inputFormatter.parse(list.get(idx).end_datetime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String startDate = outputFormatterDate.format(dateInputStart);
                String startTime = outputFormatterTime.format(dateInputStart);
                String endDate = outputFormatterDate.format(dateInputEnd);
                String endTime = outputFormatterTime.format(dateInputEnd);
                Bundle bundle = new Bundle();
                bundle.putString("appointmentId",list.get(idx).id);
                bundle.putString("judul",list.get(idx).title);
                bundle.putString("desc",list.get(idx).description);
                bundle.putString("organizedBy",list.get(idx).organizer_name);
                bundle.putString("startDate",startDate);
                bundle.putString("endDate",endDate);
                bundle.putString("startTime",startTime);
                bundle.putString("endTime",endTime);
                detailAppointmentDialogFragment = DetailAppointmentDialogFragment.newInstance(presenter,bundle);
                detailAppointmentDialogFragment.show(fm,"dialog");
            }
        }
    }
}
