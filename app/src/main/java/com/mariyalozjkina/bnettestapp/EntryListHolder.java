package com.mariyalozjkina.bnettestapp;

import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class EntryListHolder extends RecyclerView.ViewHolder {

    private CardView cvRoot;
    private TextView tvCreatDate;
    private TextView tvModificationDate;
    private TextView tvEntryText;

    public EntryListHolder(@NonNull View itemView) {
        super(itemView);
        tvCreatDate = itemView.findViewById(R.id.tvCreatDate);
        tvModificationDate = itemView.findViewById(R.id.tvModificationDate);
        tvEntryText = itemView.findViewById(R.id.tvEntryText);
        cvRoot = itemView.findViewById(R.id.cvRoot);
    }

    public void bind(final Entry entry, final SelectEntryListener selectEntryListener) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
        tvCreatDate.setText(dateFormat.format(entry.da * 1000));
        tvModificationDate.setText(dateFormat.format(entry.dm * 1000));
        tvEntryText.setText(entry.body);
        cvRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectEntryListener.selectEntry(entry);
            }
        });

    }
}
