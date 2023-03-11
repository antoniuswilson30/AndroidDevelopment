package com.example.tubespppb2.view.announcement;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tubespppb2.R;
import com.example.tubespppb2.databinding.ItemListAnnouncementFilterBinding;
import com.example.tubespppb2.pojo.TagGetResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnnouncementFilterListAdapter extends BaseAdapter {
    private List<TagsWrapper> list;
    private List<TagsWrapper> listAll;
    private List<TagsWrapper> selected;
    private Activity activity;
    private HashMap<String, String> mp;
    public AnnouncementFilterListAdapter(Activity activity, HashMap<String,String> mp){
        this.list = new ArrayList<>();
        this.listAll = new ArrayList<>();
        this.selected = new ArrayList<>();
        this.activity = activity;
        this.mp = mp;
    }

    public void search(String tag){
        if(tag != null && !tag.trim().isEmpty()) {
            list.clear();
            for (int i = 0; i < listAll.size(); i++) {
                if (listAll.get(i).response.tag.toLowerCase().contains(tag)) {
                    list.add(listAll.get(i));
                }
            }
        }else{
            list.clear();
            list.addAll(listAll);
        }
        notifyDataSetChanged();
    }

    public void loadTag(List<TagGetResponse> data){
        selected.clear();
        list.clear();
        listAll.clear();
        for(int i = 0; i < data.size(); i++){
            if(mp.containsKey(data.get(i).id)){
                TagsWrapper temp = new TagsWrapper(data.get(i),selected.size(),true);
                selected.add(temp);
                listAll.add(temp);
                list.add(temp);
            }else {
                TagsWrapper temp = new TagsWrapper(data.get(i), i, false);
                listAll.add(temp);
                list.add(temp);
            }
        }
        notifyDataSetChanged();
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
        ItemListAnnouncementFilterBinding binding = ItemListAnnouncementFilterBinding.inflate(activity.getLayoutInflater());
        ViewHolder viewHolder = new ViewHolder(binding,position);
        View itemView = binding.getRoot();
        viewHolder.updateView();
        return itemView;
    }

    public ArrayList<TagGetResponse> getFilter(){
        ArrayList <TagGetResponse> temp = new ArrayList<>();
        for(int i = 0; i < selected.size(); i++){
            temp.add(selected.get(i).response);
        }
        return temp;
    }

    private class ViewHolder{
        private ItemListAnnouncementFilterBinding binding;
        private int idx;

        public ViewHolder(ItemListAnnouncementFilterBinding binding, int position){
            this.binding = binding;
            this.idx = position;
            this.binding.btnAdd.setOnClickListener(this::onClick);
        }

        private void onClick(View view) {
            list.get(idx).toggleStatus();
            if(list.get(idx).status == true){
                list.get(idx).idxSelected = selected.size();
                selected.add(list.get(idx));
            }else{
                int idxSelected = list.get(idx).idxSelected;
                selected.remove(idxSelected);
                for(int i = idxSelected; i < selected.size(); i++){
                    selected.get(i).idxSelected--;
                }
            }
            notifyDataSetChanged();
        }

        private void updateView(){
            this.binding.tvTag.setText(list.get(idx).response.tag);
            if(list.get(idx).status == false){
                this.binding.btnAdd.setImageResource(R.drawable.ic_baseline_add_24);
                this.binding.btnAdd.setBackgroundColor(activity.getResources().getColor(R.color.birulebihmuda));
            }else{
                this.binding.btnAdd.setImageResource(R.drawable.ic_remove_fill0_wght400_grad0_opsz48);
                this.binding.btnAdd.setBackgroundColor(activity.getResources().getColor(R.color.merah));
            }
        }
    }

    private class TagsWrapper{
        private TagGetResponse response;
        private int idxSelected;
        private boolean status;
        public TagsWrapper(TagGetResponse response, int idx, boolean status){
            this.idxSelected=idx;
            this.response = response;
            this.status = status;
        }

        public void toggleStatus(){
            this.status = !status;
        }
    }
}
