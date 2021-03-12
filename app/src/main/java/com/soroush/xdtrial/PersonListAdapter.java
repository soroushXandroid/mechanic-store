package com.soroush.xdtrial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PersonListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private List<PersonModel> list;
    public PersonListAdapter(List<PersonModel> list){
        this.list = list;
    }

    public List<PersonModel> showSearchList(String searchText){

        List<PersonModel> newList = new ArrayList<>();
        for (PersonModel model : list){
            String name = model.getName();
            if (name.contains(searchText))
                newList.add(model);
        }
        //notifyDataSetChanged();
        return newList;

    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.model_person, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        PersonModel model = list.get(position);
        holder.initViews(model);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
