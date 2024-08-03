package com.example.ibadat;
import com.google.gson.annotations.SerializedName;

public class PrayerTimesResponse {
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("timings")
        public Timings timings;
    }

    public class Timings {
        @SerializedName("Fajr")
        public String fajr;
        @SerializedName("Sunrise")
        public String sunrise;
        @SerializedName("Dhuhr")
        public String dhuhr;
        @SerializedName("Asr")
        public String asr;
        @SerializedName("Sunset")
        public String sunset;
        @SerializedName("Maghrib")
        public String maghrib;
        @SerializedName("Isha")
        public String isha;
        @SerializedName("Midnight")
        public String midnight;
    }
}