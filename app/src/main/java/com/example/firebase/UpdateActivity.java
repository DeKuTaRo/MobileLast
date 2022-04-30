package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEventUpdate, placeEventUpdate, dateEventUpdate, timeEventUpdate;
    private Button updateBtn, deleteBtn;
    DatabaseReference reference;

    private FirebaseUser user;
    private String userID;

    private String id, name, place, date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        nameEventUpdate = findViewById(R.id.nameEventUpdate);
        placeEventUpdate = findViewById(R.id.placeEventUpdate);
        dateEventUpdate = findViewById(R.id.dateEventUpdate);
        timeEventUpdate = findViewById(R.id.timeEventUpdate);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        getAndSetIntentData();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NoteItem noteItem = snapshot.getValue(NoteItem.class);

                if (noteItem != null) {
                    String name = noteItem.name;
                    String place = noteItem.place;
                    String date = noteItem.date;
                    String time = noteItem.time;

                    nameEventUpdate.setText(name);
                    placeEventUpdate.setText(place);
                    dateEventUpdate.setText(date);
                    timeEventUpdate.setText(time);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    private void getAndSetIntentData() {
        if (getIntent().hasExtra("name") &&
                getIntent().hasExtra("place") && getIntent().hasExtra("date") &&
                getIntent().hasExtra("time")) {

            // Getting data from intent
//            id = getIntent().getStringExtra("id");

            name = getIntent().getStringExtra("name");
            place = getIntent().getStringExtra("place");
            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");

            // Setting data intent
            nameEventUpdate.setText(name);
            placeEventUpdate.setText(place);
            dateEventUpdate.setText(date);
            timeEventUpdate.setText(time);

        } else {
            Toast.makeText(this, "No data change", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateBtn:
                updateDate();
                break;
            case R.id.deleteBtn:
                deleteItem();
                break;
        }
    }

    private void updateDate() {

        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("NoteItems");

        String name = nameEventUpdate.getText().toString().trim();
        String place = placeEventUpdate.getText().toString().trim();
        String date = dateEventUpdate.getText().toString().trim();
        String time = timeEventUpdate.getText().toString().trim();

        HashMap User = new HashMap();
        User.put("name", name);
        User.put("place", place);
        User.put("date", date);
        User.put("time", time);

        reference.updateChildren(User).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                Toast.makeText(UpdateActivity.this, "Updated successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UpdateActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void deleteItem() {
        Toast.makeText(UpdateActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
    }
}