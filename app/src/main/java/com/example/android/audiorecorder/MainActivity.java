package com.example.android.audiorecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQ_CODE = 100;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Bundle savedInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
savedInstance = savedInstanceState;
        checkPermission();
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this,permissions[0]) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,permissions[1]) == PackageManager.PERMISSION_GRANTED){
            setFragment();
        }else {
            ActivityCompat.requestPermissions(this,permissions,PERMISSION_REQ_CODE);
        }
    }

    private void setFragment() {
        if (savedInstance == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.my_container, new RecordFragment(), "RecordFragment").commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_CODE){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                setFragment();
            }
        }
    }
}