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
import androidx.fragment.app.FragmentResultListener;

import com.example.tubespppb2.databinding.FragmentTambahPengumumanBinding;
import com.example.tubespppb2.pojo.TagGetResponse;
import com.example.tubespppb2.presenter.PresenterMainActivityAnnouncement;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TambahPengumumanDialogFragment extends DialogFragment implements View.OnClickListener, ITag {
    private FragmentTambahPengumumanBinding binding;
    private PresenterMainActivityAnnouncement presenter;
    private Activity activity;
    private FragmentManager fm;
    private HashMap<String, String> mp;
    private ArrayList<TagGetResponse> tags;
    private AddTagDialogFragment addTagDialogFragment;
    private TambahPengumumanDialogFragment(){}

    public static TambahPengumumanDialogFragment newInstance(PresenterMainActivityAnnouncement presenter, Activity activity, FragmentManager fm) {
        TambahPengumumanDialogFragment fragment = new TambahPengumumanDialogFragment();
        fragment.presenter = presenter;
        fragment.activity = activity;
        fragment.fm = fm;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentTambahPengumumanBinding.inflate(inflater);
        View view = this.binding.getRoot();
        this.mp = new HashMap<>();
        this.tags = new ArrayList<>();
        this.binding.btnClose.setOnClickListener(this::onClick);
        this.binding.btnAddTag.setOnClickListener(this::onClick);
        this.binding.btnSimpan.setOnClickListener(this::onClick);
        this.fm.setFragmentResultListener("tags", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                tags.clear();
                mp.clear();
                tags.addAll(result.getParcelableArrayList("tags"));

                clearChips();
                for(int i = 0; i < tags.size(); i++){
                    mp.put(tags.get(i).id,"");
                    addChips(tags.get(i).tag);
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == this.binding.btnClose.getId()){
            dismiss();
        }else if(v.getId() == this.binding.btnAddTag.getId()){
            this.addTagDialogFragment = AddTagDialogFragment.newInstance(this.presenter,activity,this.fm,this.mp);
            addTagDialogFragment.show(this.fm,"dialog");
        }else if(v.getId() == this.binding.btnSimpan.getId()){
            String[] tagsPost = new String[this.tags.size()];
            for(int i = 0; i < this.tags.size(); i++){
                tagsPost[i] = this.tags.get(i).id;
            }
            String title = this.binding.outlinedTextFieldJudul.getEditText().getText().toString();
            String content = this.binding.outlinedTextFieldDesc.getEditText().getText().toString();
            this.presenter.makeAnnouncement(title,content,tagsPost);
        }
    }

    private void addChips(String chips){
        ChipGroup chipGroup = this.binding.chipPartisipan;
        Chip chip = new Chip(getContext());
        chip.setText(chips);
        chip.setCloseIconVisible(true);
        chip.setClickable(false);
        chipGroup.addView(chip);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < tags.size(); i++){
                    if(tags.get(i).tag.equals(((Chip)v).getText().toString())){
                        mp.remove(tags.get(i).id);
                        tags.remove(i);
                        chipGroup.removeView(v);
                    }
                }
            }
        });
    }

    private void clearChips(){
        ChipGroup chipGroup = this.binding.chipPartisipan;
        chipGroup.removeAllViews();
    }

    @Override
    public void loadTag(List<TagGetResponse> data) {
        this.addTagDialogFragment.loadTag(data);
    }
}
