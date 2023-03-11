package com.example.tubespppb2.view.frsPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tubespppb2.databinding.FragmentHalamanFrsBinding;
import com.example.tubespppb2.pojo.CoursesGetDaftarResponse;
import com.example.tubespppb2.pojo.EnrolledCourseGetResponse;
import com.example.tubespppb2.pojo.EnrollmentPostResponse;
import com.example.tubespppb2.pojo.EnrollmentReason;
import com.example.tubespppb2.pojo.PrasyaratGetResponse;
import com.example.tubespppb2.presenter.PresenterFRS;

import java.util.List;

public class HalamanFRSFragment extends Fragment implements IYear,IEnrolledCourse, IMatKul{
    private FragmentHalamanFrsBinding binding;
    private HalamanFRSListAdapter adapter;
    private PresenterFRS presenter;
    private FragmentManager fm;
    private String studentYear;
    private String activeYear;
    private HalamanFRSFragment(){}
    public static HalamanFRSFragment newInstance(PresenterFRS presenter) {
        HalamanFRSFragment fragment = new HalamanFRSFragment();
        fragment.presenter = presenter;
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentHalamanFrsBinding.inflate(inflater);
        View view = this.binding.getRoot();
        this.fm = getParentFragmentManager();
        this.adapter = new HalamanFRSListAdapter(getActivity(), fm,presenter);
        binding.listPengumuman.setAdapter(this.adapter);
        this.presenter.getStudentInitialYear();
        return view;
    }


    @Override
    public void loadStudentYear(String year) {
        this.studentYear = year;
        this.presenter.getActiveAcademicYear();
    }

    @Override
    public void loadActiveYear(String year) {
        this.activeYear = year;
        getAcademicYears(this.activeYear,year);
    }

    private void getAcademicYears(String activeYear, String studentYear) {
        this.adapter.loadData(presenter.getAcademicYears(activeYear, studentYear));
    }

    @Override
    public void loadEnrolment(List<EnrolledCourseGetResponse> data) {
        this.adapter.loadEnrolment(data);
    }

    @Override
    public void deleteEnrolmentSuccess() {
        this.adapter.deleteEnrolmentSuccess();
    }

    @Override
    public void loadPrasyarat(List<PrasyaratGetResponse> data, int choice) {
        this.adapter.loadPrasyarat(data,choice);
    }

    @Override
    public void addEnrollment(EnrollmentPostResponse data, String nama) {
        this.adapter.addEnrollment(data,nama);
    }

    @Override
    public void showPrasyaratGagal(List<EnrollmentReason> dataErr) {
        this.adapter.showPrasyaratGagal(dataErr);
    }

    @Override
    public void getCourseSuccess(List<CoursesGetDaftarResponse> data, int choice) {
        this.adapter.getCourseSuccess(data,choice);
    }
}
