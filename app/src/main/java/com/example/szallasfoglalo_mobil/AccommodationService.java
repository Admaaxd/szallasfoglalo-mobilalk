package com.example.szallasfoglalo_mobil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AccommodationService {
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public AccommodationService(Context context) {
        this.sharedPreferences = context.getSharedPreferences("booked_accommodations", Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    @SuppressLint("StaticFieldLeak")
    public void saveAccommodation(String key, Accommodation accommodation, Runnable onSuccess, Runnable onError) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String json = gson.toJson(accommodation);
                    editor.putString(key, json);
                    editor.apply();
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    onSuccess.run();
                } else {
                    onError.run();
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteAccommodation(String key, Runnable onSuccess, Runnable onError) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(key);
                    editor.apply();
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    onSuccess.run();
                } else {
                    onError.run();
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void getAllAccommodations(Runnable onSuccess, Runnable onError, Consumer<List<Accommodation>> onResult) {
        new AsyncTask<Void, Void, List<Accommodation>>() {
            @Override
            protected List<Accommodation> doInBackground(Void... voids) {
                try {
                    Map<String, ?> allEntries = sharedPreferences.getAll();
                    List<Accommodation> accommodations = new ArrayList<>();
                    for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                        accommodations.add(gson.fromJson(entry.getValue().toString(), Accommodation.class));
                    }
                    return accommodations;
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Accommodation> result) {
                if (result != null) {
                    onResult.accept(result);
                    onSuccess.run();
                } else {
                    onError.run();
                }
            }
        }.execute();
    }
}
