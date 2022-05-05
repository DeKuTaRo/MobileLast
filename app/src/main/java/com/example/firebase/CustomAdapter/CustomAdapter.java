package com.example.firebase.CustomAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase.Models.NoteItem;
import com.example.firebase.PostLoginActivity.UpdateActivity;
import com.example.firebase.databinding.ActivityRecyclerViewItemNoteItemBinding;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private final Context context;
    private final List<NoteItem> dataSource;

    public CustomAdapter(Context context, List<NoteItem> dataSource) {
        this.context = context;
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityRecyclerViewItemNoteItemBinding viewRoot = ActivityRecyclerViewItemNoteItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(viewRoot);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindData(this.dataSource.get(position), position);
    }

    @Override
    public int getItemCount() {
        return this.dataSource.size();
    }

    protected static class MyViewHolder extends RecyclerView.ViewHolder {

        private final ActivityRecyclerViewItemNoteItemBinding binding;

        public MyViewHolder(@NonNull ActivityRecyclerViewItemNoteItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        private void bindData(NoteItem noteItem, int position){
            this.binding.nameEvent.setText(noteItem.getName());
            this.binding.placeEvent.setText(noteItem.getPlace());
            this.binding.dateEvent.setText(noteItem.getDate());
            this.binding.timeEvent.setText(noteItem.getTime());

            this.binding.mainCardView.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), UpdateActivity.class);
                intent.putExtra("name", String.valueOf(noteItem.getName()));
                intent.putExtra("place", String.valueOf(noteItem.getPlace()));
                intent.putExtra("date", String.valueOf(noteItem.getDate()));
                intent.putExtra("time", String.valueOf(noteItem.getTime()));

                view.getContext().startActivity(intent);
//            Toast.makeText(context, holder.nameEvent.getText().toString(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
