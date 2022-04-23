package com.example.android.audiorecorder;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PlaylistFragment extends Fragment implements View.OnClickListener, RecyclerViewAdapter.RecyclerViewOnItemClick, MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {
    private RecyclerView recyclerView;
    private BottomSheetBehavior behavior;
    private TextView record_name;
    private ImageButton play_pause;
    private SeekBar seekBar;
    private View bottom_sheet;
    private String path;
    private RecyclerViewAdapter adapter;
    private File directory;
    private MediaPlayer player;
    private File[] files;
    private File currentFile;
    private Handler handler = new Handler();
    private Timer timer;

    public PlaylistFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_playlist,container,false);
        init(root_view);
        return root_view;
    }

    private void init(View view) {
        path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        directory = new File(path);
        files = directory.listFiles();
        adapter = new RecyclerViewAdapter(files);

        recyclerView = view.findViewById(R.id.recyclerView_list);
        bottom_sheet = view.findViewById(R.id.bottom_sheet);
        record_name = view.findViewById(R.id.record_name);
        play_pause = view.findViewById(R.id.play_pause);
        seekBar = view.findViewById(R.id.seekbar);
        behavior = BottomSheetBehavior.from(bottom_sheet);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (player.isPlaying()){
            player.pause();
            play_pause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }else {
            player.start();
            play_pause.setImageResource(R.drawable.ic_baseline_pause_24);
        }
    }

    @Override
    public void onItemClickListener(File file, int Position) {
        currentFile = file;
        play_pause.setOnClickListener(this);
        if (player!=null && player.isPlaying()){
            player.stop();
        }
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        try {
            player.setDataSource(file.getAbsolutePath());
            player.prepare();
            player.start();
            play_pause.setImageResource(R.drawable.ic_baseline_pause_24);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            record_name.setText(file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        seekBar.setMax(player.getDuration());
        seekBar.setOnSeekBarChangeListener(this);

        if (timer == null){
            timer = new Timer();
        }
        timer.schedule(new MyTimerTask(),0,500);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.stop();
        mp.reset();
        play_pause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        try {
            mp.setDataSource(currentFile.getAbsolutePath());
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
            player.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    seekBar.setProgress(player.getCurrentPosition());
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player !=null && timer==null && player.isPlaying()){
        player.stop();
        player.release();
        player = null;
        timer.purge();
        timer.cancel();
        }
    }
}
