package com.mariyalozjkina.bnettestapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EntryAdapter extends RecyclerView.Adapter<EntryListHolder> {

    private ArrayList<Entry> items = new ArrayList<>();
    private SelectEntryListener selectEntryListener;

    public EntryAdapter(SelectEntryListener selectEntryListener) {
        this.selectEntryListener = selectEntryListener;
    }

    @NonNull
    @Override
    public EntryListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_entry_list, parent, false);
        return new EntryListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryListHolder holder, int position) {
        holder.bind(items.get(position), selectEntryListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Entry> items) {
        this.items = items;
    }
}
