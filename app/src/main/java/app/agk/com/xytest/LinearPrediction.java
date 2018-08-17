package app.agk.com.xytest;

import android.util.Log;

public class LinearPrediction implements AngleAnalyzer {
    private static final String L_TAG = LinearPrediction.class.getSimpleName();

    private double mAngle;
    private double mSpeed;
    private double mDir = 1d;

    @Override
    public void restart(double aAngle) {
        mAngle = aAngle;
        mSpeed = 0d;
    }

    @Override
    public void step(double aDelta, double aAngle) {
        if ( aAngle < 0 ) {
            throw new IllegalArgumentException("The angle can be positive only.");
        }

        aAngle = aAngle * mDir;
        double prediction = (Math.abs(mSpeed) > 2) ? mAngle + mSpeed * aDelta * 1.5: aAngle;

        Log.v(L_TAG, "Angle: " + aAngle
                + " Predict: " + prediction
                + " Prev speed: " + mSpeed);

        if (Math.signum(prediction) != Math.signum(aAngle)) {
            Log.v(L_TAG, "Flip: " + prediction + " " + aAngle
                    + " Signs: " +  Math.signum(prediction) + " != " + Math.signum(aAngle));
            mDir = - mDir;
            aAngle = -aAngle;
        }

        mSpeed = (aAngle - mAngle) / aDelta;;
        mAngle = aAngle;
    }
}
