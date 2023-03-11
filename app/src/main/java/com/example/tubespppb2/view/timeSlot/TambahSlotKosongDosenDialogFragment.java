package com.example.tubespppb2.view.timeSlot;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tubespppb2.R;
import com.example.tubespppb2.databinding.FragmentTambahSlotKosongDosenBinding;
import com.example.tubespppb2.presenter.PresenterMainActivityTimeSlot;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;

public class TambahSlotKosongDosenDialogFragment extends DialogFragment implements View.OnClickListener{
    private FragmentTambahSlotKosongDosenBinding binding;
    private PresenterMainActivityTimeSlot presenter;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> hari;
    private String[] timeArr;
    private TambahSlotKosongDosenDialogFragment(){}

    public static TambahSlotKosongDosenDialogFragment newInstance(PresenterMainActivityTimeSlot presenter) {
        TambahSlotKosongDosenDialogFragment fragment = new TambahSlotKosongDosenDialogFragment();
        fragment.presenter = presenter;
        return fragment;
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return dialog;
    }
    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.dimAmount = 0;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        getDialog().getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_TubesPPPB2);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentTambahSlotKosongDosenBinding.inflate(inflater);
        View view = this.binding.getRoot();
        this.timeArr = new String[2];
        this.hari = new ArrayList<>();
        this.loadHari();
        this.adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,this.hari);
        this.binding.dropdownSearch.setAdapter(this.adapter);
        this.binding.btnClockMulai.setOnClickListener(this::onClick);
        this.binding.btnClockAkhir.setOnClickListener(this::onClick);
        this.binding.btnSimpan.setOnClickListener(this::onClick);
        this.binding.btnClose.setOnClickListener(this::onClick);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == this.binding.btnClockMulai.getId()){
            this.showTimePicker(0,this.binding.etWaktuMulai.getEditText());
        }else if(v.getId() == this.binding.btnClockAkhir.getId()){
            this.showTimePicker(1,this.binding.etWaktuAkhir.getEditText());
        }else if(v.getId() == this.binding.btnSimpan.getId()){
            String day = this.binding.chooseDay.getEditText().getText().toString();
            this.presenter.postLecturerTimeSlot(day,timeArr[0],timeArr[1]);
        }else{
            dismiss();
        }
    }

    private void showTimePicker(int idx, EditText editText){
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTitleText("pick time")
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build();

        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = String.format("%02d:%02d+0700",timePicker.getHour(),timePicker.getMinute());
                timeArr[idx] = time;
                String timeToShow = String.format("%02d:%02d",timePicker.getHour(),timePicker.getMinute());
                editText.setText(timeToShow);
            }
        });
        timePicker.show(getActivity().getSupportFragmentManager(),"tag");
    }

    private void loadHari(){
        this.hari.clear();
        this.hari.add("mon");
        this.hari.add("tue");
        this.hari.add("wed");
        this.hari.add("thu");
        this.hari.add("fri");
        this.hari.add("sat");
        this.hari.add("sun");
    }
}
