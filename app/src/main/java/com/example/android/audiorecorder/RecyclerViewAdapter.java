package com.example.android.audiorecorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private RecyclerViewOnItemClick listener;
    private File[] voices;

    public RecyclerViewAdapter(File[] voices) {
        this.voices = voices;
    }

    interface RecyclerViewOnItemClick{
        void onItemClickListener(File file, int Position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.record_name.setText(voices[position].getName());
        String date = new SimpleDateFormat("yyyy/MM/dd-hh:mm:ss", Locale.US).format(new Date(voices[position].lastModified()));

        holder.record_date.setText(date);
    }

    @Override
    public int getItemCount() {
        return voices.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView record_name, record_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            record_name = itemView.findViewById(R.id.record_name);
            record_date = itemView.findViewById(R.id.record_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() != RecyclerView.NO_POSITION){
                        listener.onItemClickListener(voices[getAdapterPosition()],getAdapterPosition());
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(RecyclerViewOnItemClick listener){
        this.listener = listener;
    }
}
