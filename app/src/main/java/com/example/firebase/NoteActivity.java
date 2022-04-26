package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ImageView empty_icon;
    TextView textViewNoData;

    private FirebaseAuth mAuth;

    DatabaseReference databaseReference;
    ArrayList<NoteItem> list;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recycleView);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("NoteItems");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        empty_icon = findViewById(R.id.empty_icon);
        textViewNoData = findViewById(R.id.textViewNoData);

        list = new ArrayList<>();
        customAdapter = new CustomAdapter(this, list);
        recyclerView.setAdapter(customAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NoteItem noteItem = dataSnapshot.getValue(NoteItem.class);
                    list.add(noteItem);
                }

                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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