package com.example.tubespppb2.view.appointment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tubespppb2.IError;
import com.example.tubespppb2.databinding.FragmentDaftarPertemuanBinding;
import com.example.tubespppb2.pojo.AppointmentGetDaftarOwnerAndParticipantResponse;
import com.example.tubespppb2.pojo.AppointmentGetDaftarPesertaResponse;
import com.example.tubespppb2.pojo.LecturerGetTimeSlotsResponse;
import com.example.tubespppb2.pojo.UsersGetByEmailResponse;
import com.example.tubespppb2.presenter.PresenterMainActivityAppointment;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DaftarPertemuanFragment extends Fragment implements View.OnClickListener, IAppointment, IAppointmentJadwalDosen,IAppointmentDetailPeserta {
    private FragmentDaftarPertemuanBinding binding;
    private Date[] dateArr;
    private PresenterMainActivityAppointment presenter;
    private DaftarPertemuanListAdapter adapter;
    private FragmentManager fm;
    private HalamanTambahPertemuanFragment tambahPertemuanFragment;
    private HalamanTambahPartisipanFragment tambahPartisipanFragment;
    private IError iError;
    private DaftarPertemuanFragment(){}
    public static DaftarPertemuanFragment newInstance(PresenterMainActivityAppointment presenter, FragmentManager fm, IError iError) {
        DaftarPertemuanFragment fragment = new DaftarPertemuanFragment();
        fragment.presenter = presenter;
        fragment.dateArr = new Date[2];
        fragment.fm = fm;
        fragment.iError = iError;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentDaftarPertemuanBinding.inflate(inflater);
        View view = this.binding.getRoot();
        this.adapter = new DaftarPertemuanListAdapter(getActivity(), this.presenter,getParentFragmentManager());
        this.binding.btnStartDate.setOnClickListener(this::onClick);
        this.binding.btnEndDate.setOnClickListener(this::onClick);
        this.binding.btnSearch.setOnClickListener(this::onClick);
        this.binding.btnAdd.setOnClickListener(this::onClick);
        this.binding.listPertemuanContainer.setAdapter(this.adapter);
        this.binding.chipFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.filterByMe(isChecked);
            }
        });


        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == this.binding.btnStartDate.getId()){
            EditText editText = this.binding.etTanggalStart.getEditText();
            this.showDatePicker(0,editText);
        }else if(v.getId() == this.binding.btnEndDate.getId()){
            EditText editText = this.binding.etTanggalEnd.getEditText();
            this.showDatePicker(1,editText);
        }else if(v.getId() == this.binding.btnSearch.getId()){
            if(dateArr[0] != null && dateArr[1] != null) {
                this.presenter.loadData(this.formatDate(dateArr[0], 1), this.formatDate(dateArr[1], 1));
            }else{
                this.iError.showError("tanggal harus diisi");
            }
        }else if(v.getId() == this.binding.btnAdd.getId()){
            this.tambahPertemuanFragment =  HalamanTambahPertemuanFragment.newInstance(this.presenter);
            this.tambahPertemuanFragment.show(this.fm, "dialog");
        }
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
        if(choice == 1) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String res = dateFormat.format(date);
            return res;
        }else{
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String res = dateFormat.format(date);
            return res;
        }
    }

    @Override
    public void addData(List<AppointmentGetDaftarOwnerAndParticipantResponse> data) {
        this.adapter.loadData(data);
        this.resetChipFilter();
    }

    @Override
    public void addAppointmentSuccess(String appointmentId) {
        this.tambahPartisipanFragment = HalamanTambahPartisipanFragment.newInstance(this.presenter,appointmentId);
        this.tambahPartisipanFragment.show(this.fm,"dialog");
        this.tambahPertemuanFragment.dismiss();
    }

    @Override
    public void emailExist(UsersGetByEmailResponse data) {
        this.tambahPartisipanFragment.emailExist(data);
    }

    @Override
    public void getLecturerId(UsersGetByEmailResponse data) {
        this.tambahPartisipanFragment.getLecturerId(data);
    }

    @Override
    public void addParticipantsSuccess() {
        this.tambahPartisipanFragment.dismiss();
        if(dateArr[0] != null && dateArr[1] != null) {
            this.presenter.loadData(this.formatDate(dateArr[0], 1), this.formatDate(dateArr[1], 1));
        }
    }

    @Override
    public void deleteAppointmentSuccess() {
        this.presenter.loadData(this.formatDate(dateArr[0], 1), this.formatDate(dateArr[1], 1));
    }

    @Override
    public void loadDaftarPesertaDetail(List<AppointmentGetDaftarPesertaResponse> data) {
        this.adapter.loadDaftarPesertaDetail(data);
    }

    @Override
    public void loadLecturerTimeSlots(List<LecturerGetTimeSlotsResponse> data) {
        this.tambahPartisipanFragment.loadLecturerTimeSlots(data);
    }

    private void resetChipFilter(){
        Chip chip =  this.binding.chipFilter;
        chip.setChecked(false);
    }
}
