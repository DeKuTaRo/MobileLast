package com.example.firebase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText nameEvent, placeEvent, dateEvent, timeEvent;
    final Calendar calendar = Calendar.getInstance();
    private ProgressBar progressBar;
    private Button addBtn;

    private FirebaseAuth mAuth;
    private FirebaseDatabase rootNode;
    DatabaseReference reference;

    private String userID;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        nameEvent = findViewById(R.id.nameEvent);
        placeEvent = findViewById(R.id.placeEvent);
        dateEvent = findViewById(R.id.dateEvent);
        timeEvent = findViewById(R.id.timeEvent);
        addBtn = findViewById(R.id.addBtn);

        placeEvent.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(AddNoteActivity.this);
            dialog.setTitle("SELECT PLACE");
            dialog.setPositiveButton("OK", null);
            dialog.setNeutralButton("Cancel", null);

            String[] items = {"C201", "C202", "C203", "C204"};

            int checkItem = 1;

            dialog.setSingleChoiceItems(items, checkItem, (dialogInterface, i) -> {
                switch (i) {
                    case 0 :
                        placeEvent.setText(items[0]);
                        break;

                    case 1 :
                        placeEvent.setText(items[1]);
                        break;

                    case 2 :
                        placeEvent.setText(items[2]);
                        break;

                    case 3 :
                        placeEvent.setText(items[3]);
                        break;
                }
            });
            dialog.show();
        });

        DatePickerDialog.OnDateSetListener date = (datePicker, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateCalendar();
        };

        dateEvent.setOnClickListener(view ->
                new DatePickerDialog(AddNoteActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());

        //


        timeEvent.setOnClickListener(view -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(AddNoteActivity.this, (timePicker, selectedHour, selectedMinute) ->
                    timeEvent.setText( selectedHour + ":" + selectedMinute), hour, minute, true);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });


        addBtn.setOnClickListener(this);

    }

    @SuppressLint("WeekBasedYear")
    private void updateCalendar() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateEvent.setText(sdf.format(calendar.getTime()));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addBtn:
                sendDataToDatabase();
                break;
        }
    }

    private void sendDataToDatabase() {
        String nameValue = nameEvent.getText().toString().trim();
        String placeValue = placeEvent.getText().toString().trim();
        String dateValue = dateEvent.getText().toString().trim();
        String timeValue = timeEvent.getText().toString().trim();

        if (nameValue.isEmpty()) {
            nameEvent.setError("Name is required");
            nameEvent.requestFocus();
            return;
        }

        if (placeValue.isEmpty()) {
            placeEvent.setError("Place is required");
            placeEvent.requestFocus();
            return;
        }

        if (dateValue.isEmpty()) {
            dateEvent.setError("Date is required");
            dateEvent.requestFocus();
            return;
        }

        if (timeValue.isEmpty()) {
            timeEvent.setError("Time is required");
            timeEvent.requestFocus();
            return;
        }

        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        progressBar.setVisibility(View.VISIBLE);
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Users").child(userID).child("NoteItems");

        NoteItem noteItem = new NoteItem(nameValue, placeValue, dateValue, timeValue);

        reference.push().setValue(noteItem);

        Toast.makeText(AddNoteActivity.this, "Add successful", Toast.LENGTH_SHORT).show();
    }
}