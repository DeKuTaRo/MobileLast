package com.example.firebase.PostLoginActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.firebase.CustomAdapter.CustomAdapter;
import com.example.firebase.Models.NoteItem;
import com.example.firebase.R;
import com.example.firebase.databinding.ActivityNoteBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NoteActivity extends AppCompatActivity {

    private ActivityNoteBinding binding;

    private FirebaseAuth mAuth;
    private String userID;
    private DatabaseReference databaseReference;

    // RecyclerView
    private List<NoteItem> list_NoteItem;
    private CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityNoteBinding.inflate(getLayoutInflater());
        View viewRoot = this.binding.getRoot();
        setContentView(viewRoot);

        Initialize();
        DatabaseSetup();
        SetupRecyclerView();
        SearchViewInputText();
    }

    private void DatabaseSetup() {
        this.mAuth = FirebaseAuth.getInstance();
        this.userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        this.databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("NoteItems");

        this.databaseReference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NoteItem noteItem = snapshot.getValue(NoteItem.class);
                if (noteItem != null) {
                    list_NoteItem.add(noteItem);
                    customAdapter.notifyDataSetChanged();
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NoteItem noteItem = snapshot.getValue(NoteItem.class);

                if (noteItem != null || list_NoteItem == null || list_NoteItem.isEmpty()) {
                    return;
                }
                for (int i = 0; i < list_NoteItem.size(); i++) {
                    if (noteItem.getId().equals(list_NoteItem.get(i).getId())) {
                        list_NoteItem.set(i, noteItem);
                        break;
                    }
                }
                customAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                NoteItem noteItem = snapshot.getValue(NoteItem.class);
                if (noteItem != null || list_NoteItem == null || list_NoteItem.isEmpty()) {
                    return;
                }
                for (int i = 0; i < list_NoteItem.size(); i++) {
                    if (noteItem.getId().equals(list_NoteItem.get(i).getId())) {
                        list_NoteItem.remove(list_NoteItem.get(i));
                        break;
                    }
                }
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void SetupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        this.binding.recycleView.setLayoutManager(layoutManager);
        this.binding.recycleView.setHasFixedSize(true);
        this.binding.recycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        this.binding.recycleView.setAdapter(this.customAdapter);
    }

    private void SearchViewInputText() {
        this.binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText) {
        List<NoteItem> noteItemList = new ArrayList<>();

        for (NoteItem noteItem : list_NoteItem) {
            if (noteItem.getLabel().toLowerCase().contains(newText.toLowerCase())) {
                noteItemList.add(noteItem);
            }
        }

        customAdapter.filterList(noteItemList);
    }

    private void Initialize() {
        this.list_NoteItem = new ArrayList<>();
        this.customAdapter = new CustomAdapter(this, this.list_NoteItem, new CustomAdapter.IItemClick() {
            @Override
            public void deleteItem(NoteItem noteItem) {
                onClickDeleteItem(noteItem);
            }
        });
    }

    private void onClickDeleteItem(NoteItem noteItem) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addBtn :
                Intent i = new Intent(this, AddNoteActivity.class);
                startActivity(i);
                break;
            case R.id.profileBtn :
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}