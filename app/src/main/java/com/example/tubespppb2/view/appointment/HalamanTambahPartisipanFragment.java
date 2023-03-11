package com.example.tubespppb2.view.appointment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tubespppb2.R;
import com.example.tubespppb2.databinding.HalamanTambahPartisipanBinding;
import com.example.tubespppb2.pojo.LecturerGetTimeSlotsResponse;
import com.example.tubespppb2.pojo.UsersGetByEmailResponse;
import com.example.tubespppb2.presenter.PresenterMainActivityAppointment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HalamanTambahPartisipanFragment extends DialogFragment implements View.OnClickListener, IAppointmentJadwalDosen{
    private HalamanTambahPartisipanBinding binding;
    private PresenterMainActivityAppointment presenter;
    private HashMap<String,String> participants;
    private String appointmentId;
    private JadwalDosenDialogFragment jadwalDosenDialogFragment;
    private HalamanTambahPartisipanFragment(){}

    public static HalamanTambahPartisipanFragment newInstance(PresenterMainActivityAppointment presenter,String appointmentId) {
        HalamanTambahPartisipanFragment fragment = new HalamanTambahPartisipanFragment();
        fragment.presenter = presenter;
        fragment.appointmentId = appointmentId;
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
        this.binding = HalamanTambahPartisipanBinding.inflate(inflater);
        View view = this.binding.getRoot();
        this.participants = new HashMap<>();
        this.binding.btnJadwalDosen.setOnClickListener(this::onClick);
        this.binding.btnAdd.setOnClickListener(this::onClick);
        this.binding.btnSimpan.setOnClickListener(this::onClick);
        this.binding.btnClose.setOnClickListener(this::onClick);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == this.binding.btnAdd.getId()){
            String email = this.binding.inputPartisipan.getEditText().getText().toString();
            this.presenter.isEmailExist(email);
        }else if(v.getId() == this.binding.btnJadwalDosen.getId()){
            this.jadwalDosenDialogFragment = JadwalDosenDialogFragment.newInstance(this.presenter);
            this.jadwalDosenDialogFragment.show(getParentFragmentManager(),"dialog");
        }else if(v.getId() == this.binding.btnSimpan.getId()){
            String[] idParticipant = new String[this.participants.size()];
            int i = 0;
            for(Map.Entry<String,String> set:  participants.entrySet()){
                idParticipant[i] = set.getValue();
                i++;
            }
            this.presenter.addParticipants(idParticipant, this.appointmentId);
        }else if(v.getId() == this.binding.btnClose.getId()){
            dismiss();
        }
    }

    public void emailExist(UsersGetByEmailResponse data){
        if(!this.participants.containsKey(data.email)) {
            this.addChips(data.email);
            this.participants.put(data.email,data.id);
        }
        this.binding.inputPartisipan.getEditText().setText("");
    }

    @Override
    public void getLecturerId(UsersGetByEmailResponse data){
        this.jadwalDosenDialogFragment.getLecturerId(data);
    }

    @Override
    public void loadLecturerTimeSlots(List<LecturerGetTimeSlotsResponse> data) {
        this.jadwalDosenDialogFragment.loadLecturerTimeSlots(data);
    }

    private void addChips(String chips){
        ChipGroup chipGroup = this.binding.tambahPartisipanChip;
        Chip chip = new Chip(getContext());
        chip.setText(chips);
        chip.setCloseIconVisible(true);
        chip.setClickable(false);
        chipGroup.addView(chip);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participants.remove(((Chip)v).getText().toString());
                chipGroup.removeView(v);
            }
        });
    }
}
