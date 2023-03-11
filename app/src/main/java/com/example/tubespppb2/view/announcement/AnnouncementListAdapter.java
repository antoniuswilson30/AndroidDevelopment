package com.example.tubespppb2.view.announcement;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.tubespppb2.databinding.ItemListPengumumanBinding;
import com.example.tubespppb2.pojo.AnnouncementGetDaftarResponse;
import com.example.tubespppb2.pojo.AnnouncementGetResponse;
import com.example.tubespppb2.pojo.Data;
import com.example.tubespppb2.presenter.PresenterMainActivityAnnouncement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnnouncementListAdapter extends BaseAdapter {
    private List<Data> list;
    private List<Data> listAll;
    private Activity activity;
    private FragmentManager fm;
    private String metadataNext;
    private int max;
    private int now;
    private String limit;
    private PresenterMainActivityAnnouncement presenter;
    private boolean isDataExist;
    public AnnouncementListAdapter(Activity activity, FragmentManager fm, PresenterMainActivityAnnouncement presenter){
        this.list = new ArrayList<>();
        this.listAll = new ArrayList<>();
        this.activity = activity;
        this.fm = fm;
        this.presenter = presenter;
        this.max = 0;
        this.now = 0;
        this.limit = "5";
        this.isDataExist = false;
    }

    public void addData(AnnouncementGetDaftarResponse data){
            this.max++;
            this.now++;
            this.metadataNext = data.metadata.next;
            Data[] arr = data.data;
            listAll.addAll(Arrays.asList(arr));
            list.clear();
            list.addAll(Arrays.asList(arr));
            this.isDataExist = true;
            notifyDataSetChanged();
    }

    public void clearData(){
        this.now = 0;
        this.max = 0;
        this.metadataNext = null;
        this.isDataExist = false;
        this.list.clear();
        this.listAll.clear();
    }

    public void next(String title,List<String> tags){
        if(isDataExist) {
            if (now == max) {
                if (metadataNext != null) {
                    this.presenter.loadData(title, tags, metadataNext, limit);
                } else {
                    CharSequence text = "halaman sudah habis";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(activity.getApplicationContext(), text, duration);
                    toast.show();
                }
            } else {
                now++;
                int start = (now - 1) * Integer.parseInt(limit);
                list.clear();
                for (int i = start; i < start + Integer.parseInt(limit); i++) {
                    if (i < listAll.size()) {
                        list.add(listAll.get(i));
                    }
                }
                notifyDataSetChanged();
            }
        }
    }

    public void before(){
        if(isDataExist) {
            if (now == 1) {
                CharSequence text = "halaman sudah habis";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(activity.getApplicationContext(), text, duration);
                toast.show();
            } else {
                now--;
                int start = (now - 1) * Integer.parseInt(limit);
                list.clear();
                for (int i = start; i < start + Integer.parseInt(limit); i++) {
                    list.add(listAll.get(i));
                }
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemListPengumumanBinding binding = ItemListPengumumanBinding.inflate(activity.getLayoutInflater());
        ViewHolder viewHolder = new ViewHolder(binding,position);
        View itemView = binding.getRoot();
        viewHolder.updateView();
        return itemView;
    }

    private class ViewHolder{
        private ItemListPengumumanBinding binding;
        private int idx;

        public ViewHolder(ItemListPengumumanBinding binding, int position){
            this.binding = binding;
            this.idx = position;
            this.binding.pengumumanContainer.setOnClickListener(this::onClick);
        }

        private void onClick(View view) {
            String id = list.get(this.idx).id;
            presenter.loadDetail(id);
        }

        private void updateView(){
            String tags = "Tags: ";
            String tanggal = list.get(idx).created_at.substring(0,10);
            for(int i = 0; i < list.get(idx).tags.length; i++){
                if(i > 0) {
                    tags+=", ";
                }
                tags += list.get(idx).tags[i].tag;
            }
            String title = list.get(idx).title;
            if(title.length() >= 18){
                title = title.substring(0,18);
                title +="...";
            }
            this.binding.tvTitle.setText(title);
            this.binding.tvTag.setText(tags);
            this.binding.tvTanggal.setText(tanggal);
        }
    }

    public void showDialog(AnnouncementGetResponse data){
        DetailPengumumanDialogFragment dialogFragment = DetailPengumumanDialogFragment.newInstance(presenter,data.title,data.content,data.tags,data.id);
        dialogFragment.show(fm, "show dialog");
    }
}
