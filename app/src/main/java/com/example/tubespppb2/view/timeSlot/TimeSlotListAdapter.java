package com.example.tubespppb2.view.timeSlot;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tubespppb2.databinding.ItemListJadwalDosenBinding;
import com.example.tubespppb2.pojo.LecturerGetTimeSlotsResponse;
import com.example.tubespppb2.presenter.PresenterMainActivityTimeSlot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeSlotListAdapter extends BaseAdapter {
    private List<LecturerGetTimeSlotsResponse> list;
    private List<LecturerGetTimeSlotsResponse> listAll;
    private Activity activity;
    private PresenterMainActivityTimeSlot presenter;
    public TimeSlotListAdapter(Activity activity, PresenterMainActivityTimeSlot presenter){
        this.list = new ArrayList<>();
        this.listAll = new ArrayList<>();
        this.activity = activity;
        this.presenter = presenter;
    }

    public void filter(String filter){
        if(!filter.trim().isEmpty()) {
            this.list.clear();
            for (int i = 0; i < listAll.size(); i++) {
                if (listAll.get(i).day.equals(filter)) {
                    this.list.add(listAll.get(i));
                }
            }
        }else{
            this.list.clear();
            this.list.addAll(listAll);
        }
        notifyDataSetChanged();
    }
    public void addData(List<LecturerGetTimeSlotsResponse> data){
        Log.d("msg","hahaha");
        this.listAll.clear();
        this.list.clear();
        this.list.addAll(data);
        this.listAll.addAll(data);
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
        ItemListJadwalDosenBinding binding = ItemListJadwalDosenBinding.inflate(this.activity.getLayoutInflater());
        ViewHolder viewHolder = new ViewHolder(binding,position);
        View view = binding.getRoot();
        viewHolder.updateView();
        return view;
    }

    private class ViewHolder implements View.OnClickListener{
        private int idx;
        private ItemListJadwalDosenBinding binding;

        public ViewHolder(ItemListJadwalDosenBinding binding, int position){
            this.binding = binding;
            this.idx = position;
        }

        private void updateView(){
            DateFormat inputFormatter = new SimpleDateFormat("HH:mmZ");
            DateFormat outputFormatterTime = new SimpleDateFormat("HH:mm");
            Date dateInputStart = null;
            Date dateInputEnd = null;
            try {
                dateInputStart = inputFormatter.parse(list.get(idx).start_time);
                dateInputEnd = inputFormatter.parse(list.get(idx).end_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String startTime = outputFormatterTime.format(dateInputStart);
            String endTime = outputFormatterTime.format(dateInputEnd);
            this.binding.tvHari.setText(list.get(idx).day);
            this.binding.tvStartTime.setText(startTime);
            this.binding.tvEndTime.setText(endTime);
            this.binding.btnDelete.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == this.binding.btnDelete.getId()){
                presenter.deleteTimeSlot(list.get(idx).id);
            }
        }
    }
}
