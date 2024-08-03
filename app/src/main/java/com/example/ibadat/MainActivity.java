package com.example.ibadat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalTime;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;




public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "https://api.aladhan.com/v1/";
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    String city = "Dhaka";
    private TextView dateWithDayTextView;
    private TextView districtNameTextView;
    private TextView ongoingPrayerName;
    private TextView timeRemainingOngoing;
    private TextView ongoingNamajTimeTextView;
    private TextView upcomingPrayerName;
    private TextView timeRemaining;
    private TextView upcomingNamajTimeTextView, anotherTimeTextView;
    private TextView fajrNamajTimeTextView;
    private TextView maghribNamajTimeTextView;
    private FusedLocationProviderClient fusedLocationClient;
    private Handler handler;
    private Runnable runnable;

    private LocalTime[] prayerTimes = new LocalTime[8];
    private String[] prayerNames = {"Fajr", "Sunrise", "Dhuhr", "Asr", "Sunset", "Maghrib", "Isha", "Midnight"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        anotherTimeTextView = findViewById(R.id.another_time_text_view);
        dateWithDayTextView = findViewById(R.id.date_with_day);
        districtNameTextView = findViewById(R.id.district_name);
        ongoingPrayerName = findViewById(R.id.ongoing_prayer_name);
        timeRemainingOngoing = findViewById(R.id.time_remaining_ongoing);
        ongoingNamajTimeTextView = findViewById(R.id.ongoing_namaj_time);
        upcomingPrayerName = findViewById(R.id.upcoming_prayer_name);
        timeRemaining = findViewById(R.id.time_remaining);
        upcomingNamajTimeTextView = findViewById(R.id.upcoming_namaj_time);
        fajrNamajTimeTextView = findViewById(R.id.fajr_namaj_time);
        maghribNamajTimeTextView = findViewById(R.id.maghrib_namaj_time);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                updatePrayerTimes();
                handler.postDelayed(this, 1000); // Update every second
            }
        };

        fetchPrayerTimes(city, "Bangladesh", 1);
        updateDateWithDay();
        getLocationAndUpdateDistrict(); // Get location district name
    }

    private void fetchPrayerTimes(String city, String country, int method) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PrayerTimesApi api = retrofit.create(PrayerTimesApi.class);
        Call<PrayerTimesResponse> call = api.getPrayerTimes(city, country, method);
        call.enqueue(new Callback<PrayerTimesResponse>() {
            @Override
            public void onResponse(Call<PrayerTimesResponse> call, Response<PrayerTimesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setPrayerTimes(response.body().data.timings);
                    handler.post(runnable);
                } else {
                    Log.e(TAG, "Failed to get prayer times");
                }
            }

            @Override
            public void onFailure(Call<PrayerTimesResponse> call, Throwable t) {
                Log.e(TAG, "API call failed", t);
            }
        });
    }

    private void setPrayerTimes(PrayerTimesResponse.Timings timings) {
        prayerTimes[0] = LocalTime.parse(timings.fajr);
        prayerTimes[1] = LocalTime.parse(timings.sunrise);
        prayerTimes[2] = LocalTime.parse(timings.dhuhr);
        prayerTimes[3] = LocalTime.parse(timings.asr);
        prayerTimes[4] = LocalTime.parse(timings.sunset);
        prayerTimes[5] = LocalTime.parse(timings.maghrib);
        prayerTimes[6] = LocalTime.parse(timings.isha);
        prayerTimes[7] = LocalTime.parse(timings.midnight);
    }

    private void updatePrayerTimes() {
        DateTime now = DateTime.now();
        LocalTime currentTime = now.toLocalTime();

        String ongoingName = null;
        Duration ongoingDuration = null;
        String nextPrayerName = null;
        Duration nextPrayerDuration = null;

        for (int i = 0; i < prayerTimes.length; i++) {
            if (i == 0 && currentTime.isBefore(prayerTimes[i])) {
                ongoingName = prayerNames[prayerNames.length - 1];
                ongoingDuration = new Duration(now.withTime(prayerTimes[prayerNames.length - 1]), now);
                nextPrayerName = prayerNames[i];
                nextPrayerDuration = new Duration(now, now.withTime(prayerTimes[i]));
                break;
            } else if (i > 0 && currentTime.isBefore(prayerTimes[i])) {
                ongoingName = prayerNames[i - 1];
                ongoingDuration = new Duration(now.withTime(prayerTimes[i - 1]), now);
                nextPrayerName = prayerNames[i];
                nextPrayerDuration = new Duration(now, now.withTime(prayerTimes[i]));
                break;
            }
        }

        if (nextPrayerName == null) {
            ongoingName = prayerNames[prayerNames.length - 1];
            ongoingDuration = new Duration(now.withTime(prayerTimes[prayerNames.length - 1]), now);
            nextPrayerName = prayerNames[0];
            nextPrayerDuration = new Duration(now, now.plusDays(1).withTime(prayerTimes[0]));
        }


        if (ongoingName != null) {
            ongoingPrayerName.setText(ongoingName);
            timeRemainingOngoing.setText(formatDuration(ongoingDuration));
        }
        upcomingPrayerName.setText(nextPrayerName);
        timeRemaining.setText(formatDuration(nextPrayerDuration));


        LocalTime ongoingNamajTime = prayerTimes[Arrays.asList(prayerNames).indexOf(ongoingName)];
        LocalTime upcomingNamajTime = prayerTimes[Arrays.asList(prayerNames).indexOf(nextPrayerName)];
        ongoingNamajTimeTextView.setText(ongoingNamajTime.toString("hh:mm a"));
        upcomingNamajTimeTextView.setText(upcomingNamajTime.toString("hh:mm a"));


        fajrNamajTimeTextView.setText(prayerTimes[0].toString("hh:mm a"));
        maghribNamajTimeTextView.setText(prayerTimes[5].toString("hh:mm a"));
        anotherTimeTextView.setText(LocalTime.now().toString("hh:mm:ss a"));
    }

    private void updateDateWithDay() {
        DateTime now = DateTime.now();
        String formattedDate = now.toString("EEEE, MMMM dd, yyyy", Locale.getDefault());
        dateWithDayTextView.setText(formattedDate);
    }

    private void getLocationAndUpdateDistrict() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }

        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Location location = task.getResult();
                    updateDistrictName(location);
                } else {
                    Log.e(TAG, "Failed to get location");
                }
            }
        });
    }

    private void updateDistrictName(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                String district = addresses.get(0).getSubAdminArea();
                districtNameTextView.setText(district);
            }
        } catch (IOException e) {
            Log.e(TAG, "Geocoder failed", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndUpdateDistrict();
            } else {
                Log.e(TAG, "Location permission denied");
            }
        }
    }

    private String formatDuration(Duration duration) {
        long hours = duration.getStandardHours();
        long minutes = duration.getStandardMinutes() % 60;
        long seconds = duration.getStandardSeconds() % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    public void compassFunction(View view) {
        Intent myIntent = new Intent(this, QiblaCompass.class);
        startActivity(myIntent);
    }

    public void tasbihFunction(View view) {
        Intent myIntent = new Intent(this, tasbih.class);
        startActivity(myIntent);
    }

    public void alquranFunction(View view) {
        Intent myIntent = new Intent(this, alquran.class);
        startActivity(myIntent);
    }

    public void prayerSchedule(View view) {
        Intent myIntent = new Intent(this, prayer_schedule.class);
        startActivity(myIntent);
    }

    public void sahriANDifter(View view) {
        Intent myIntent = new Intent(this, sahriANDiftar.class);
        startActivity(myIntent);
    }
    public void donationfun(View view) {
        Intent myIntent = new Intent(this, donation.class);
        startActivity(myIntent);
    }
}