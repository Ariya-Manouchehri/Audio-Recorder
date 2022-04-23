package com.example.android.audiorecorder;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordFragment extends Fragment implements View.OnClickListener {
    private ImageButton record_play_list;
    private Chronometer record_timer;
    private Button record_btn;
    private boolean isRecording = false;
    private ValueAnimator valueAnimator;
    private MediaRecorder recorder;
    private String path;

    public RecordFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_record, container, false);
        init(root_view);
        return root_view;
    }

    private void init(View view) {
        record_play_list = view.findViewById(R.id.play_list);
        record_timer = view.findViewById(R.id.timer);
        record_btn = view.findViewById(R.id.record_btn);
        path = getActivity().getExternalFilesDir("/").getAbsolutePath();

        record_btn.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.recordbtn_anim));

        record_btn.setOnClickListener(this);
        record_play_list.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_btn:
                if (isRecording) {
                    stopRecording();
                } else {
                    startRecording();
                }
                break;

            case R.id.play_list:
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.fade_out,R.anim.fade_in,R.anim.slide_out).replace(R.id.my_container,new PlaylistFragment(),"PlaylistFragment").addToBackStack(getTag()).commit();
                break;
        }
    }

    private void stopRecording() {
        valueAnimator.cancel();
        record_btn.setBackgroundColor(Color.rgb(0,150,136));
        record_btn.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.recordbtn_anim));
        recorder.stop();
        recorder.release();
        record_timer.stop();
        record_timer.setBase(SystemClock.elapsedRealtime());
        record_btn.setText("record");
        isRecording = false;
    }

    private void startRecording() {
        record_btn.getAnimation().cancel();
        record_timer.setBase(SystemClock.elapsedRealtime());
        record_btn.setText("stop");

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.US);
        Date now = new Date();
        String name = "Recording " + simpleDateFormat.format(now) + ".3gp";

        recorder.setOutputFile(path+"/"+name);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
            recorder.start();
            record_timer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), ContextCompat.getColor(getActivity(), R.color.red_200), ContextCompat.getColor(getActivity(), R.color.red_500), ContextCompat.getColor(getActivity(), R.color.red_700));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                record_btn.setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(1000);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
        isRecording = true;
    }

    @Override
    public void onStop() {
        super.onStop();
    if (recorder!=null && isRecording){
        recorder.stop();
        recorder.release();
        recorder=null;
    }
    }
}
