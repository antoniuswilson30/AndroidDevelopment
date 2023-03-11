package com.example.tubespppb2.view.appointment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tubespppb2.R;
import com.example.tubespppb2.databinding.HalamanTambahPertemuanBinding;
import com.example.tubespppb2.presenter.PresenterMainActivityAppointment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HalamanTambahPertemuanFragment extends DialogFragment implements View.OnClickListener {
    private HalamanTambahPertemuanBinding binding;
    private PresenterMainActivityAppointment presenter;
    private Date []dateArr;
    private String[] timeArr;
    private HalamanTambahPertemuanFragment(){}

    public static HalamanTambahPertemuanFragment newInstance(PresenterMainActivityAppointment presenter) {
        HalamanTambahPertemuanFragment fragment = new HalamanTambahPertemuanFragment();
        fragment.presenter = presenter;
        fragment.dateArr = new Date[2];
        fragment.timeArr = new String[2];
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
        this.binding = HalamanTambahPertemuanBinding.inflate(inflater);
        View view = this.binding.getRoot();
        this.binding.btnCalendarMulai.setOnClickListener(this::onClick);
        this.binding.btnCalendarAkhir.setOnClickListener(this::onClick);
        this.binding.btnClockMulai.setOnClickListener(this::onClick);
        this.binding.btnClockAkhir.setOnClickListener(this::onClick);
        this.binding.btnBerikutnya.setOnClickListener(this::onClick);
        this.binding.btnClose.setOnClickListener(this::onClick);
        return view;
    }

    private void showDatePicker(int idx, EditText editText){
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("pilih tanggal")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Date date = new Date((Long)selection);
                dateArr[idx] = date;
                editText.setText(formatDate(date,2));
            }
        });

        datePicker.show(getActivity().getSupportFragmentManager(), "tag");
    }

    private String formatDate(Date date,int choice){
        if(date != null) {
            if (choice == 1) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String res = dateFormat.format(date);
                return res;
            } else {
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String res = dateFormat.format(date);
                return res;
            }
        }else{
            return null;
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

    @Override
    public void onClick(View v) {
        if(v.getId() == this.binding.btnCalendarMulai.getId()){
            this.showDatePicker(0,this.binding.etTanggalMulai.getEditText());
        }else if(v.getId() == this.binding.btnCalendarAkhir.getId()){
            this.showDatePicker(1,this.binding.etTanggalAkhir.getEditText());
        }else if(v.getId() == this.binding.btnClockMulai.getId()){
            this.showTimePicker(0,this.binding.etWaktuMulai.getEditText());
        }else if(v.getId() == this.binding.btnClockAkhir.getId()){
            this.showTimePicker(1,this.binding.etWaktuAkhir.getEditText());
        }else if(v.getId() == this.binding.btnBerikutnya.getId()){
            String title = this.binding.etJudulPertemuan.getEditText().getText().toString();
            String desc = this.binding.etDeskripsi.getText().toString();
            String startDate = formatDate(dateArr[0],1);
            String endDate = formatDate(dateArr[1],1);
            String startDateTime = startDate+" "+timeArr[0];
            String endDateTime = endDate+" "+timeArr[1];
            if(startDate == null || timeArr[0] == null){
                startDateTime = null;
            }

            if(endDate == null || timeArr[1] == null){
                endDateTime = null;
            }
            this.presenter.addAppointment(title,desc,startDateTime,endDateTime);
        }else if(v.getId() == this.binding.btnClose.getId()){
            this.dismiss();
        }
    }
}
