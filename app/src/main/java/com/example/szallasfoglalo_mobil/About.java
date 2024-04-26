package com.example.szallasfoglalo_mobil;

import static android.content.ContentValues.TAG;
import static com.example.szallasfoglalo_mobil.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_about);

        MaterialToolbar toolbar = findViewById(id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        View customTitleView = LayoutInflater.from(this).inflate(layout.toolbar_title_layout, null);
        TextView titleTextView = customTitleView.findViewById(id.toolbar_title);
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

        TextView tvAccommodationDescription = findViewById(R.id.tvAccommodationDescription);
        String descriptionText = "Discover the ultimate getaway with our exclusive accommodations. Experience unparalleled comfort and luxury that makes every moment unforgettable.";
        tvAccommodationDescription.setText(descriptionText);

        animateTextColor(tvAccommodationDescription);
    }
    private void animateTextColor(TextView textView) {
        final int colorStart = Color.RED;
        final int colorEnd = Color.BLUE;

        ValueAnimator colorAnimator = ValueAnimator.ofArgb(colorStart, colorEnd);
        colorAnimator.setDuration(2000);
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.setRepeatMode(ValueAnimator.REVERSE);

        colorAnimator.addUpdateListener(animator -> textView.setTextColor((int) animator.getAnimatedValue()));
        colorAnimator.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menubar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == id.my_accomodations) {
            Intent intent = new Intent(this, my_accomodations.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        else if (item.getItemId() == id.action_about){
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}