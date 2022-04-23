package com.example.android.audiorecorder;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
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

public class RecordFragment extends Fragment implements View.OnClickListener {
    private ImageButton record_play_list;
    private Chronometer record_timer;
    private Button record_btn;
    private boolean isRecording = false;
    private ValueAnimator valueAnimator;

    public RecordFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_record, container, false);
        return root_view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        record_play_list = view.findViewById(R.id.play_list);
        record_timer = view.findViewById(R.id.timer);
        record_btn = view.findViewById(R.id.record_btn);

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
        record_timer.stop();
        record_timer.setBase(SystemClock.elapsedRealtime());
        record_btn.setText("record");
        isRecording = false;
    }

    private void startRecording() {
        record_btn.getAnimation().cancel();
        record_timer.setBase(SystemClock.elapsedRealtime());
        record_timer.start();
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
        record_btn.setText("stop");
        isRecording = true;
    }
}
