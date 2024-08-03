package com.example.ibadat;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class quran_audio extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private List<String> songUrls;
    private int currentSongIndex;
    private boolean isPlaying = false;

    private int playbackPosition = 0;

    TextView outputId, outputText1, outputText, outputTextc;
    SeekBar progressBar;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_audio);
        outputId = findViewById(R.id.audioId);
        outputText = findViewById(R.id.english_text);
        outputTextc = findViewById(R.id.english_textc);
        outputText1 = findViewById(R.id.arabic_text1);
        progressBar = findViewById(R.id.song_progress);

        songUrls = new ArrayList<>();
        songUrls.add("");
        songUrls.add("https://firebasestorage.googleapis.com/v0/b/ibadat2024-986ff.appspot.com/o/1.mp3?alt=media&token=13a2e17f-6b4f-4f8e-8837-53ba1f9968a0");
        songUrls.add("https://firebasestorage.googleapis.com/v0/b/ibadat2024-986ff.appspot.com/o/2.mp3?alt=media&token=dcdf598c-7bfc-48d7-9bed-0061872dcc82");
        songUrls.add("https://firebasestorage.googleapis.com/v0/b/ibadat2024-986ff.appspot.com/o/3.mp3?alt=media&token=54a6535b-fe86-45aa-834d-f8ac67e8bb3b");
        songUrls.add("https://firebasestorage.googleapis.com/v0/b/ibadat2024-986ff.appspot.com/o/4.mp3?alt=media&token=6ac32983-1e0a-43bf-a39b-a69cadc0f409");
        songUrls.add("https://firebasestorage.googleapis.com/v0/b/ibadat2024-986ff.appspot.com/o/5.mp3?alt=media&token=0e5f542b-8062-4669-a523-35ac9965e81f");
        songUrls.add("https://firebasestorage.googleapis.com/v0/b/ibadat2024-986ff.appspot.com/o/6.mp3?alt=media&token=92f6da1d-b240-4620-bc74-fbc6dd19a176");
        songUrls.add("https://firebasestorage.googleapis.com/v0/b/ibadat2024-986ff.appspot.com/o/7.mp3?alt=media&token=7d695390-9d6b-4985-834a-00740f7b5752");
        songUrls.add("https://firebasestorage.googleapis.com/v0/b/ibadat2024-986ff.appspot.com/o/8.mp3?alt=media&token=69de56b1-e3ac-42d2-a4e4-27ace2316bd9");
        songUrls.add("https://firebasestorage.googleapis.com/v0/b/ibadat2024-986ff.appspot.com/o/9.mp3?alt=media&token=61f8a037-5f76-4cef-b92e-57962813a412");
        songUrls.add("https://firebasestorage.googleapis.com/v0/b/ibadat2024-986ff.appspot.com/o/10.mp3?alt=media&token=865e419c-2ac7-4ceb-af60-16bd1c1da69b");


        Intent mygetIntent = getIntent();
        currentSongIndex = mygetIntent.getIntExtra("EXTRA_ID", 0);
        int id = currentSongIndex;
        outputId.setText(String.valueOf(id + 1));
        String url = mygetIntent.getStringExtra("URL");
        String text = mygetIntent.getStringExtra("text");
        outputText.setText(text);
        outputTextc.setText(text);
        String text1 = mygetIntent.getStringExtra("text1");
        outputText1.setText(text1);

        ImageView playButton = findViewById(R.id.btn_play);
        ImageView stopButton = findViewById(R.id.btn_stop);

        playButton.setOnClickListener(v -> {
            if (isPlaying) {
                pausePlayback();
            } else {
                resumePlayback();
            }
        });
        stopButton.setOnClickListener(v -> stopPlayback());

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());


        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed for this implementation
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void back(View view) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playbackPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        finish();
    }

    private void playCurrentSong() {
        try {
            mediaPlayer.setDataSource(songUrls.get(currentSongIndex));
            mediaPlayer.prepare();
            mediaPlayer.seekTo(playbackPosition);
            mediaPlayer.start();
            isPlaying = true;
            progressBar.setMax(mediaPlayer.getDuration());
            updateProgressBar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlayback() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isPlaying = false;
            progressBar.setMax(mediaPlayer.getDuration());
            updateProgressBar();
        }
    }

    private void pausePlayback() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    private void resumePlayback() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPlaying = true;
            updateProgressBar();
        }
    }



    private void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                progressBar.setProgress(currentPosition);
                mHandler.postDelayed(this, 100);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);
    }
}