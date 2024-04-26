package com.example.szallasfoglalo_mobil;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Map;

public class my_accomodations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_accomodations);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        View customTitleView = LayoutInflater.from(this).inflate(R.layout.toolbar_title_layout, null);
        TextView titleTextView = customTitleView.findViewById(R.id.toolbar_title);
        toolbar.addView(customTitleView);
        ObjectAnimator textSizeAnimator = ObjectAnimator.ofFloat(titleTextView, "textSize", 18f, 20f);
        textSizeAnimator.setDuration(800);
        textSizeAnimator.setRepeatCount(ValueAnimator.INFINITE);
        textSizeAnimator.setRepeatMode(ValueAnimator.REVERSE);
        textSizeAnimator.start();

        titleTextView.setOnClickListener(v -> {
            Intent intent = new Intent(this, AccomodationList.class);
            startActivity(intent);
        });

        displayBookedAccommodations();
    }

    private void displayBookedAccommodations() {
        SharedPreferences preferences = getSharedPreferences("booked_accommodations", MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();
        Gson gson = new Gson();

        LinearLayout container = findViewById(R.id.accommodationsContainer);
        container.removeAllViews();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String json = (String) entry.getValue();
            Accommodation accommodation = gson.fromJson(json, Accommodation.class);

            TextView textView = new TextView(this);
            String text = accommodation.getName() + "\n" + accommodation.getLocation() + "\nPrice: " + accommodation.getPrice() + "$/day" + "\nStay from: " + accommodation.getStartDate() + " to " + accommodation.getEndDate() + "\n" + "\n" + accommodation.getDescription();
            textView.setText(text);
            textView.setContentDescription("Accommodation details: " + text);
            textView.setPadding(10, 10, 10, 10);
            container.addView(textView);

            Button changeDateButton = new Button(this);
            changeDateButton.setText("Change Date");
            changeDateButton.setOnClickListener(v -> {
                // Show DatePickerDialogs to select new start and end dates
                showChangeDateDialog(accommodation, entry.getKey());
            });
            container.addView(changeDateButton);

            Button deleteButton = new Button(this);
            deleteButton.setText("Delete Reservation");
            deleteButton.setOnClickListener(v -> {
                deleteAccommodation(entry.getKey());
            });
            container.addView(deleteButton);
        }
    }

    private void showChangeDateDialog(Accommodation accommodation, String accommodationKey) {
        DatePickerDialog.OnDateSetListener startDateListener = (view, year, month, dayOfMonth) -> {
            String startDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            accommodation.setStartDate(startDate);

            DatePickerDialog endDatePicker = new DatePickerDialog(my_accomodations.this, (view2, year2, month2, dayOfMonth2) -> {
                String endDate = dayOfMonth2 + "/" + (month2 + 1) + "/" + year2;
                accommodation.setEndDate(endDate);

                saveUpdatedAccommodation(accommodation, accommodationKey);
            }, year, month, dayOfMonth);
            endDatePicker.setTitle("Select End Date");
            endDatePicker.show();
        };

        Calendar cal = Calendar.getInstance();
        DatePickerDialog startDatePicker = new DatePickerDialog(my_accomodations.this, startDateListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        startDatePicker.setTitle("Select Start Date");
        startDatePicker.show();
    }

    private void saveUpdatedAccommodation(Accommodation accommodation, String accommodationKey) {
        SharedPreferences preferences = getSharedPreferences("booked_accommodations", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(accommodation);
        editor.putString(accommodationKey, json);
        editor.apply();
        displayBookedAccommodations();
    }

    private void deleteAccommodation(String accommodationName) {
        SharedPreferences preferences = getSharedPreferences("booked_accommodations", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(accommodationName);
        editor.apply();
        displayBookedAccommodations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menubar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.my_accomodations) {
            Intent intent = new Intent(this, my_accomodations.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        else if (item.getItemId() == R.id.action_about){
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}