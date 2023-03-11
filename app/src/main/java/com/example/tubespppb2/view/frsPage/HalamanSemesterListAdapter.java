package com.example.tubespppb2.view.frsPage;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.fragment.app.FragmentManager;

import com.example.tubespppb2.databinding.ItemListMataKuliahBinding;
import com.example.tubespppb2.pojo.EnrolledCourseGetResponse;
import com.example.tubespppb2.presenter.PresenterFRS;

import java.util.ArrayList;
import java.util.List;

public class HalamanSemesterListAdapter extends BaseAdapter {
    private List<EnrolledCourseGetResponse> data;
    private Activity activity;
    private FragmentManager fm;
    private boolean lewatSemester;
    private PresenterFRS presenterFRS;
    private int academic_years;
    public HalamanSemesterListAdapter(Activity activity, FragmentManager fm, PresenterFRS presenterFRS, int academic_years){
        this.activity = activity;
        this.fm = fm;
        this.data = new ArrayList<>();
        this.presenterFRS = presenterFRS;
        this.academic_years = academic_years;
    }

    public void loadData(List<EnrolledCourseGetResponse> data, boolean lewatSemester){
        this.data.clear();
        this.data.addAll(data);
        this.lewatSemester = lewatSemester;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public Object getItem(int i) {
        return this.data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ItemListMataKuliahBinding binding = ItemListMataKuliahBinding.inflate(activity.getLayoutInflater());
        HalamanSemesterListAdapter.ViewHolder viewHolder = new ViewHolder(binding,i);
        View itemView = binding.getRoot();
        viewHolder.updateView();
        return itemView;
    }

    private class ViewHolder implements View.OnClickListener{
        private ItemListMataKuliahBinding binding;
        private int idx;

        public ViewHolder(ItemListMataKuliahBinding binding, int position){
            this.binding = binding;
            this.idx = position;
            this.binding.btnDelete.setOnClickListener(this::onClick);
        }

        public void updateView(){
            this.binding.mataKuliahTv.setText(data.get(idx).name);
            if(lewatSemester){
               this.binding.btnDelete.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view){
            if(view.getId() == this.binding.btnDelete.getId()){
                presenterFRS.deleteEnrolment(data.get(idx).course_id,academic_years);
            }
        }
    }
}
