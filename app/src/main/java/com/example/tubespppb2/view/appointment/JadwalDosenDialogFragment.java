package com.example.tubespppb2.view.appointment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tubespppb2.R;
import com.example.tubespppb2.databinding.FragmentJadwalDosenBinding;
import com.example.tubespppb2.pojo.LecturerGetTimeSlotsResponse;
import com.example.tubespppb2.pojo.UsersGetByEmailResponse;
import com.example.tubespppb2.presenter.PresenterMainActivityAppointment;

import java.util.List;

public class JadwalDosenDialogFragment extends DialogFragment implements View.OnClickListener, IAppointmentJadwalDosen, CompoundButton.OnCheckedChangeListener {
    private FragmentJadwalDosenBinding binding;
    private PresenterMainActivityAppointment presenter;
    private JadwalDosenListAdapter adapter;
    private JadwalDosenDialogFragment(){}

    public static JadwalDosenDialogFragment newInstance(PresenterMainActivityAppointment presenter) {
        JadwalDosenDialogFragment fragment = new JadwalDosenDialogFragment();
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
        this.binding = FragmentJadwalDosenBinding.inflate(inflater);
        View view = this.binding.getRoot();
        this.adapter = new JadwalDosenListAdapter(getActivity());
        this.binding.listContainer.setAdapter(this.adapter);
        this.binding.btnClose.setOnClickListener(this::onClick);
        this.binding.tombolSearch.setOnClickListener(this::onClick);
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
    public void onClick(View v) {
        if(v.getId() == this.binding.btnClose.getId()){
            dismiss();
        }else if(v.getId() == this.binding.tombolSearch.getId()){
            this.resetChipFilter();
            String email = this.binding.etEmailDosen.getEditText().getText().toString();
            this.presenter.getLecturerId(email);
        }
    }

    private void resetChipFilter(){
        this.binding.chipMon.setChecked(false);
        this.binding.chipTue.setChecked(false);
        this.binding.chipWed.setChecked(false);
        this.binding.chipThu.setChecked(false);
        this.binding.chipFri.setChecked(false);
        this.binding.chipSat.setChecked(false);
        this.binding.chipSun.setChecked(false);
    }

    @Override
    public void getLecturerId(UsersGetByEmailResponse data) {
        this.presenter.getLecturerTimeSlots(data.id);
    }

    @Override
    public void loadLecturerTimeSlots(List<LecturerGetTimeSlotsResponse> data){
        this.adapter.addData(data);
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

}
