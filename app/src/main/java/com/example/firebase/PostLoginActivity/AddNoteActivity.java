package com.example.firebase.PostLoginActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firebase.Models.NoteItem;
import com.example.firebase.R;
import com.example.firebase.databinding.ActivityAddNoteBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;

public class AddNoteActivity extends AppCompatActivity {
    private EditText label, textContent;
    final Calendar calendar = Calendar.getInstance();

    private FirebaseAuth mAuth;
    private FirebaseDatabase rootNode;
    DatabaseReference reference;

    private String userID;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mAuth = FirebaseAuth.getInstance();

        label = findViewById(R.id.label);
        textContent = findViewById(R.id.textContent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveBtn:
                sendDataToDatabase();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void sendDataToDatabase() {
        String labelValue = label.getText().toString().trim();
        String textContentValue = textContent.getText().toString().trim();

        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Users").child(userID).child("NoteItems");

        NoteItem noteItem = new NoteItem(labelValue, textContentValue);

        reference.child(labelValue).setValue(noteItem);

        Toast.makeText(AddNoteActivity.this, "Add successful", Toast.LENGTH_SHORT).show();

    }

}