package com.example.tubespppb2.view.frsPage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.fragment.app.FragmentManager;

import com.example.tubespppb2.databinding.ItemListHalamanFrsBinding;
import com.example.tubespppb2.model.frs.AcademicYear;
import com.example.tubespppb2.pojo.CoursesGetDaftarResponse;
import com.example.tubespppb2.pojo.EnrolledCourseGetResponse;
import com.example.tubespppb2.pojo.EnrollmentPostResponse;
import com.example.tubespppb2.pojo.EnrollmentReason;
import com.example.tubespppb2.pojo.PrasyaratGetResponse;
import com.example.tubespppb2.presenter.PresenterFRS;

import java.util.ArrayList;
import java.util.List;

public class HalamanFRSListAdapter extends BaseAdapter implements IEnrolledCourse, IMatKul {
    private List<AcademicYear> listTahun;
    private Activity activity;
    private FragmentManager fm;
    private HalamanSemesterDialogFragment fragment;
    private PresenterFRS presenterFRS;
    public HalamanFRSListAdapter(Activity activity, FragmentManager fm, PresenterFRS presenterFRS){
        this.listTahun = new ArrayList<>();
        this.activity = activity;
        this.presenterFRS = presenterFRS;
        this.fm = fm;
    }
    public void loadData(List<AcademicYear> list ){
        listTahun.clear();
        listTahun.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return this.listTahun.size();
    }

    @Override
    public Object getItem(int i) {
        return this.listTahun.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemListHalamanFrsBinding binding = ItemListHalamanFrsBinding.inflate(activity.getLayoutInflater());
        HalamanFRSListAdapter.ViewHolder viewHolder = new HalamanFRSListAdapter.ViewHolder(binding,position);
        View itemView = binding.getRoot();
        viewHolder.updateView();
        return itemView;
    }

    @Override
    public void loadEnrolment(List<EnrolledCourseGetResponse> data) {
        this.fragment.loadEnrolment(data);
    }

    @Override
    public void deleteEnrolmentSuccess() {
        this.fragment.deleteEnrolmentSuccess();
    }

    @Override
    public void loadPrasyarat(List<PrasyaratGetResponse> data, int choice) {
        this.fragment.loadPrasyarat(data,choice);
    }

    @Override
    public void addEnrollment(EnrollmentPostResponse data, String nama) {
        this.fragment.addEnrollment(data,nama);
    }

    @Override
    public void showPrasyaratGagal(List<EnrollmentReason> dataErr) {
        this.fragment.showPrasyaratGagal(dataErr);
    }

    @Override
    public void getCourseSuccess(List<CoursesGetDaftarResponse> data, int choice) {
        this.fragment.getCourseSuccess(data, choice);
    }

    private class ViewHolder implements View.OnClickListener{
        private ItemListHalamanFrsBinding binding;
        private int idx;

        public ViewHolder(ItemListHalamanFrsBinding binding, int position) {
            this.binding = binding;
            this.idx = position;
            if( listTahun.get(idx).getStatus() == 2 || listTahun.get(idx).getStatus() == 1){
                this.binding.itemListWrapper.setOnClickListener(this::onClick);
            }
        }

        private void updateView(){
            binding.tahunAjaranTv.setText(listTahun.get(idx).getNama());
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putString("titleSemester", listTahun.get(idx).getNama());
            if(listTahun.get(idx).getStatus() == 2){
                bundle.putInt("status", 2);
            }else if(listTahun.get(idx).getStatus() == 1){
                bundle.putInt("status", 1);
            }
            bundle.putString("tahun", listTahun.get(idx).getTahun());
            showDialog(bundle);
        }
    }

    private void showDialog(Bundle bundle){
        this.fragment = HalamanSemesterDialogFragment.newInstance(this.presenterFRS,bundle);
        this.fragment.show(this.fm,"dialog");
    }
}
