package com.example.tubespppb2.view.timeSlot;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tubespppb2.databinding.FragmentTimeSlotBinding;
import com.example.tubespppb2.pojo.LecturerGetTimeSlotsResponse;
import com.example.tubespppb2.presenter.PresenterMainActivityTimeSlot;

import java.util.List;

public class TimeSlotFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, ITimeSlot {
    private FragmentTimeSlotBinding binding;
    private PresenterMainActivityTimeSlot presenter;
    private TimeSlotListAdapter adapter;
    private Activity activity;
    private TambahSlotKosongDosenDialogFragment fragment;
    private FragmentManager fm;
    private TimeSlotFragment(){}

    public static TimeSlotFragment newInstance(PresenterMainActivityTimeSlot presenter,Activity activity, FragmentManager fm) {
        TimeSlotFragment fragment = new TimeSlotFragment();
        fragment.presenter = presenter;
        fragment.activity =  activity;
        fragment.fm = fm;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentTimeSlotBinding.inflate(inflater);
        View view = this.binding.getRoot();
        this.adapter = new TimeSlotListAdapter(activity,presenter);
        this.binding.listKosongContainer.setAdapter(this.adapter);
        this.presenter.loadLecturerTimeSlot();
        this.binding.btnAdd.setOnClickListener(this::onClick);
        this.binding.chipMon.setOnCheckedChangeListener(this::onCheckedChanged);
        this.binding.chipTue.setOnCheckedChangeListener(this::onCheckedChanged);
        this.binding.chipWed.setOnCheckedChangeListener(this::onCheckedChanged);
        this.binding.chipThu.setOnCheckedChangeListener(this::onCheckedChanged);
        this.binding.chipFri.setOnCheckedChangeListener(this::onCheckedChanged);
        this.binding.chipSat.setOnCheckedChangeListener(this::onCheckedChanged);
        this.binding.chipSun.setOnCheckedChangeListener(this::onCheckedChanged);
        return view;
    }

    @Override
    public void loadTimeSlots(List<LecturerGetTimeSlotsResponse> data){
        this.adapter.addData(data);
    }

    @Override
    public void deleteTimeSlotSuccess() {
        this.presenter.loadLecturerTimeSlot();
    }

    @Override
    public void postTimeSlotSuccess() {
        this.fragment.dismiss();
        this.presenter.loadLecturerTimeSlot();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            if (buttonView.getId() == this.binding.chipMon.getId()) {
                this.adapter.filter("mon");
            } else if (buttonView.getId() == this.binding.chipTue.getId()) {
                this.adapter.filter("tue");
            } else if (buttonView.getId() == this.binding.chipWed.getId()) {
                this.adapter.filter("wed");
            } else if (buttonView.getId() == this.binding.chipThu.getId()) {
                this.adapter.filter("thu");
            } else if (buttonView.getId() == this.binding.chipFri.getId()) {
                this.adapter.filter("fri");
            } else if (buttonView.getId() == this.binding.chipSat.getId()) {
                this.adapter.filter("sat");
            } else {
                this.adapter.filter("sun");
            }
        }else{
            this.adapter.filter("");
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == this.binding.btnAdd.getId()){
            this.fragment = TambahSlotKosongDosenDialogFragment.newInstance(this.presenter);
            this.fragment.show(this.fm,"dialog");
        }
    }
}
