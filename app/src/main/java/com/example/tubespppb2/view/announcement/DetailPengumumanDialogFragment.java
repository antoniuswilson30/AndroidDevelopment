package com.example.tubespppb2.view.announcement;

import android.app.Dialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tubespppb2.R;
import com.example.tubespppb2.databinding.FragmentDetailPengumumanBinding;
import com.example.tubespppb2.pojo.Tags;
import com.example.tubespppb2.presenter.PresenterMainActivityAnnouncement;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class DetailPengumumanDialogFragment extends DialogFragment {
    private FragmentDetailPengumumanBinding binding;
    private PresenterMainActivityAnnouncement presenter;
    private String title;
    private String content;
    private Tags[] tags;
    private String id;
    private DetailPengumumanDialogFragment(){}

    public static DetailPengumumanDialogFragment newInstance(PresenterMainActivityAnnouncement presenter, String title, String content, Tags[] tags, String id) {
        DetailPengumumanDialogFragment fragment = new DetailPengumumanDialogFragment();
        fragment.presenter = presenter;
        fragment.title = title;
        fragment.content = content;
        fragment.tags = tags;
        fragment.id = id;
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
        this.binding = FragmentDetailPengumumanBinding.inflate(inflater);
        View view = this.binding.getRoot();
        this.binding.tvTitle.setText(this.title);
        this.addChipToGroup();
        this.binding.tvContent.setText(this.content);
        this.binding.btnMarkAsRead.setOnClickListener(this::onClick);
        this.binding.tvContent.setMovementMethod(new ScrollingMovementMethod());
        return view;
    }

    private void onClick(View view) {
        dismiss();
    }

    private void addChipToGroup(){
        ChipGroup chipGroup = this.binding.chipGroup;
        for(int i = 0; i < tags.length; i++){
            Chip chip = new Chip(getContext());
            chip.setText(tags[i].tag);
            chip.setClickable(false);
            chipGroup.addView(chip);
        }
    }
}
