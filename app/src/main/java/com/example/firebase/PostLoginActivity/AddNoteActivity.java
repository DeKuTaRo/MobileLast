package com.example.firebase.PostLoginActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private ActivityAddNoteBinding binding;

    private final String[] LIST_EVENT_PLACE = { "A002", "C001", "Hội trường 10A", "A006", "C504", "D10" };
    private int event_place_selected_index = -1;

    private FirebaseAuth mAuth;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private String userID;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        View viewRoot = this.binding.getRoot();
        setContentView(viewRoot);

        databaseSetup();
        addListener();
    }

    private void databaseSetup() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    private void addListener() {
        Objects.requireNonNull(this.binding.placeEvent).setOnClickListener(this);
        Objects.requireNonNull(this.binding.dateEvent).setOnClickListener(this);
        Objects.requireNonNull(this.binding.timeEvent).setOnClickListener(this);

        this.binding.placeEvent.setOnFocusChangeListener(this);
        this.binding.dateEvent.setOnFocusChangeListener(this);
        this.binding.timeEvent.setOnFocusChangeListener(this);

        this.binding.addBtn.setOnClickListener(this);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.placeEvent:
                showPlaceDialog();
                break;
            case R.id.dateEvent:
                showDatePicker();
                break;
            case R.id.timeEvent:
                showTimePicker();
                break;
            case R.id.addBtn:
                sendDataToDatabase();
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if ( !hasFocus ){
            return;
        }

        switch (view.getId()) {
            case R.id.placeEvent:
                showPlaceDialog();
                break;
            case R.id.dateEvent:
                showDatePicker();
                break;
            case R.id.timeEvent:
                showTimePicker();
                break;
            default:
                // Error
                break;
        }
    }

    // private utilities method
    //    Use static to save previous time picker
    private static int save_HourOfDay = -1;
    private static int save_Minute = -1;

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        int hour;
        int minute;

        hour = AddNoteActivity.save_HourOfDay == -1 ? calendar.get(Calendar.HOUR_OF_DAY) : AddNoteActivity.save_HourOfDay;
        minute = AddNoteActivity.save_Minute == -1 ? calendar.get(Calendar.MINUTE) : AddNoteActivity.save_Minute;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hourOfDay, minute1) -> {
            @SuppressLint("DefaultLocale") String eventDate = String.format("%02d", hourOfDay) + ':' +String.format("%02d", minute1);
            Objects.requireNonNull(this.binding.timeEvent).setText(eventDate);

            AddNoteActivity.save_HourOfDay = hourOfDay;
            AddNoteActivity.save_Minute = minute1;
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private static int save_Day = -1;
    private static int save_Month = -1;
    private static int save_Year = -1;

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        int day;
        int month;
        int year;

        day = AddNoteActivity.save_Day == -1 ? calendar.get(Calendar.DAY_OF_MONTH) : AddNoteActivity.save_Day;
        month = AddNoteActivity.save_Month == -1 ? calendar.get(Calendar.MONTH) : AddNoteActivity.save_Month;
        year = AddNoteActivity.save_Year == -1 ? calendar.get(Calendar.YEAR) : AddNoteActivity.save_Year;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year1, month1, dayOfMonth) -> {
            @SuppressLint("DefaultLocale") String eventDate = String.format("%02d", dayOfMonth) + '/' +String.format("%02d", month1 +1) + '/' + year1;
            Objects.requireNonNull(binding.dateEvent).setText(eventDate);

            // Set static variable to mark that this has been choose so the next time user add event the previous date will show up
            AddNoteActivity.save_Day = dayOfMonth;
            AddNoteActivity.save_Month = month1;
            AddNoteActivity.save_Year = year1;
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showPlaceDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Select Event Place")
                .setSingleChoiceItems(LIST_EVENT_PLACE, event_place_selected_index, (dialogInterface, position) -> {
                    // Set position index equals to position that user click on UI
                    event_place_selected_index = position;
                    Objects.requireNonNull(binding.placeEvent).setText(LIST_EVENT_PLACE[event_place_selected_index]);
                })
                .create().show();
    }

    private void sendDataToDatabase() {
        String nameValue = this.binding.nameEvent.getText().toString().trim();
        String placeValue = this.binding.placeEvent.getText().toString().trim();
        String dateValue = this.binding.dateEvent.getText().toString().trim();
        String timeValue = this.binding.timeEvent.getText().toString().trim();

        if (nameValue.isEmpty()) {
            this.binding.nameEvent.setError("Name is required");
            this.binding.nameEvent.requestFocus();
            return;
        }

        if (placeValue.isEmpty()) {
            this.binding.placeEvent.setError("Place is required");
            this.binding.placeEvent.requestFocus();
            return;
        }

        if (dateValue.isEmpty()) {
            this.binding.dateEvent.setError("Date is required");
            this.binding.dateEvent.requestFocus();
            return;
        }

        if (timeValue.isEmpty()) {
            this.binding.timeEvent.setError("Time is required");
            this.binding.timeEvent.requestFocus();
            return;
        }

        this.binding.progressBar.setVisibility(View.VISIBLE);

        this.userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        this.rootNode = FirebaseDatabase.getInstance();
        this.reference = this.rootNode.getReference("Users").child(userID).child("NoteItems");

        NoteItem noteItem = new NoteItem(nameValue, placeValue, dateValue, timeValue);

        this.reference.push().setValue(noteItem);

        Toast.makeText(AddNoteActivity.this, "Add successful", Toast.LENGTH_SHORT).show();
    }

}