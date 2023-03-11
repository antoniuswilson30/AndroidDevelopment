package com.example.tubespppb2.view.appointment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tubespppb2.databinding.FragmentUndanganPertemuanBinding;
import com.example.tubespppb2.pojo.AppointmentGetDaftarUndanganResponse;
import com.example.tubespppb2.presenter.PresenterMainActivityAppointment;

import java.util.List;

public class UndanganPertemuanFragment extends Fragment implements IUndanganAppointment {
    private FragmentUndanganPertemuanBinding binding;
    private PresenterMainActivityAppointment presenter;
    private UndanganPertemuanListAdapter adapter;
    private Activity activity;
    private UndanganPertemuanFragment(){}

    public static UndanganPertemuanFragment newInstance(PresenterMainActivityAppointment presenter, Activity activity) {
        UndanganPertemuanFragment fragment = new UndanganPertemuanFragment();
        fragment.presenter = presenter;
        fragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentUndanganPertemuanBinding.inflate(inflater);
        View view = this.binding.getRoot();
        this.presenter.getDaftarUndanganAppointment();
        this.adapter = new UndanganPertemuanListAdapter(activity,presenter);
        this.binding.listUndangan.setAdapter(this.adapter);
        return view;
    }

    @Override
    public void getDaftarUndanganSuccess(List<AppointmentGetDaftarUndanganResponse> data) {
        this.adapter.loadData(data);
    }

    @Override
    public void patchAppointmentRSVPSuccess() {
        this.presenter.getDaftarUndanganAppointment();
    }
}
