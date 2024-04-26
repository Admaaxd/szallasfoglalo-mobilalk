package com.example.szallasfoglalo_mobil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AccommodationAdapter extends RecyclerView.Adapter<AccommodationAdapter.AccommodationViewHolder> {

    private final List<Accommodation> accommodations;
    private OnAccommodationListener listener;

    public AccommodationAdapter(List<Accommodation> accommodations, OnAccommodationListener listener) {
        this.accommodations = accommodations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AccommodationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accomodation, parent, false);
        return new AccommodationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccommodationViewHolder holder, int position) {
        Accommodation accommodation = accommodations.get(position);
        holder.textViewAccommodationName.setText(accommodation.getName());
        holder.imageViewAccommodation.setImageResource(accommodation.getImageResource());

        holder.itemView.setOnClickListener(v -> listener.onAccommodationClicked(accommodation));
    }

    @Override
    public int getItemCount() {
        return accommodations.size();
    }

    public static class AccommodationViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageViewAccommodation;
        final TextView textViewAccommodationName;

        public AccommodationViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAccommodation = itemView.findViewById(R.id.imageViewAccommodation);
            textViewAccommodationName = itemView.findViewById(R.id.textViewAccommodationName);
        }
    }

    public interface OnAccommodationListener {
        void onAccommodationClicked(Accommodation accommodation);
    }
}


