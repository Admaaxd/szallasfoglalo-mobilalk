package com.example.szallasfoglalo_mobil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.gson.Gson;

import java.util.Calendar;

public class BookingActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "booking_channel_id";
    private static final int MY_PERMISSIONS_REQUEST_POST_NOTIFICATIONS = 1;

    private TextView accommodationStartDateTextView, accommodationEndDateTextView;
    private int year, month, day;
    private String startDate = "", endDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        createNotificationChannel();

        ImageView accommodationImageView = findViewById(R.id.accommodationImageView);
        TextView accommodationNameTextView = findViewById(R.id.accommodationNameTextView);
        TextView accommodationLocationTextView = findViewById(R.id.accommodationLocationTextView);
        TextView accommodationPriceTextView = findViewById(R.id.accommodationPriceTextView);
        TextView accommodationDescriptionTextView = findViewById(R.id.accommodationDescriptionTextView);
        accommodationStartDateTextView = findViewById(R.id.accommodationStartDateTextView);
        accommodationEndDateTextView = findViewById(R.id.accommodationEndDateTextView);

        accommodationStartDateTextView.setOnClickListener(view -> showDatePickerDialog(true));
        accommodationEndDateTextView.setOnClickListener(view -> showDatePickerDialog(false));

        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        Button bookNowButton = findViewById(R.id.bookNowButton);
        Button backButton = findViewById(R.id.backButton);

        if (getIntent() != null) {
            String name = getIntent().getStringExtra("ACCOMMODATION_NAME");
            int imageResource = getIntent().getIntExtra("ACCOMMODATION_IMAGE_RESOURCE", 0);
            String location = getIntent().getStringExtra("ACCOMMODATION_LOCATION");
            double price = getIntent().getDoubleExtra("ACCOMMODATION_PRICE", 0);
            String description = getIntent().getStringExtra("ACCOMMODATION_DESCRIPTION");

            accommodationLocationTextView.setText(location);
            accommodationPriceTextView.setText(getString(R.string.price_format, price));
            accommodationDescriptionTextView.setText(description);

            accommodationImageView.setImageResource(imageResource);
            accommodationNameTextView.setText(name);
        }
        backButton.setOnClickListener(v -> finish());

        bookNowButton.setOnClickListener(v -> {
            if (startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Please choose both start and end dates before booking.", Toast.LENGTH_LONG).show();
                return;
            }
            String name = getIntent().getStringExtra("ACCOMMODATION_NAME");
            int imageResource = getIntent().getIntExtra("ACCOMMODATION_IMAGE_RESOURCE", 0);
            String location = getIntent().getStringExtra("ACCOMMODATION_LOCATION");
            double price = getIntent().getDoubleExtra("ACCOMMODATION_PRICE", 0);
            String description = getIntent().getStringExtra("ACCOMMODATION_DESCRIPTION");

            Accommodation accommodation = new Accommodation(name, imageResource, location, price, description);
            accommodation.setStartDate(startDate);
            accommodation.setEndDate(endDate);

            bookAccommodation(name, accommodation);
            String accommodationName = getIntent().getStringExtra("ACCOMMODATION_NAME");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestNotificationPermission();
            } else {
                proceedWithBooking();
            }
        });
    }

    private void bookAccommodation(String name, Accommodation accommodation) {
        Gson gson = new Gson();
        String json = gson.toJson(accommodation);

        SharedPreferences preferences = getSharedPreferences("booked_accommodations", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(name, json);
        editor.apply();

        Toast.makeText(this, "Accommodation booked!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, my_accomodations.class);
        startActivity(intent);
        finish();
    }

    private void showDatePickerDialog(boolean isStartDate) {
        final Calendar calendar = Calendar.getInstance();
        int mYear = year;
        int mMonth = month;
        int mDay = day;

        if (!isStartDate && !startDate.isEmpty()) {
            String[] parts = startDate.split("/");
            mYear = Integer.parseInt(parts[2]);
            mMonth = Integer.parseInt(parts[1]) - 1;
            mDay = Integer.parseInt(parts[0]);

            calendar.set(mYear, mMonth, mDay);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(BookingActivity.this,
                (view, yearSelected, monthOfYear, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + yearSelected;
                    if (isStartDate) {
                        startDate = date;
                        accommodationStartDateTextView.setText(date);
                    } else {
                        endDate = date;
                        accommodationEndDateTextView.setText(date);
                    }
                }, mYear, mMonth, mDay);

        if (!isStartDate && !startDate.isEmpty()) {
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        }
        datePickerDialog.show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, MY_PERMISSIONS_REQUEST_POST_NOTIFICATIONS);
        } else {
            proceedWithBooking();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                proceedWithBooking();
            } else {
                Toast.makeText(this, "Permission denied to post notifications.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void proceedWithBooking() {
        String name = getIntent().getStringExtra("ACCOMMODATION_NAME");
        int imageResource = getIntent().getIntExtra("ACCOMMODATION_IMAGE_RESOURCE", 0);
        String location = getIntent().getStringExtra("ACCOMMODATION_LOCATION");
        double price = getIntent().getDoubleExtra("ACCOMMODATION_PRICE", 0);
        String description = getIntent().getStringExtra("ACCOMMODATION_DESCRIPTION");

        Accommodation accommodation = new Accommodation(name, imageResource, location, price, description);
        accommodation.setStartDate(startDate);
        accommodation.setEndDate(endDate);

        bookAccommodation(name, accommodation);
        sendBookingNotification(name);
    }

    private void sendBookingNotification(String accommodationName) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.apartment)
                .setContentTitle("Booking Successful")
                .setContentText("Your booking for " + accommodationName + " has been confirmed.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = (int) System.currentTimeMillis();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }
}