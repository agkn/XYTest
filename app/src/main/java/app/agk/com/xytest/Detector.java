package app.agk.com.xytest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class Detector implements SensorEventListener {
    private static final String L_TAG = Detector.class.getSimpleName();

    private static final double TIME_SCALE = 1 / 1.0E+9;
    private final LinearPrediction mLinearAnalyzer;
    private float[] mGravity = new float[3];
    private long mTimeStamp;

    public Detector() {
        mLinearAnalyzer = new LinearPrediction();
    }

    @Override
    public void onSensorChanged(SensorEvent aSensorEvent) {
        // Log.v(L_TAG, "Date readed: " + Arrays.toString(aSensorEvent.values));

        final float alpha = 0.7f;

        // Isolate the force of mGravity with the low-pass filter.
        mGravity[0] = alpha * mGravity[0] + (1 - alpha) * aSensorEvent.values[0];
        mGravity[1] = alpha * mGravity[1] + (1 - alpha) * aSensorEvent.values[1];
        mGravity[2] = alpha * mGravity[2] + (1 - alpha) * aSensorEvent.values[2];

        // Log.v(L_TAG, "Date readed: " + Arrays.toString(mGravity));

        double full = Math.sqrt(mGravity[0] * mGravity[0] + mGravity[1] * mGravity[1] + mGravity[2] * mGravity[2]);
        double pxy = Math.sqrt(mGravity[0] * mGravity[0] + mGravity[1] * mGravity[1]);
//        double angle = Math.acos(pxy/full) / Math.PI * 180;
        double angle = pxy > 9.8 ? 0.0d : Math.acos(pxy / full) / Math.PI * 180;

        if (mTimeStamp > 0) {
            mLinearAnalyzer.step((aSensorEvent.timestamp - mTimeStamp) * TIME_SCALE, angle);
        } else {
            mLinearAnalyzer.restart(angle);
        }

        mTimeStamp = aSensorEvent.timestamp;
    }

    @Override
    public void onAccuracyChanged(Sensor aSensor, int aI) {
        
    }
}
