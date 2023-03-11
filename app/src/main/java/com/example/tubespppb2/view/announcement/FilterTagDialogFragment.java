package com.example.tubespppb2.view.announcement;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.tubespppb2.databinding.FragmentFilterTagDialogBinding;
import com.example.tubespppb2.pojo.TagGetResponse;
import com.example.tubespppb2.presenter.PresenterMainActivityAnnouncement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterTagDialogFragment extends DialogFragment implements ITag {
    private FragmentFilterTagDialogBinding binding;
    private PresenterMainActivityAnnouncement presenter;
    private AnnouncementFilterListAdapter adapter;
    private Activity activity;
    private FragmentManager fm;
    private FilterTagDialogFragment(){}

    public static FilterTagDialogFragment newInstance(PresenterMainActivityAnnouncement presenter, Activity activity, FragmentManager fm, HashMap<String,String> mp) {
        FilterTagDialogFragment fragment = new FilterTagDialogFragment();
        fragment.presenter = presenter;
        fragment.activity = activity;
        fragment.adapter = new AnnouncementFilterListAdapter(activity,mp );
        fragment.fm = fm;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentFilterTagDialogBinding.inflate(inflater);
        View view = this.binding.getRoot();
        this.binding.listTag.setAdapter(this.adapter);
        this.binding.btnSearch.setOnClickListener(this::onClick);
        this.binding.applybtn.setOnClickListener(this::onClick);
        this.presenter.loadTag();
        return view;
    }

    private void onClick(View view) {
        if(view.getId() == this.binding.btnSearch.getId()) {
            String search = this.binding.etSearch.getEditText().getText().toString();
            this.adapter.search(search);
        }else{
            ArrayList<TagGetResponse> filter = this.adapter.getFilter();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("filter",filter);
            fm.setFragmentResult("filter",bundle);
            dismiss();
        }
    }

    @Override
    public void loadTag(List<TagGetResponse> data) {
        this.adapter.loadTag(data);
    }
}

