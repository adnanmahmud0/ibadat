package com.example.ibadat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ibadat.databinding.ActivitySahriandiftarBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class sahriANDiftar extends AppCompatActivity {
    private ActivitySahriandiftarBinding binding;
    private sahriANDiftarAdapter sahriANDiftarAdapter;
    private Spinner citySpinner;
    private String selectedCity;
    private RequestQueue requestQueue;
    private static final String PREF_SELECTED_CITY = "selected_city";
    private SharedPreferences sharedPreferences;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private ArrayAdapter<CharSequence> adapter; // Declare adapter as a class-level variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySahriandiftarBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {

            getDefaultLocation();
        }


        citySpinner = findViewById(R.id.citySpinner);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.city_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);

        // Set listener for item selection
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = parent.getItemAtPosition(position).toString();
                Toast.makeText(sahriANDiftar.this, "Selected City: " + selectedCity, Toast.LENGTH_SHORT).show();
                loadData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing if nothing is selected
            }
        });

        sahriANDiftarAdapter = new sahriANDiftarAdapter(this);
        binding.namazRecycler.setAdapter(sahriANDiftarAdapter);
        binding.namazRecycler.setLayoutManager(new LinearLayoutManager(this));

        requestQueue = Volley.newRequestQueue(this);
    }

    private void getDefaultLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {

                            String city = getCityFromLocation(location);
                            if (city != null) {

                                int position = adapter.getPosition(city);
                                citySpinner.setSelection(position);

                                sharedPreferences.edit().putString(PREF_SELECTED_CITY, city).apply();
                            }
                        }
                    }
                });
    }

    private String getCityFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                return addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get default location
                getDefaultLocation();
            } else {
                // Permission denied, handle accordingly (e.g., show message, disable location features)
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadData() {
        long millis = System.currentTimeMillis();
        String url = buildUrl(millis);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            sahriANDiftarAdapter.clear();
                            String status = response.getString("status");
                            if ("OK".equals(status)) {
                                JSONArray dataArray = response.getJSONArray("data");
                                int todayDate = Integer.parseInt(convertDate(millis, "dd"));

                                for (int i = todayDate - 1; i < dataArray.length(); i++) {
                                    JSONObject timingObject = dataArray.getJSONObject(i).getJSONObject("timings");
                                    JSONObject dateObject = dataArray.getJSONObject(i).getJSONObject("date");
                                    String fajr = timingObject.optString("Fajr", "");

                                    String maghrib = timingObject.optString("Maghrib", "");

                                    String date = dateObject.getString("readable");

                                    fajr = convertTime(fajr);

                                    maghrib = convertTime(maghrib);


                                    sahriANDiftarModel sahriANDiftarModel = new sahriANDiftarModel(fajr, maghrib, date);
                                    sahriANDiftarAdapter.add(sahriANDiftarModel);
                                }
                            }
                        } catch (JSONException e) {
                            Toast.makeText(sahriANDiftar.this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(sahriANDiftar.this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private String buildUrl(long millis) {
        DateFormat dateFormat = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
        String monthYear = dateFormat.format(new Date(millis));

        return "http://api.aladhan.com/v1/calendarByCity?" +
                "city=" + selectedCity + "&" +
                "country=Bangladesh&" +
                "method=2&" +
                "month=" + monthYear.substring(0, 2) + "&" +
                "year=" + monthYear.substring(3);
    }

    public static String convertDate(long millis, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return formatter.format(new Date(millis));
    }

    // Function to convert time string to formatted time string
    public static String convertTime(String time) {
        // Remove parentheses and their contents from the time string
        time = time.replaceAll("\\(.*\\)", "").trim();

        // Convert time strings to Date objects
        SimpleDateFormat sdfInput = new SimpleDateFormat("HH:mm");
        Date temp = null;
        try {
            temp = sdfInput.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            // Return the original time string if parsing fails
            return time;
        }

        // Set AM or PM based on the time
        SimpleDateFormat sdfOutput = new SimpleDateFormat("hh:mm a");
        return sdfOutput.format(temp);
    }
    public void back(View view) {
        finish();
    }
}
