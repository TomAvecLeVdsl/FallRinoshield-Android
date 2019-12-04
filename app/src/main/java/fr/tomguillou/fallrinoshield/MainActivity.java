package fr.tomguillou.fallrinoshield;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;

import static java.lang.Character.SIZE;
import static java.lang.Math.sqrt;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    Sensor accelerometer;

    private long lastUpdate = -1;

    private float accel_values[];
    private float last_accel_values[];
    LinkedList<Float> samples;
    public static final String TAG = "MainActivity" ;
    private final float THRESHOLD =  8f;

//    private int fallThreshold = 10;

    private float mAccelCurrent = SensorManager.GRAVITY_EARTH;
    private float mAccelLast = SensorManager.GRAVITY_EARTH;
    private float mAccel = 0.00f;

    private final static int CHECK_INTERVAL = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG,"onCreate : Initialising Sensor") ;
        sensorManager =(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG,"onCreate : Accelerometer Registered") ;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long curTime = System.currentTimeMillis();

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // sampling frequency f= 10Hz.
            if ((curTime - lastUpdate) > CHECK_INTERVAL) {

                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                accel_values = event.values.clone();

                if (last_accel_values != null) {

                    mAccelLast = mAccelCurrent;
                    mAccelCurrent =(float)Math.sqrt(accel_values[0]* accel_values[0] + accel_values[1]*accel_values[1]
                            + accel_values[2]*accel_values[2]);

                    // Initial approach
//                    float delta = mAccelCurrent - mAccelLast;
//                    mAccel = mAccel * 0.9f + delta;



                    add(mAccelCurrent);
                    if (isFull() && isFallDetected()){
                        Log.w(TAG, "Fall detected by window class");

                    }
//                    Inital approach
//                    =====================================
//                    if (mAccel > fallThreshold) {
//
//                        Log.w(TAG, "acceleration greater than threshold");
//                        // Send the value back to the Activity
//                        msg = mHandler.obtainMessage(Constants.MESSAGE_EMERGENCY);
//                        mHandler.sendMessage(msg);
//                    }
                }

                last_accel_values = accel_values.clone();
            }
        }

    }

    public Boolean isFull(){
        return (samples.size() > SIZE);
    }
    public Boolean isFallDetected(){
        Float max = Collections.max(samples);
        Float min = Collections.min(samples);
        Float diff = Math.abs(max-min);

        // check if min value detected earlier than max
        Boolean isFall = ( samples.indexOf(max) > samples.indexOf(min) );

        return (diff>THRESHOLD && isFall);
    }

    public void add(float value){
        if (!isFull()){
            samples.add(new Float(value));
            //add value
        } else {
            samples.removeFirst();
            samples.add(new Float(value));
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}