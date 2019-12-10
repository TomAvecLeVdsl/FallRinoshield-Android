package fr.tomguillou.fallrinoshield;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;

import static android.view.Gravity.CENTER;
import static android.widget.ListPopupWindow.MATCH_PARENT;
import static java.lang.Character.SIZE;
import static java.lang.Math.sqrt;

public class MainActivity extends AppCompatActivity implements ShakeDetector.Listener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector((ShakeDetector.Listener) this);
        sd.start(sensorManager);

        TextView tv = new TextView(this);
        tv.setGravity(CENTER);
        tv.setText("Shake me, bro!");
        setContentView(tv, new ActionBar.LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }

    @Override
    public void hearShake() {
        Toast.makeText(this, "Don't shake me, bro!", Toast.LENGTH_SHORT).show();
    }

}