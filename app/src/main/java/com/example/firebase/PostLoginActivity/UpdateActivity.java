package com.example.firebase.PostLoginActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.firebase.Models.NoteItem;
import com.example.firebase.R;
import com.example.firebase.databinding.ActivityNoteBinding;
import com.example.firebase.databinding.ActivityUpdateBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class UpdateActivity extends AppCompatActivity{

    private EditText label_update, textContent_update;
    final Calendar calendar = Calendar.getInstance();

    DatabaseReference reference;

    private String userID;
    //    private FirebaseFireStore db;
    private String id, label, text_content;
    private NoteItem noteItem;

    private NotificationCompat managerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        label_update = findViewById(R.id.label_update);
        textContent_update = findViewById(R.id.textContent_update);

        NoteItem noteItem = (NoteItem) getIntent().getSerializableExtra("noteItems");
        id = noteItem.getId();

        label_update.setText(noteItem.getLabel());
        textContent_update.setText(noteItem.getTextContent());


//        managerCompat = NotificationManagerCompat.from(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu_update_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.updateBtn :
                updateData();
                break;
            case R.id.notifyBtn :
                setNotification();
                break;
            case R.id.setPasswordBtn:
                setPasswordNote();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ShowToast")
    public void updateData() {

        NoteItem noteItem = (NoteItem) getIntent().getSerializableExtra("noteItems");
        id = noteItem.getId();

        label = label_update.getText().toString().trim();
        text_content = textContent_update.getText().toString().trim();

        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").
                child(userID).child("NoteItems");



        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                snapshot.getRef().child("label").setValue(label);
                snapshot.getRef().child("textContent").setValue(text_content);
                Toast.makeText(UpdateActivity.this, "Update successfully", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setNotification() {

        final EditText mtimePicker = new EditText(this);
        mtimePicker.setHint("Choose time");
        mtimePicker.setClickable(false);
        mtimePicker.setFocusable(false);
        mtimePicker.setCursorVisible(false);
        mtimePicker.setFocusableInTouchMode(false);

        mtimePicker.setOnClickListener(view -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(UpdateActivity.this, (timePicker, selectedHour, selectedMinute) ->
                    mtimePicker.setText( selectedHour + ":" + selectedMinute), hour, minute, true);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });


        final EditText mdatePicker = new EditText(this);
        mdatePicker.setHint("Choose date");
        mdatePicker.setClickable(false);
        mdatePicker.setFocusable(false);
        mdatePicker.setCursorVisible(false);
        mdatePicker.setFocusableInTouchMode(false);

        DatePickerDialog.OnDateSetListener date = (datePicker, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            mdatePicker.setText(sdf.format(calendar.getTime()));
        };

        mdatePicker.setOnClickListener(view ->
                new DatePickerDialog(UpdateActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        ll.addView(mtimePicker);
        ll.addView(mdatePicker);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Set time and date for note")
                .setView(ll)
                .setPositiveButton("Yes", (dialogInterface, i) -> {
//                        String time = mtimePicker.getText().toString();
//                        String date = mdatePicker.getText().toString();
                    Toast.makeText(UpdateActivity.this, "Get notifi successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialogInterface, i) -> {
                    Toast.makeText(UpdateActivity.this, "Get notifi successfully", Toast.LENGTH_SHORT).show();

                    dialogInterface.dismiss();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

//    private void openNotification(View view) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "main")
//                .setContentTitle(label)
//                .setContentText(text_content)
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setSmallIcon(R.drawable.ic_circle_notifications);
//        managerCompat.notify(100, builder.build());
//    }


    private void setPasswordNote() {
        final EditText password = new EditText(this);
        password.setHint("Enter password here");

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        ll.addView(password);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Set password for note")
                .setView(ll)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String passwordValue = password.getText().toString();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}