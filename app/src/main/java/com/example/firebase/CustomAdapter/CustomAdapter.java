package com.example.firebase.CustomAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase.Models.NoteItem;
import com.example.firebase.PostLoginActivity.UpdateActivity;
import com.example.firebase.databinding.ActivityRecyclerViewItemNoteItemBinding;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private final Context context;
    private List<NoteItem> dataSource;

    private IItemClick itemClick;

    public interface IItemClick {
        void deleteItem(NoteItem noteItem);
    }

    public CustomAdapter(Context context, List<NoteItem> dataSource, IItemClick itemClick) {
        this.context = context;
        this.dataSource = dataSource;
        this.itemClick = itemClick;
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

    protected class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,View.OnClickListener{

        private final ActivityRecyclerViewItemNoteItemBinding binding;

        public MyViewHolder(@NonNull ActivityRecyclerViewItemNoteItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

            this.binding.mainCardView.setOnCreateContextMenuListener(this);
            this.itemView.setOnClickListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(this.getAdapterPosition(), 121, 0, "Hide");
            contextMenu.add(this.getAdapterPosition(), 122, 1, "Pin");
            contextMenu.add(this.getAdapterPosition(), 123, 3, "Share with");
        }

        @Override
        public void onClick(View view) {
            NoteItem noteItem = dataSource.get(getAdapterPosition());
            Intent intent = new Intent(context, UpdateActivity.class);
            intent.putExtra("noteItems", noteItem);
            context.startActivity(intent);
        }

        private void bindData(NoteItem noteItem, int position){
            Date currentTime = Calendar.getInstance().getTime();

            this.binding.label.setText(noteItem.getLabel());
            this.binding.textContent.setText(noteItem.getTextContent());
            this.binding.timeCreate.setText(currentTime.toString());
            this.binding.deleteBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    itemClick.deleteItem(noteItem);
                }
            });
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<NoteItem> noteItemArrayList) {
        dataSource = noteItemArrayList;
        notifyDataSetChanged();
    }
}
