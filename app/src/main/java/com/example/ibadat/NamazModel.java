package com.example.ibadat;

public class NamazModel {
    private String fajr;
    private String sunrise;
    private String dhuhr;
    private String asr;
    private String sunset;
    private String maghrib;
    private String isha;
    private String midnight;
    private String date;

    public NamazModel(String fajr, String sunrise, String dhuhr, String asr, String sunset, String maghrib, String isha, String midnight, String date) {
        this.fajr = fajr;
        this.sunrise = sunrise;
        this.dhuhr = dhuhr;
        this.asr = asr;
        this.sunset = sunset;
        this.maghrib = maghrib;
        this.isha = isha;
        this.midnight = midnight;
        this.date = date;
    }

    public String getFajr() {
        return fajr;
    }

    public void setFajr(String fajr) {
        this.fajr = fajr;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getDhuhr() {
        return dhuhr;
    }

    public void setDhuhr(String dhuhr) {
        this.dhuhr = dhuhr;
    }

    public String getAsr() {
        return asr;
    }

    public void setAsr(String asr) {
        this.asr = asr;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getMaghrib() {
        return maghrib;
    }

    public void setMaghrib(String maghrib) {
        this.maghrib = maghrib;
    }

    public String getIsha() {
        return isha;
    }

    public void setIsha(String isha) {
        this.isha = isha;
    }

    public String getMidnight() {
        return midnight;
    }

    public void setMidnight(String midnight) {
        this.midnight = midnight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
