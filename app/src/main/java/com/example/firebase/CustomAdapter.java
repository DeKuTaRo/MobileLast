package com.example.firebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;
    ArrayList<NoteItem> list;
    Activity activity;

    public CustomAdapter(Context context, ArrayList<NoteItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_get_listview, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NoteItem noteItem = list.get(position);
        holder.nameEvent.setText(noteItem.getName());
        holder.placeEvent.setText(noteItem.getPlace());
        holder.dateEvent.setText(noteItem.getDate());
        holder.timeEvent.setText(noteItem.getTime());
        holder.updateBtn.setOnClickListener(view -> {
            Intent intent = new Intent(context, UpdateActivity.class);
            activity.startActivityForResult(intent, 1);
//            Toast.makeText(context, holder.nameEvent.getText().toString(), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameEvent, placeEvent, dateEvent, timeEvent;
        CardView mainCardView;
        Button updateBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameEvent = itemView.findViewById(R.id.nameEvent);
            placeEvent = itemView.findViewById(R.id.placeEvent);
            dateEvent = itemView.findViewById(R.id.dateEvent);
            timeEvent = itemView.findViewById(R.id.timeEvent);
            updateBtn = itemView.findViewById(R.id.updateBtn);
            mainCardView = itemView.findViewById(R.id.mainCardView);


        }
    }
}
