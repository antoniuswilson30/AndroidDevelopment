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

import com.example.tubespppb2.databinding.FragmentAddTagDialogBinding;
import com.example.tubespppb2.pojo.TagGetResponse;
import com.example.tubespppb2.presenter.PresenterMainActivityAnnouncement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddTagDialogFragment extends DialogFragment implements ITag {
    private FragmentAddTagDialogBinding binding;
    private PresenterMainActivityAnnouncement presenter;
    private AnnouncementTagListAdapter adapter;
    private Activity activity;
    private FragmentManager fm;
    private AddTagDialogFragment(){}

    public static AddTagDialogFragment newInstance(PresenterMainActivityAnnouncement presenter, Activity activity, FragmentManager fm, HashMap<String,String> mp) {
        AddTagDialogFragment fragment = new AddTagDialogFragment();
        fragment.presenter = presenter;
        fragment.activity = activity;
        fragment.adapter = new AnnouncementTagListAdapter(activity,mp );
        fragment.fm = fm;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentAddTagDialogBinding.inflate(inflater);
        View view = this.binding.getRoot();
        this.binding.listTag.setAdapter(this.adapter);
        this.binding.btnSearch.setOnClickListener(this::onClick);
        this.binding.addbtn.setOnClickListener(this::onClick);
        this.presenter.loadTag();
        return view;
    }

    private void onClick(View view) {
        if(view.getId() == this.binding.btnSearch.getId()) {
            String search = this.binding.etSearch.getEditText().getText().toString();
            this.adapter.search(search);
        }else{
            List<TagGetResponse> temp = this.adapter.getTagSelected();
            ArrayList<TagGetResponse> tags = new ArrayList<>();
            tags.addAll(temp);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("tags",tags);
            fm.setFragmentResult("tags",bundle);
            dismiss();
        }
    }

    @Override
    public void loadTag(List<TagGetResponse> data) {
        this.adapter.loadTag(data);
    }
}
