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
import com.example.tubespppb2.databinding.FragmentDetailAppointmentBinding;
import com.example.tubespppb2.pojo.AppointmentGetDaftarPesertaResponse;
import com.example.tubespppb2.presenter.PresenterMainActivityAppointment;

import java.util.List;

public class DetailAppointmentDialogFragment extends DialogFragment implements IAppointmentDetailPeserta, View.OnClickListener{
    private FragmentDetailAppointmentBinding binding;
    private DetailAppointmentListAdapter adapter;
    private PresenterMainActivityAppointment presenter;

    private DetailAppointmentDialogFragment(){}

    public static DetailAppointmentDialogFragment newInstance(PresenterMainActivityAppointment presenter, Bundle bundle) {
        DetailAppointmentDialogFragment fragment = new DetailAppointmentDialogFragment();
        fragment.presenter = presenter;
        fragment.setArguments(bundle);
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
        this.binding = FragmentDetailAppointmentBinding.inflate(inflater);
        View view = this.binding.getRoot();
        this.setView();
        Bundle bundle = this.getArguments();
        this.adapter = new DetailAppointmentListAdapter(getActivity());
        this.binding.listDetailPartisipan.setAdapter(this.adapter);
        this.presenter.loadDaftarPesertaDetail(bundle.getString("appointmentId"));
        this.binding.btnClose.setOnClickListener(this::onClick);
        return view;
    }

    private void setView(){
        Bundle bundle = this.getArguments();
        String judul = bundle.getString("judul");
        String desc = bundle.getString("desc");
        String organizedBy = bundle.getString("organizedBy");
        String startDate = bundle.getString("startDate");
        String endDate = bundle.getString("endDate");
        String startTime = bundle.getString("startTime");
        String endTime = bundle.getString("endTime");
        this.binding.tvTitle.setText(judul);
        this.binding.tvDescription.setText(desc);
        this.binding.tvOrganizer.setText(organizedBy);
        this.binding.tvStartDate.setText(startDate);
        this.binding.tvEndDate.setText(endDate);
        this.binding.tvStartTime.setText(startTime);
        this.binding.tvEndTime.setText(endTime);
    }

    @Override
    public void loadDaftarPesertaDetail(List<AppointmentGetDaftarPesertaResponse> data) {
        this.adapter.loadDataPeserta(data);
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }
}
