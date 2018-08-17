package app.agk.com.xytest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

public class SensorsService extends Service {
    private boolean mStarted;
    private Detector mDetector;

    public SensorsService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!mStarted) {
            mDetector = new Detector();
            mStarted = true;
            SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            sensorManager.registerListener(mDetector, sensor, SensorManager.SENSOR_DELAY_GAME);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent aIntent) {
        return null;
    }

    public static void startService(Context aContext) {
        aContext.startService(new Intent(aContext, SensorsService.class));
    }
}
