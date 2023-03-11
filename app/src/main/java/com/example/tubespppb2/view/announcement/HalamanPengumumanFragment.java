package com.example.tubespppb2.view.announcement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import com.example.tubespppb2.IError;
import com.example.tubespppb2.MainActivity;
import com.example.tubespppb2.R;
import com.example.tubespppb2.databinding.FragmentHalamanPengumumanBinding;
import com.example.tubespppb2.pojo.AnnouncementGetDaftarResponse;
import com.example.tubespppb2.pojo.AnnouncementGetResponse;
import com.example.tubespppb2.pojo.TagGetResponse;
import com.example.tubespppb2.presenter.PresenterMainActivityAnnouncement;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HalamanPengumumanFragment extends Fragment implements IError, IAnnouncement, ITag, View.OnClickListener {
    private FragmentHalamanPengumumanBinding binding;
    private PresenterMainActivityAnnouncement presenterMainActivityAnnouncement;
    private AnnouncementListAdapter adapter;
    private String title;
    private FilterTagDialogFragment dialogFragmentFilter;
    private TambahPengumumanDialogFragment dialogFragmentAddPengumuman;
    private SharedPreferenceStore spStore;
    private FragmentManager fm;
    private List<String> tags;
    private HashMap<String, String> mp;
    private boolean isFilter;
    private ArrayList<TagGetResponse> filter;
    private HalamanPengumumanFragment(){}

    public static HalamanPengumumanFragment newInstance(PresenterMainActivityAnnouncement presenterMainActivityAnnouncement) {
        HalamanPengumumanFragment fragment = new HalamanPengumumanFragment();
        fragment.presenterMainActivityAnnouncement = presenterMainActivityAnnouncement;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentHalamanPengumumanBinding.inflate(inflater);
        View view = this.binding.getRoot();
        this.adapter = new AnnouncementListAdapter(getActivity(),getParentFragmentManager(),this.presenterMainActivityAnnouncement);
        this.binding.listPengumuman.setAdapter(this.adapter);
        this.mp = new HashMap<>();
        this.spStore = new SharedPreferenceStore(getActivity().getApplicationContext());
        this.fm = getParentFragmentManager();
        this.tags = new ArrayList<>();
        this.filter = new ArrayList<>();
        this.isFilter = false;
        this.presenterMainActivityAnnouncement.loadData(null,null,null,"5");
        this.binding.btnPageBack.setOnClickListener(this::onClick);
        this.binding.btnPageNext.setOnClickListener(this::onClick);
        this.binding.btnFilter.setOnClickListener(this::onClick);
        this.binding.btnTambahPengumuman.setOnClickListener(this::onClick);
        if(this.spStore.getRole().equals("student")||this.spStore.getRole().equals("lecturer")){
            this.binding.btnTambahPengumuman.hide();
        }else{
            this.binding.btnTambahPengumuman.show();
        }
        setHasOptionsMenu(true);

        this.fm.setFragmentResultListener(
                "filter", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        filter.clear();
                        filter.addAll(result.getParcelableArrayList("filter"));
                        clearChips();
                        if(filter.size() > 0) {
                            mp.clear();
                            tags.clear();
                            for(int i = 0; i < filter.size(); i++){
                                mp.put(filter.get(i).id,"");
                                tags.add(filter.get(i).id);
                                addChips(filter.get(i).tag);
                            }
                        }else{
                            mp.clear();
                            tags.clear();
                        }
                        adapter.clearData();
                        presenterMainActivityAnnouncement.loadData(title,tags,null,"5");
                    }
                }
        );
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((MainActivity) getContext()).getSupportActionBar().getThemedContext());
        item.setShowAsAction((MenuItem.SHOW_AS_ACTION_ALWAYS));
        item.setActionView(searchView);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                title = query;
                adapter.clearData();
                presenterMainActivityAnnouncement.loadData(query,tags,null,"5");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && newText.trim().isEmpty()){
                    title = null;
                    adapter.clearData();
                    presenterMainActivityAnnouncement.loadData(null,tags,null,"5");
                }
                return false;
            }
        });
    }



    @Override
    public void addData(AnnouncementGetDaftarResponse data) {
        this.adapter.addData(data);
    }

    @Override
    public void showDetail(AnnouncementGetResponse data) {
        this.adapter.showDialog(data);
    }

    @Override
    public void makeAnnouncementSuccess() {
        this.dialogFragmentAddPengumuman.dismiss();
    }

    @Override
    public void showError(String msg) {
        CharSequence text = msg;
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, duration);
        toast.show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == this.binding.btnPageBack.getId()){
            this.adapter.before();
        }else if(v.getId() == this.binding.btnPageNext.getId()){
            this.adapter.next(title,tags);
        }else if(v.getId() == this.binding.btnFilter.getId()){
            this.isFilter = true;
            this.dialogFragmentFilter = FilterTagDialogFragment.newInstance(presenterMainActivityAnnouncement,getActivity(),this.fm,this.mp);
            this.tags.clear();
            dialogFragmentFilter.show(getParentFragmentManager(), "show dialog");
        }else if(v.getId() == this.binding.btnTambahPengumuman.getId()){
            this.isFilter = false;
            this.dialogFragmentAddPengumuman = TambahPengumumanDialogFragment.newInstance(this.presenterMainActivityAnnouncement,getActivity(),this.fm);
            dialogFragmentAddPengumuman.show(this.fm,"dialog");
        }
    }

    @Override
    public void loadTag(List<TagGetResponse> data) {
        if(isFilter) {
            this.dialogFragmentFilter.loadTag(data);
        }else{
            this.dialogFragmentAddPengumuman.loadTag(data);
        }
    }
    private void addChips(String chips) {
        ChipGroup chipGroup = this.binding.chipPartisipan;
        Chip chip = new Chip(getContext());
        chip.setText(chips);
        chip.setCloseIconVisible(true);
        chip.setClickable(false);
        chipGroup.addView(chip);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < filter.size(); i++) {
                    if (filter.get(i).tag.equals(((Chip) v).getText().toString())) {
                        String id = filter.get(i).id;
                        mp.remove(filter.get(i).id);
                        filter.remove(i);
                        for(int j = 0; j < tags.size(); j++){
                            if(tags.get(j).equals(id)){
                                tags.remove(j);
                                break;
                            }
                        }
                        chipGroup.removeView(v);
                        adapter.clearData();
                        presenterMainActivityAnnouncement.loadData(title,tags,null,"5");
                        break;
                    }
                }
            }
        });
    }
    private void clearChips(){
        ChipGroup chipGroup = this.binding.chipPartisipan;
        chipGroup.removeAllViews();
    }
}
