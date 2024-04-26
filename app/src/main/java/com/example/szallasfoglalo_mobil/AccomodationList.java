package com.example.szallasfoglalo_mobil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AccomodationList extends AppCompatActivity implements AccommodationAdapter.OnAccommodationListener {

    private static final String LOG_TAG = AccomodationList.class.getName();
    private FirebaseUser user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accomodation_list);


        List<Accommodation> accommodations = new ArrayList<>();
        accommodations.add(new Accommodation("Coral Cove", R.drawable.image10, "Maldives", 250.00, "Escape to paradise with this exclusive villa on a pristine beach. Perfect for luxury relaxation."));
        accommodations.add(new Accommodation("Sandy Shores Resort", R.drawable.image2, "Cancun, Mexico", 180.00, "Family-friendly resort with endless beach activities and water sports. Ideal for a fun-filled vacation."));
        accommodations.add(new Accommodation("Blue Horizon Suites", R.drawable.image3, "Santorini, Greece", 300.00, "Elegant suites offering breathtaking sunset views over the ocean. A romantic getaway spot."));
        accommodations.add(new Accommodation("Palm Tree Bungalows", R.drawable.image4, "Maui, Hawaii", 220.00, "Relax in your private bungalow surrounded by palm trees and steps away from the beach."));
        accommodations.add(new Accommodation("Surfer's Paradise", R.drawable.image5, "Gold Coast, Australia", 150.00, "The ultimate destination for surf enthusiasts. Enjoy beach access and surf lessons."));
        accommodations.add(new Accommodation("Oceanfront Villa", R.drawable.image6, "Rio de Janeiro, Brazil", 260.00, "Luxurious villa with direct beach access. Experience the vibrant culture of Rio."));
        accommodations.add(new Accommodation("Sea Breeze Inn", R.drawable.image7, "Phuket, Thailand", 130.00, "Cozy inn located on a quiet beach. The perfect retreat for peace and tranquility."));
        accommodations.add(new Accommodation("Beachside Escape", R.drawable.image8, "Lagos, Portugal", 200.00, "Modern apartments with stunning ocean views. Explore the beautiful Algarve region."));
        accommodations.add(new Accommodation("Wave Watcher's Cabin", R.drawable.image9, "Monterey, California", 175.00, "A rustic cabin with a deck overlooking the Pacific. Ideal for nature lovers and wave watchers."));

        RecyclerView rvAccommodations = findViewById(R.id.accommodationRecyclerView);
        rvAccommodations.setLayoutManager(new GridLayoutManager(this, 2));
        AccommodationAdapter adapter = new AccommodationAdapter(accommodations, this);
        rvAccommodations.setAdapter(adapter);

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

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void fetchAccommodationsData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Accommodation> accommodations = new ArrayList<>();

        db.collection("accommodations")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Accommodation accommodation = document.toObject(Accommodation.class);
                            accommodations.add(accommodation);
                        }
                        // Set the adapter after fetching data
                        AccommodationAdapter adapter = new AccommodationAdapter(accommodations, AccomodationList.this);
                        RecyclerView rvAccommodations = findViewById(R.id.accommodationRecyclerView);
                        rvAccommodations.setAdapter(adapter);
                    } else {
                        Log.d("AccommodationList", "Error getting documents: ", task.getException());
                    }
                });
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


    public void onAccommodationClicked(Accommodation accommodation) {
        Intent intent = new Intent(this, BookingActivity.class);
        intent.putExtra("ACCOMMODATION_NAME", accommodation.getName());
        intent.putExtra("ACCOMMODATION_IMAGE_RESOURCE", accommodation.getImageResource());
        intent.putExtra("ACCOMMODATION_LOCATION", accommodation.getLocation());
        intent.putExtra("ACCOMMODATION_PRICE", accommodation.getPrice());
        intent.putExtra("ACCOMMODATION_DESCRIPTION", accommodation.getDescription());
        startActivity(intent);
    }
}