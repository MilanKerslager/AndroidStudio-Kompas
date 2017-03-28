package cz.kerslager.android.kompas;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    ImageView imageViewArrow;
    TextView textViewDegrees;

    private static SensorManager sensorService;
    private Sensor sensor;

    private float currentDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewArrow = (ImageView) findViewById(R.id.imageViewArrow);
        textViewDegrees = (TextView) findViewById(R.id.textViewDegrees);

        sensorService = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorService.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    protected void onResume() {
        super.onResume();
        if (sensor != null) {
            sensorService.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(MainActivity.this, "Not supported!", Toast.LENGTH_LONG).show();
        }
    }

    protected void onPause() {
        super.onPause();
        sensorService.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int degree = Math.round(event.values[0]);
        textViewDegrees.setText(Integer.toString(degree)+(char) 0x00B0);

        // šipka potřebuje otočit o +90 stupnů (originál směřuje doleva)
        degree-=90;

        // vlastní rotace (s animací)
        RotateAnimation ra = new RotateAnimation(
                currentDegree, // z pozice
                -degree,        // do pozice
                Animation.RELATIVE_TO_SELF, // pivotXType
                0.5f,                       // pivotXValue -> rotace kolem středu obrázku (50%)
                Animation.RELATIVE_TO_SELF, // pivotYType
                0.5f                        // pivotYValue -> rotace kolem středu obrázku (50%)
        );
        ra.setDuration(1000);  // délka animace otáčení
        ra.setFillAfter(true); // otočit a zůstat v nové pozici

        imageViewArrow.startAnimation(ra);
        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
