package com.example.tubespppb2.view.frsPage;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tubespppb2.databinding.ItemListAmbilfrsBinding;
import com.example.tubespppb2.pojo.EnrollmentReason;
import com.example.tubespppb2.pojo.PrasyaratGetResponse;

import java.util.ArrayList;
import java.util.List;

public class PrasyaratAdapter extends BaseAdapter {
    private List<PrasyaratGetResponse> data;
    private Activity activity;
    private boolean isGagal;
    public PrasyaratAdapter(Activity activity){
        this.activity = activity;
        this.data = new ArrayList<>();
        this.isGagal = false;
    }

    public void clearData(){
        isGagal = false;
        this.data.clear();
        notifyDataSetChanged();
    }
    public void loadData(List<PrasyaratGetResponse> data, boolean isGagal){
        this.data.clear();
        this.data.addAll(data);
        this.isGagal = isGagal;
        notifyDataSetChanged();
    }

    public void loadDataErr(List<EnrollmentReason> dataErr, boolean isGagal){
        this.data.clear();
        this.isGagal = isGagal;
        for(int i = 0; i < dataErr.size();i++){
            PrasyaratGetResponse temp = new PrasyaratGetResponse(dataErr.get(i).code,dataErr.get(i).name, dataErr.get(i).required_score);
            this.data.add(temp);
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public Object getItem(int i) {
        return this.data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ItemListAmbilfrsBinding binding = ItemListAmbilfrsBinding.inflate(activity.getLayoutInflater());
        PrasyaratAdapter.ViewHolder viewHolder = new PrasyaratAdapter.ViewHolder(binding,i);
        View itemView = binding.getRoot();
        viewHolder.updateView();
        return itemView;
    }

    private class ViewHolder{
        private ItemListAmbilfrsBinding binding;
        private int idx;

        public ViewHolder(ItemListAmbilfrsBinding binding, int position){
            this.binding = binding;
            this.idx = position;
            if(!isGagal){
                this.binding.containerTidakTerpenuhi.setVisibility(View.GONE);
            }
        }

        public void updateView(){
            this.binding.tvNamaMatkul.setText(data.get(idx).prerequisite_name);
            this.binding.tvKode.setText(data.get(idx).prerequisite_code);
            this.binding.tvNilai.setText(data.get(idx).score);
        }
    }
}
