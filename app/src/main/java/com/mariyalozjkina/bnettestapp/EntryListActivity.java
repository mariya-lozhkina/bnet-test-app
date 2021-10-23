package com.mariyalozjkina.bnettestapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EntryListActivity extends AppCompatActivity {

    private RecyclerView rvEntrys;
    private EntryAdapter entryAdapter;
    private TextView tvCreateEntry;
    private Handler handler = new Handler();
    private String session;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadEntries();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_list);
        startNewSession();

        rvEntrys = findViewById(R.id.rvEntrys);
        initList();

        entryAdapter.notifyDataSetChanged();
        tvCreateEntry = findViewById(R.id.tvCreateEntry);
        tvCreateEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateEntry();
            }
        });
    }

    private void openCreateEntry() {
        Intent intent = new Intent(this, CreateEntryActivity.class);
        intent.putExtra(Constants.SESSION, session);
        startActivityForResult(intent, 1);
    }

    private void initList() {
        entryAdapter = new EntryAdapter(new SelectEntryListener() {
            @Override
            public void selectEntry(Entry entry) {
                openEntry(entry);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvEntrys.setAdapter(entryAdapter);
        rvEntrys.setLayoutManager(layoutManager);
    }

    /**
     * Здесь мы
     * @param entry
     */
    private void openEntry(Entry entry) {
        Intent intent = new Intent(this,EntryActivity.class);
        intent.putExtra(Constants.BODY, entry.body);
        intent.putExtra(Constants.DA, entry.da);
        intent.putExtra(Constants.DM, entry.da);
        startActivity(intent);
    }

    private void setSession(String session) {
        this.session = session;
    }

    private void startNewSession() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String newSessionJson = getNewSession();
                NewSessionResponse response = new Gson().fromJson(newSessionJson, NewSessionResponse.class);
                final String session = response.data.session;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setSession(session);
                    }
                });
            }
        }).start();
    }

    private String getNewSession() {
        String url = Constants.URL;
        int timeout = 5000;
        Map<String, String> params = new HashMap<>();
        params.put("a", "new_session");

        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("POST");
            c.setRequestProperty("token", Constants.TOKEN);
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            c.setDoOutput(true);
            c.getOutputStream().write(postDataBytes);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    String result = sb.toString();
                    System.out.println(result);
                    return result;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    private void loadEntries() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String getEntriesJson = requestGetEntries();
                GetEntriesResponse response = new Gson().fromJson(getEntriesJson, GetEntriesResponse.class);
                final ArrayList<Entry> entries = response.data.get(0);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        entryAdapter.setItems(entries);
                        entryAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }


    private String requestGetEntries() {
        String url = Constants.URL;
        int timeout = 5000;
        Map<String, String> params = new HashMap<>();
        params.put("a", "get_entries");
        params.put("session", session);

        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("POST");
            c.setRequestProperty("token", Constants.TOKEN);
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            c.setDoOutput(true);
            c.getOutputStream().write(postDataBytes);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    String result = sb.toString();
                    System.out.println(result);
                    return result;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

}