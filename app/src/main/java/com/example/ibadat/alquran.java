package com.example.ibadat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class alquran extends AppCompatActivity {

    final static int extraID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alquran);



    }

    public void back(View view) {
        finish();
    }

    public void audio0(View view) {
        Intent myIntent = new Intent(this, quran_audio.class);
        myIntent.putExtra("EXTRA_ID", 0);
        myIntent.putExtra("URL", "url1");
        myIntent.putExtra("text", "Al-Faatiha");
        myIntent.putExtra("text1", "سورة الفاتحة");
        startActivity(myIntent);
    }
    public void audio1(View view) {
        Intent myIntent = new Intent(this, quran_audio.class);
        myIntent.putExtra("EXTRA_ID", 1);
        myIntent.putExtra("URL", "url2");
        myIntent.putExtra("text", "Al-Baqara");
        myIntent.putExtra("text1", "سورة البقرة");
        startActivity(myIntent);
    }
    public void audio2(View view) {
        Intent myIntent = new Intent(this, quran_audio.class);
        myIntent.putExtra("EXTRA_ID", 2);
        myIntent.putExtra("URL", "url3");
        myIntent.putExtra("text", "Al-Imraan");
        myIntent.putExtra("text1", "سورة آل عمران");
        startActivity(myIntent);
    }
    public void audio3(View view) {
        Intent myIntent = new Intent(this, quran_audio.class);
        myIntent.putExtra("EXTRA_ID", 3);
        myIntent.putExtra("URL", "url4");
        myIntent.putExtra("text", "Al-Nisaa");
        myIntent.putExtra("text1", "سورة النساء");
        startActivity(myIntent);
    }
    public void audio4(View view) {
        Intent myIntent = new Intent(this, quran_audio.class);
        myIntent.putExtra("EXTRA_ID", 4);
        myIntent.putExtra("URL", "url5");
        myIntent.putExtra("text", "Al-Maaida");
        myIntent.putExtra("text1", "سورة المائدة");
        startActivity(myIntent);
    }
    public void audio5(View view) {
        Intent myIntent = new Intent(this, quran_audio.class);
        myIntent.putExtra("EXTRA_ID", 5);
        myIntent.putExtra("URL", "url6");
        myIntent.putExtra("text", "Al-An'aam");
        myIntent.putExtra("text1", "سورة الأنعام");
        startActivity(myIntent);
    }
    public void audio6(View view) {
        Intent myIntent = new Intent(this, quran_audio.class);
        myIntent.putExtra("EXTRA_ID", 6);
        myIntent.putExtra("URL", "url7");
        myIntent.putExtra("text", "Al-A'raf");
        myIntent.putExtra("text1", "سورة الأعراف");
        startActivity(myIntent);
    }
    public void audio7(View view) {
        Intent myIntent = new Intent(this, quran_audio.class);
        myIntent.putExtra("EXTRA_ID", 7);
        myIntent.putExtra("URL", "url8");
        myIntent.putExtra("text", "Al-Anfaal");
        myIntent.putExtra("text1", "سورة الأنفال");
        startActivity(myIntent);
    }
    public void audio8(View view) {
        Intent myIntent = new Intent(this, quran_audio.class);
        myIntent.putExtra("EXTRA_ID", 8);
        myIntent.putExtra("URL", "url9");
        myIntent.putExtra("text", "Al-Tawba");
        myIntent.putExtra("text1", "سورة التوبة");
        startActivity(myIntent);
    }
    public void audio9(View view) {
        Intent myIntent = new Intent(this, quran_audio.class);
        myIntent.putExtra("EXTRA_ID", 9);
        myIntent.putExtra("URL", "url10");
        myIntent.putExtra("text", "Yunus");
        myIntent.putExtra("text1", "سورة يونس");
        startActivity(myIntent);
    }

}