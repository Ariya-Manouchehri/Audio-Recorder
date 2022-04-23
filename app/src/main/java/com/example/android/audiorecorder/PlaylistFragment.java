package com.example.android.audiorecorder;

import android.os.Bundle;
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

public class PlaylistFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private BottomSheetBehavior behavior;
    private TextView record_name;
    private ImageButton play_pause;
    private SeekBar seekBar;
    private View bottom_sheet;
    private String path;
    private RecyclerViewAdapter adapter;
    private File directory;
    private File[] files;

    public PlaylistFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_playlist,container,false);
        return root_view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        play_pause.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
}
