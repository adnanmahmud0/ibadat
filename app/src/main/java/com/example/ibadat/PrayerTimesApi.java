package com.example.ibadat;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PrayerTimesApi {
    @GET("timingsByCity")
    Call<PrayerTimesResponse> getPrayerTimes(
            @Query("city") String city,
            @Query("country") String country,
            @Query("method") int method
    );
}
