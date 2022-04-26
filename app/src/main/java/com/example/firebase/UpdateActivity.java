package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UpdateActivity extends AppCompatActivity {

    private EditText nameEventUpdate, placeEventUpdate, dateEventUpdate, timeEventUpdate;
    private Button updateBtn;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        nameEventUpdate = findViewById(R.id.nameEventUpdate);
        placeEventUpdate = findViewById(R.id.placeEventUpdate);
        dateEventUpdate = findViewById(R.id.dateEventUpdate);
        timeEventUpdate = findViewById(R.id.timeEventUpdate);
        updateBtn = findViewById(R.id.updateBtn);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEventUpdate.getText().toString().trim();
                String place = placeEventUpdate.getText().toString().trim();
                String date = dateEventUpdate.getText().toString().trim();
                String time = timeEventUpdate.getText().toString().trim();

                updateDate(name, place, date, time);
            }
        });
    }

    private void updateDate(String name, String place, String date, String time) {
        HashMap User = new HashMap();
        User.put("name", name);
        User.put("place", place);
        User.put("date", date);
        User.put("time", time);

        reference = FirebaseDatabase.getInstance().getReference().child("NoteItems");
        reference.updateChildren(User).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if (task.isSuccessful()) {
                    Toast.makeText(UpdateActivity.this, "Updated successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdateActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}