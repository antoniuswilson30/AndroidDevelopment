package com.example.tubespppb2.view.frsPage;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import com.example.tubespppb2.R;
import com.example.tubespppb2.databinding.FragmentHalamanSemesterBinding;
import com.example.tubespppb2.pojo.CoursesGetDaftarResponse;
import com.example.tubespppb2.pojo.EnrolledCourseGetResponse;
import com.example.tubespppb2.pojo.EnrollmentPostResponse;
import com.example.tubespppb2.pojo.EnrollmentReason;
import com.example.tubespppb2.pojo.PrasyaratGetResponse;
import com.example.tubespppb2.presenter.PresenterFRS;

import java.util.List;

public class HalamanSemesterDialogFragment extends DialogFragment implements IEnrolledCourse, IMatKul, View.OnClickListener{
    private FragmentManager fm;
    private FragmentHalamanSemesterBinding binding;
    private HalamanSemesterListAdapter adapter;
    private PresenterFRS presenterFRS;
    private HalamanAmbilFRSFragment halamanAmbilFRSFragment;

    private HalamanSemesterDialogFragment(){}
    public static HalamanSemesterDialogFragment newInstance(PresenterFRS presenter, Bundle bundle){
        HalamanSemesterDialogFragment fragment = new HalamanSemesterDialogFragment();
        fragment.presenterFRS = presenter;
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
        this.binding = FragmentHalamanSemesterBinding.inflate(inflater);
        View view = this.binding.getRoot();
        this.fm = getParentFragmentManager();
        Bundle bundle = this.getArguments();
        String academic_years = bundle.getString("tahun");
        this.adapter = new HalamanSemesterListAdapter(getActivity(), this.fm, this.presenterFRS, Integer.parseInt(academic_years));

        this.binding.listMataKuliah.setAdapter(this.adapter);
        this.binding.titleSemester.setText(bundle.getString("titleSemester"));

        if(bundle.getInt("status") == 2){
            this.binding.fab.hide();
        }else{
            this.binding.fab.setOnClickListener(this::onClick);
        }

        this.presenterFRS.loadEnrolment(bundle.getString("tahun"));

        this.binding.btnClose.setOnClickListener(this::onClick);

        this.fm.setFragmentResultListener("loadEnrolment", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                presenterFRS.loadEnrolment(bundle.getString("tahun"));
            }
        });
        return view;
    }

    @Override
    public void onClick(View view){
        if(view.getId() == this.binding.fab.getId()) {
            Bundle temp = this.getArguments();
            Bundle bundle = new Bundle();
            bundle.putInt("tahun", Integer.parseInt(temp.getString("tahun")));
            this.halamanAmbilFRSFragment = HalamanAmbilFRSFragment.newInstance(presenterFRS,bundle);
            this.halamanAmbilFRSFragment.show(this.fm,"dialog");
        }else if(view.getId() == this.binding.btnClose.getId()){
            this.dismiss();
        }
    }

    @Override
    public void loadEnrolment(List<EnrolledCourseGetResponse> data) {
        Bundle bundle = this.getArguments();
        this.adapter.loadData(data, (bundle.getInt("status") == 2));
    }

    @Override
    public void deleteEnrolmentSuccess() {
        Bundle bundle = this.getArguments();
        this.presenterFRS.loadEnrolment(bundle.getString("tahun"));
    }

    @Override
    public void loadPrasyarat(List<PrasyaratGetResponse> data, int choice) {
        this.halamanAmbilFRSFragment.loadPrasyarat(data,choice);
    }

    @Override
    public void addEnrollment(EnrollmentPostResponse data, String nama) {
        this.halamanAmbilFRSFragment.addEnrollment(data,nama);
    }

    @Override
    public void showPrasyaratGagal(List<EnrollmentReason> dataErr) {
        this.halamanAmbilFRSFragment.showPrasyaratGagal(dataErr);
    }

    @Override
    public void getCourseSuccess(List<CoursesGetDaftarResponse> data, int choice) {
        this.halamanAmbilFRSFragment.getCourseSuccess(data, choice);
    }
}
