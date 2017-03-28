package cz.kerslager.android.kompas;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    ImageView imageViewArrow;

    private ImageView imageView;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;

    boolean haveAccelerometer = false;
    boolean haveMagnetometer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        imageViewArrow = (ImageView) findViewById(R.id.imageViewArrow);

        //this.haveAccelerometer = this.mSensorManager.registerListener(mSensorEventListener, this.mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        //this.haveMagnetometer = this.mSensorManager.registerListener(mSensorEventListener, this.mMagnetometer, SensorManager.SENSOR_DELAY_GAME);

        if (haveAccelerometer && haveMagnetometer) {
            // ready to go
        } else {
            // unregister and stop
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }

        /* if (mCompassPositionDegree != mCompassDegree) {
            float d = Math.abs(mCompassPositionDegree - mCompassDegree);
            float s = (mCompassDegree - mCompassPositionDegree) / d;
            if (d > 180) {
                s = -s;
                d = 360 - d;
            }
            mCompassPositionDegree += s * 0.3f * d;
            if (mCompassPositionDegree < 0) {
                mCompassPositionDegree = 360 - mCompassPositionDegree;
            }
        } */

        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;
            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree, -azimuthInDegress, // z pozice do pozice
                    Animation.RELATIVE_TO_SELF, 0.5f,  // rotace kolem středu obrázku
                    Animation.RELATIVE_TO_SELF, 0.5f); // rotace kolem středu obrázku

            ra.setDuration(250);
            ra.setFillAfter(true);

            imageViewArrow.startAnimation(ra);
            mCurrentDegree = -azimuthInDegress;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }
}
