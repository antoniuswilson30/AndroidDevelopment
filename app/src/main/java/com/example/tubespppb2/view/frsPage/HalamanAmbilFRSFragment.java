package com.example.tubespppb2.view.frsPage;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.tubespppb2.R;
import com.example.tubespppb2.databinding.HalamanTambahFrsBinding;
import com.example.tubespppb2.pojo.CoursesGetDaftarResponse;
import com.example.tubespppb2.pojo.EnrollmentPostResponse;
import com.example.tubespppb2.pojo.EnrollmentReason;
import com.example.tubespppb2.pojo.PrasyaratGetResponse;
import com.example.tubespppb2.presenter.PresenterFRS;

import java.util.List;

public class HalamanAmbilFRSFragment extends DialogFragment implements IMatKul, View.OnClickListener {
    private PresenterFRS presenter;
    private HalamanTambahFrsBinding binding;
    private PrasyaratAdapter adapter;

    private HalamanAmbilFRSFragment() {
    }


    public static HalamanAmbilFRSFragment newInstance(PresenterFRS presenter, Bundle bundle) {
        HalamanAmbilFRSFragment fragment = new HalamanAmbilFRSFragment();
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
        this.binding = HalamanTambahFrsBinding.inflate(inflater);
        View view = binding.getRoot();
        this.binding.tombolSearch.setOnClickListener(this::onClick);
        this.binding.btnClose.setOnClickListener(this::onClick);
        this.binding.btnAdd.setOnClickListener(this::onClick);
        this.binding.btnAdd.setOnClickListener(this::onClick);
        this.adapter = new PrasyaratAdapter(getActivity());
        this.binding.listContainer.setAdapter(this.adapter);
        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == this.binding.btnClose.getId()) {
            dismiss();
        } else if (v.getId() == this.binding.tombolSearch.getId()) {
            String namaMatkul = this.binding.etMataKuliah.getEditText().getText().toString();
            this.presenter.getCourses(namaMatkul, 1);
        } else if (v.getId() == this.binding.btnAdd.getId()) {
            String namaMatkul = this.binding.etMataKuliah.getEditText().getText().toString();
            this.presenter.getCourses(namaMatkul, 2);
        }
    }

    @Override
    public void loadPrasyarat(List<PrasyaratGetResponse> data, int choice) {
        this.adapter.loadData(data, false);
    }

    @Override
    public void addEnrollment(EnrollmentPostResponse data, String nama) {
        Context context = getContext().getApplicationContext();
        CharSequence text = nama+" berhasil ditambahkan";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        this.adapter.clearData();
        this.binding.etMataKuliah.getEditText().setText("");
        FragmentManager fm = getParentFragmentManager();
        fm.setFragmentResult("loadEnrolment",null);
    }

    @Override
    public void showPrasyaratGagal(List<EnrollmentReason> dataErr) {
        this.adapter.loadDataErr(dataErr,true);
    }

    @Override
    public void getCourseSuccess(List<CoursesGetDaftarResponse> data, int choice) {
        if(data.size() != 0) {
            if (choice == 1) {
                this.presenter.getPrasyarat(data.get(0).id, choice);
            } else if (choice == 2) {
                Bundle bundle = this.getArguments();
                String nama = this.binding.etMataKuliah.getEditText().getText().toString();
                this.presenter.postEnrollment(data.get(0).id, bundle.getInt("tahun"), nama);
            }
        }else{
            this.presenter.showError("mata kuliah tidak ada");
        }
    }
}
