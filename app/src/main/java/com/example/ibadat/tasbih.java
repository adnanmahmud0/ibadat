package com.example.ibadat;

import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class tasbih extends AppCompatActivity {

    public TextView textView, textView1;
    public int currentIndex;
    public final String[] texts = {"La ilaha illallah","Subhanallah", "Alhamdulillah", "Allahu Akbar"};
    public final String[] texts1 = {"لا إله إلا الله","سُبْحَانَ اللّٰه ", "ٱلْحَمْدُ لِلَّٰهِ", "الله أكبر"};

    public TextView touchCounter;
    public TextView top_touchCounter;
    public int touchCount = 0, top_touchCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasbih);

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        textView = findViewById(R.id.tv_slider_text);
        textView1 = findViewById(R.id.tv_slider_text1);
        touchCounter = findViewById(R.id.tv_touch_counter);
        top_touchCounter = findViewById(R.id.tv_top_touch_counter);

        textView.setText(texts[currentIndex]);

        ImageView btnLeft = findViewById(R.id.btn_left);
        ImageView btnRight = findViewById(R.id.btn_right);

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchCount = 0;
                if (currentIndex > 0) {
                    currentIndex--;
                    textView.setText(texts[currentIndex]);
                    textView1.setText(texts1[currentIndex]);
                    touchCounter.setText("" + touchCount);
                    top_touchCounter.setText("Read " + touchCount + " times");
                }
            }
        });




        LinearLayout layout = findViewById(R.id.main_layout);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchCount++;
                    top_touchCount++;
                    touchCounter.setText("" + touchCount);
                    top_touchCounter.setText("Read " + top_touchCount + " times");

                    if (touchCount == 33) {
                        vibrator.vibrate(300);
                        touchCount=0;
                    }
                }
                return true;
            }
        });
    }

    public void back(View view) {
        finish();
    }
}
