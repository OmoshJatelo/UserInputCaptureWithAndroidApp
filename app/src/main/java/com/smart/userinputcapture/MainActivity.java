package com.smart.userinputcapture; // change to match your own package name

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout layoutName, layoutDate, layoutEmail;
    private TextInputEditText editTextName, editTextDate, editTextEmail;
    private RadioGroup radioGroupMarital;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        layoutName = findViewById(R.id.layoutName);
        layoutDate = findViewById(R.id.layoutDate);
        layoutEmail = findViewById(R.id.layoutEmail);
        editTextName = findViewById(R.id.editTextName);
        editTextDate = findViewById(R.id.editTextDate);
        editTextEmail = findViewById(R.id.editTextEmail);
        radioGroupMarital = findViewById(R.id.radioGroupMarital);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Open the calendar when the date field is tapped
        editTextDate.setOnClickListener(v -> showDatePicker());

        // Show the summary dialog on submit
        buttonSubmit.setOnClickListener(v -> onSubmit());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Month is zero-based, so add 1
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    editTextDate.setText(date);
                    layoutDate.setError(null);
                },
                year, month, day);

        // Prevent choosing a future date of birth
        picker.getDatePicker().setMaxDate(System.currentTimeMillis());
        picker.show();
    }

    private void onSubmit() {
        String name = editTextName.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (!validate(name, date, email)) {
            return;
        }

        String maritalStatus = (radioGroupMarital.getCheckedRadioButtonId() == R.id.radioMarried)
                ? "Married" : "Single";

        String message = "Name: " + name
                + "\nDate: " + date
                + "\nEmail: " + email
                + "\nMarital Status: " + maritalStatus;

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Submitted Details")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private boolean validate(String name, String date, String email) {
        boolean valid = true;

        layoutName.setError(null);
        layoutDate.setError(null);
        layoutEmail.setError(null);

        if (name.isEmpty()) {
            layoutName.setError("Please enter your full name");
            valid = false;
        }
        if (date.isEmpty()) {
            layoutDate.setError("Please select your date of birth");
            valid = false;
        }
        if (email.isEmpty()) {
            layoutEmail.setError("Please enter your email");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            layoutEmail.setError("Enter a valid email address");
            valid = false;
        }
        if (radioGroupMarital.getCheckedRadioButtonId() == -1) {
            new AlertDialog.Builder(this)
                    .setTitle("Missing selection")
                    .setMessage("Please select your marital status.")
                    .setPositiveButton("OK", null)
                    .show();
            valid = false;
        }
        return valid;
    }
}