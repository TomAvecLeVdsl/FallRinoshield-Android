package fr.tomguillou.fallrinoshield;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;



public class MainActivity extends Activity implements SensorEventListener  {


    private static final float SHAKE_THRESHOLD = -5; // m/S**2
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 500;
    private long mLastShakeTime;
    private SensorManager mSensorMgr;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get a sensor manager to listen for shakes
        mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        mp = MediaPlayer.create(this, Uri.parse("android.resource://"+getPackageName()+"/raw/rinoshield1"));

        // Listen for shakes
        Sensor accelerometer = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMgr.registerListener( this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }


    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;
                Log.d("FallingRinoshield", "Acceleration is " + acceleration + "m/s^2");

                if (acceleration >= SHAKE_THRESHOLD) {
                    mLastShakeTime = curTime;
                    Log.d("FallingRinoshield", "RRRIIIINNNNNOOOOSSSHHHHIIIIIEEEELLLLDDDDD!!!!!!!");
                    mp.start();
                }
            }
        }
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ignore
    }




}
