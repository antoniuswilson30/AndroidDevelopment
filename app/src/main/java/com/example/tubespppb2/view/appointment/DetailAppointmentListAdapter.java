package com.example.tubespppb2.view.appointment;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tubespppb2.databinding.ItemListDetailAppointmentBinding;
import com.example.tubespppb2.pojo.AppointmentGetDaftarPesertaResponse;

import java.util.ArrayList;
import java.util.List;

public class DetailAppointmentListAdapter extends BaseAdapter {
    private List<AppointmentGetDaftarPesertaResponse> list;
    private Activity activity;
    public DetailAppointmentListAdapter(Activity activity){
        this.list = new ArrayList<>();
        this.activity = activity;
    }

    public void loadDataPeserta(List<AppointmentGetDaftarPesertaResponse> data){
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
        ItemListDetailAppointmentBinding binding = ItemListDetailAppointmentBinding.inflate(activity.getLayoutInflater());
        View view = binding.getRoot();
        ViewHolder viewHolder = new ViewHolder(binding,position);
        viewHolder.updateView();
        return view;
    }

    private class ViewHolder{
        private int idx;
        private ItemListDetailAppointmentBinding binding;

        public ViewHolder(ItemListDetailAppointmentBinding binding, int position){
            this.binding = binding;
            this.idx = position;
        }

        private void updateView(){
           this.binding.tvName.setText(list.get(idx).name);
           if(list.get(idx).attending == true) {
               this.binding.tvStatus.setText("YES");
           }else{
               this.binding.tvStatus.setText("NO");
           }
        }
    }
}
