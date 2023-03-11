package com.example.tubespppb2.view.accountPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tubespppb2.databinding.FragmentPageAccountBinding;
import com.example.tubespppb2.presenter.PresenterMainActivityAccountPage;
import com.example.tubespppb2.service.SharedPreferenceStore;

public class PageAccountFragment extends Fragment implements View.OnClickListener{
    private FragmentPageAccountBinding binding;
    private PresenterMainActivityAccountPage presenterMainActivity;
    private PageAccountFragment(){}
    public static PageAccountFragment newInstance(PresenterMainActivityAccountPage presenterMainActivity) {
        PageAccountFragment fragment = new PageAccountFragment();
        fragment.presenterMainActivity = presenterMainActivity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentPageAccountBinding.inflate(inflater);
        View view = this.binding.getRoot();
        setView();
        this.binding.btnLogout.setOnClickListener(this::onClick);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == this.binding.btnLogout.getId()){
            SharedPreferenceStore spStore = new SharedPreferenceStore(getActivity().getApplicationContext());
            spStore.setToken("");
            spStore.setRole("");
            spStore.setEmail("");
            spStore.setUserId("");
            spStore.setNama("");
            FragmentManager fm = getParentFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putInt("page",1);
            fm.setFragmentResult("changePage",bundle);
        }
    }

    private void setView(){
        SharedPreferenceStore spStore = new SharedPreferenceStore(getActivity().getApplicationContext());
        String nama = spStore.getNama();
        String email = spStore.getEmail();
        String role = spStore.getRole();
        this.binding.tvName.setText(nama);
        this.binding.tvEmail.setText(email);
        this.binding.tvRole.setText(role);
    }
}
