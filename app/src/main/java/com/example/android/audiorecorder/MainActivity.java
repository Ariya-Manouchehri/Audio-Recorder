package com.example.android.audiorecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.my_container,new RecordFragment(),"RecordFragment").commit();
        }
    }
}