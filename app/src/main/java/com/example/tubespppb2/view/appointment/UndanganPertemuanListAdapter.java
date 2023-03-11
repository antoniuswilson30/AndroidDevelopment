package com.example.tubespppb2.view.appointment;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tubespppb2.R;
import com.example.tubespppb2.databinding.ItemListUndanganPertemuanBinding;
import com.example.tubespppb2.pojo.AppointmentGetDaftarUndanganResponse;
import com.example.tubespppb2.presenter.PresenterMainActivityAppointment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UndanganPertemuanListAdapter extends BaseAdapter {
    private Activity activity;
    private List<AppointmentGetDaftarUndanganResponse> list;
    private PresenterMainActivityAppointment presenter;
    public UndanganPertemuanListAdapter(Activity activity, PresenterMainActivityAppointment presenter){
        this.list = new ArrayList<>();
        this.activity = activity;
        this.presenter = presenter;
    }

    public void loadData(List<AppointmentGetDaftarUndanganResponse> data){
        this.list.clear();
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
        ItemListUndanganPertemuanBinding binding = ItemListUndanganPertemuanBinding.inflate(activity.getLayoutInflater());
        View view = binding.getRoot();
        ViewHolder viewHolder = new ViewHolder(binding,position);
        viewHolder.updateView();
        return view;
    }

    private class ViewHolder implements View.OnClickListener{
        private int idx;
        private ItemListUndanganPertemuanBinding binding;

        public ViewHolder(ItemListUndanganPertemuanBinding binding, int position){
            this.binding = binding;
            this.idx = position;
            binding.btnAccept.setOnClickListener(this::onClick);
            binding.btnTolak.setOnClickListener(this::onClick);
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
            }
            this.binding.tvTitle.setText(title);
            this.binding.tvDescription.setText(list.get(idx).description);
            this.binding.tvOrganizer.setText(list.get(idx).organizer_name);
            this.binding.tvStartDate.setText(startDate);
            this.binding.tvEndDate.setText(endDate);
            this.binding.tvStartTime.setText(startTime);
            this.binding.tvEndTime.setText(endTime);
            if(list.get(idx).attending == true){
                this.binding.tvStatus.setText(R.string.yes);
            }else{
                this.binding.tvStatus.setText(R.string.no);
            }
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == this.binding.btnAccept.getId()){
                presenter.patchPesertaAppointmentRsvp(list.get(idx).appointment_id,true);
            }else{
                presenter.patchPesertaAppointmentRsvp(list.get(idx).appointment_id,false);
            }
        }
    }
}
