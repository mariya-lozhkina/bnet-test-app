package com.mariyalozjkina.bnettestapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import androidx.appcompat.app.AppCompatActivity;

public class EntryActivity extends AppCompatActivity {

    private TextView tvEntryText;
    private TextView tvCreatDate;
    private TextView tvModificationDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        tvEntryText = findViewById(R.id.tvEntryText);
        tvCreatDate = findViewById(R.id.tvCreatDate);
        tvModificationDate = findViewById(R.id.tvModificationDate);
        Intent intent = getIntent();
        String text = intent.getStringExtra(Constants.BODY);
        long da = intent.getLongExtra(Constants.DA, 0);
        long dm = intent.getLongExtra(Constants.DM, 0);
        tvEntryText.setText(text);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
        tvCreatDate.setText(dateFormat.format(da * 1000));
        tvModificationDate.setText(dateFormat.format(dm * 1000));
    }
}
