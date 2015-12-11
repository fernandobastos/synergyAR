package com.coredump.synergyar.android.sensors.orientation;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.badlogic.gdx.math.Matrix4;
import com.coredump.synergyar.ar.hardware.OrientationSensor;
import com.coredump.synergyar.util.BufferAlgo;

import java.util.concurrent.Semaphore;

/**
 * @author fernando, 08-Nov-15
 * @version 0.0.1
 * @since 0.0.1
 */
public class AndroidOrientationSensor extends OrientationSensor {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private Sensor mOrientationSensor;
    private Sensor mSensorLinAcce;
    private SensorEventListener mListener;
    private Semaphore mClear = new Semaphore(1);
    private float mFar;
    private float mOrientation[] = new float[3];
    private float mAcceleration[] = new float[3];
    private float mOldOrientation[];
    private float mOldAcceleration[];
    private float[] mNewMat;
    private Matrix4 mMatT;
    private BufferAlgo mAccelerometerBuffer;
    private BufferAlgo mMagnetometerBuffer;
    private boolean mQuit;
    private boolean mStable;

    public AndroidOrientationSensor(Context context, float mFar, boolean isMarker) {
        mSensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);

        mAccelerometerBuffer = new BufferAlgo(0.1f, 0.2f);
        mMagnetometerBuffer = new BufferAlgo(0.1f, 0.2f);
        this.mFar = mFar;
    }
    public AndroidOrientationSensor(Context context){
        this(context, 1000, true);
    }

    @Override
    public void start() {
        mListener = new MyOrientationListener();

        mAccelerometer = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        mMagnetometer = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);

        mSensorManager.registerListener(mListener, mAccelerometer,
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(mListener, mMagnetometer,
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(mListener, mOrientationSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(mListener, mSensorLinAcce,
                SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    public boolean getStable() {
        return mStable;
    }

    @Override
    public void finish() {
        try {
            mClear.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mSensorManager.unregisterListener(mListener);
        mSensorManager = null;
        mAccelerometer = null;
        mMagnetometer = null;
        mListener = null;
        mOrientationSensor = null;
        mSensorLinAcce = null;
        mOrientation = null;
        mAcceleration = null;
        mOldOrientation = null;
        mOldAcceleration = null;
        mMatT = null;
        mNewMat = null;
        mQuit = true;
        mClear.release();
        super.finish();
    }

    public class MyOrientationListener implements SensorEventListener {

        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        public void onSensorChanged(SensorEvent evt) {
            if (mClear.tryAcquire()) {
                if (!mQuit) {
                    int type = evt.sensor.getType();
                    // Smoothing the sensor data a bit seems like a good
                    // idea.
                    if (type == Sensor.TYPE_MAGNETIC_FIELD) {
                        mOrientation = lowPass(evt.values, mOrientation, 0.1f);
                    } else if (type == Sensor.TYPE_ACCELEROMETER) {

                        mAcceleration = lowPass(evt.values, mAcceleration,
                                0.05f);
                    }
                    if (mOldAcceleration != null || mOldOrientation != null) {
                        mAccelerometerBuffer.execute(mOldAcceleration,
                                mAcceleration);
                        mMagnetometerBuffer.execute(mOldOrientation,
                                mOrientation);
                    } else {
                        mOldAcceleration = mAcceleration;
                        mOldOrientation = mOrientation;
                    }

                    if ((type == Sensor.TYPE_MAGNETIC_FIELD)
                            || (type == Sensor.TYPE_ACCELEROMETER)) {
                        mNewMat = new float[16];
                        SensorManager.getRotationMatrix(mNewMat, null,
                                mOldAcceleration, mOldOrientation);
                        SensorManager.remapCoordinateSystem(mNewMat,
                                SensorManager.AXIS_Y,
                                SensorManager.AXIS_MINUS_X, mNewMat);
                        mMatT = new Matrix4(mNewMat).tra();
                        float[] newLookAt = { 0, 0, -mFar, 1 };
                        float[] newUp = { 0, 1, 0, 1 };
                        float[] position = getPosition();

                        Matrix4.mulVec(mMatT.val, newLookAt);
                        Matrix4.mulVec(mMatT.val, newUp);
                        setMatrix(mMatT.val);

                        setLookAtOffset(newLookAt[0] + position[0],
                                newLookAt[1] + position[1],
                                newLookAt[2] + position[2]);

                        setUp(newUp);

                        //clean values for concurrency problems
                        newLookAt = null;
                        newUp = null;
                        position = null;
                    }
                    mClear.release();
                }
            }
        }
    }
}
