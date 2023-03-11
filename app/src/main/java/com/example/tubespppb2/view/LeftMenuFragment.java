package com.example.tubespppb2.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tubespppb2.databinding.FragmentLeftMenuBinding;
import com.example.tubespppb2.service.SharedPreferenceStore;

public class LeftMenuFragment extends Fragment implements View.OnClickListener{
    private FragmentLeftMenuBinding binding;
    private SharedPreferenceStore spStore;
    private FragmentManager fm;
    public LeftMenuFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentLeftMenuBinding.inflate(inflater);
        View view = this.binding.getRoot();
        this.spStore = new SharedPreferenceStore(getActivity().getApplicationContext());
        this.fm = getParentFragmentManager();
        this.binding.btnPengumuman.setOnClickListener(this::onClick);
        this.binding.btnPertemuan.setOnClickListener(this::onClick);
        this.binding.btnUndangan.setOnClickListener(this::onClick);
        this.binding.btnFrs.setOnClickListener(this::onClick);
        this.binding.btnPengaturan.setOnClickListener(this::onClick);
        this.binding.btnJadwalKosong.setOnClickListener(this::onClick);
        if(!this.spStore.getRole().equals("lecturer")){
            this.binding.btnJadwalKosong.setVisibility(View.GONE);
        }
        if(!this.spStore.getRole().equals("student")){
            this.binding.btnFrs.setVisibility(View.GONE);
        }

        if(this.spStore.getRole().equals("admin")){
            this.binding.akademiContainer.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == this.binding.btnPengumuman.getId()){
            Bundle bundle = new Bundle();
            bundle.putInt("page",2);
            this.fm.setFragmentResult("changePage",bundle);
        }else if(v.getId() == this.binding.btnPertemuan.getId()){
            Bundle bundle = new Bundle();
            bundle.putInt("page",3);
            this.fm.setFragmentResult("changePage",bundle);
        }else if(v.getId() == this.binding.btnUndangan.getId()){
            Bundle bundle = new Bundle();
            bundle.putInt("page",4);
            this.fm.setFragmentResult("changePage",bundle);
        }else if(v.getId() == this.binding.btnFrs.getId()){
            Bundle bundle = new Bundle();
            bundle.putInt("page",5);
            this.fm.setFragmentResult("changePage",bundle);
        }else if(v.getId() == this.binding.btnPengaturan.getId()){
            Bundle bundle = new Bundle();
            bundle.putInt("page",6);
            this.fm.setFragmentResult("changePage",bundle);
        }else if(v.getId() == this.binding.btnJadwalKosong.getId()){
            Bundle bundle = new Bundle();
            bundle.putInt("page",7);
            this.fm.setFragmentResult("changePage",bundle);
        }
    }
}
