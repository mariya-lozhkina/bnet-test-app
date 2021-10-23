package com.mariyalozjkina.bnettestapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class CreateEntryActivity extends AppCompatActivity {

    private EditText etTextField;
    private TextView tvSave;
    private TextView tvCancel;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);
        final Intent intent = getIntent();
        etTextField = findViewById(R.id.etTextField);
        tvSave = findViewById(R.id.tvSave);
        tvCancel = findViewById(R.id.tvCancel);

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void addEntry() {
        Intent intent = getIntent();
        final String session = intent.getStringExtra(Constants.SESSION);
        final String body = etTextField.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String requestAddEntryJson = requestAddEntry(session, body);
                final AddEntryResponse response = new Gson().fromJson(requestAddEntryJson, AddEntryResponse.class);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.status == 1) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                });
            }
        }).start();
    }

    private String requestAddEntry(String session, String body) {
        String url = Constants.URL;
        int timeout = 5000;
        Map<String, String> params = new HashMap<>();
        params.put("a", "add_entry");
        params.put("session", session);
        params.put("body", body);

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
