package com.example.android.audiorecorder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RecordFragment extends Fragment implements View.OnClickListener {
    private ImageButton record_play_list;
    private Chronometer record_timer;
    private Button record_btn;
    private boolean isRecording = false;

    public RecordFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_record, container);
        return root_view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        record_play_list = view.findViewById(R.id.play_list);
        record_timer = view.findViewById(R.id.timer);
        record_btn = view.findViewById(R.id.record_btn);

        record_btn.setOnClickListener(this);
        record_play_list.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_btn:
                if (!isRecording) {
                    record_btn.setText("stop");
                    isRecording = true;
                } else {
                    record_btn.setText("record");
                    isRecording = false;
                }
                break;

            case R.id.play_list:

                break;
        }
    }
}
