package com.example.ibadat;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QiblaCompass extends AppCompatActivity implements SensorEventListener {

    private ImageView compassNeedle;
    private TextView orientationText;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private float[] accelerometerReading = new float[3];
    private float[] magnetometerReading = new float[3];
    private float[] smoothedOrientation = new float[3];
    private static final float ALPHA = 0.1f; // Smoothing factor

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qibla_compass);

        compassNeedle = findViewById(R.id.compass_needle);
        orientationText = findViewById(R.id.orientation_text);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (magnetometer != null) {
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == accelerometer) {
            lowPassFilter(event.values, accelerometerReading);
        } else if (event.sensor == magnetometer) {
            lowPassFilter(event.values, magnetometerReading);
        }

        float[] rotationMatrix = new float[9];
        if (SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)) {
            float[] orientation = new float[3];
            SensorManager.getOrientation(rotationMatrix, orientation);
            lowPassFilter(orientation, smoothedOrientation);

            int azimuthInDegrees = (int) Math.toDegrees(smoothedOrientation[0]) + 90;
            azimuthInDegrees = (azimuthInDegrees + 360) % 360;

            compassNeedle.setRotation(-azimuthInDegrees);
            orientationText.setText(azimuthInDegrees + "°");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle changes in sensor accuracy if needed
    }

    private void lowPassFilter(float[] input, float[] output) {
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
    }

    public void back(View view) {
        finish();
    }
}
