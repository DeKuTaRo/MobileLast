package com.example.firebase.PostLoginActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    }

    private void DatabaseSetup() {
        this.mAuth = FirebaseAuth.getInstance();
        this.userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        this.databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("NoteItems");

        this.databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NoteItem noteItem = dataSnapshot.getValue(NoteItem.class);
                    list_NoteItem.add(noteItem);
                }

                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void SetupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        this.binding.recycleViewNoteItem.setLayoutManager(layoutManager);
        this.binding.recycleViewNoteItem.setHasFixedSize(true);
        this.binding.recycleViewNoteItem.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        this.binding.recycleViewNoteItem.setAdapter(this.customAdapter);
    }

    private void Initialize() {
        this.list_NoteItem = new ArrayList<>();
        this.customAdapter = new CustomAdapter(this, this.list_NoteItem);
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
            case R.id.searchBtn :
                Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
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