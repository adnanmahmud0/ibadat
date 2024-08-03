package com.example.ibadat;

public class sahriANDiftarModel {
    private String fajr;

    private String maghrib;

    private String date;

    public sahriANDiftarModel(String fajr, String maghrib, String date) {
        this.fajr = fajr;

        this.maghrib = maghrib;

        this.date = date;
    }

    public String getFajr() {
        return fajr;
    }

    public void setFajr(String fajr) {
        this.fajr = fajr;
    }


    public String getMaghrib() {
        return maghrib;
    }

    public void setMaghrib(String maghrib) {
        this.maghrib = maghrib;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
