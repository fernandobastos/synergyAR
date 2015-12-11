package com.coredump.synergyar.ar.hardware;

import com.badlogic.gdx.math.Matrix4;
import com.coredump.synergyar.util.BufferAlgo;

import java.util.concurrent.Semaphore;

/**
 * @author Fabio, 12/11/15
 * @version 0.0.1
 * @since 0.0.1
 */
public abstract class OrientationSensor {
    protected static final float CHANGE_FACT = 3.5f;
    private float mMatrix[] = new float[16];

    private float[] mLookAt = { 0, 0, -100, 1 };
    private float[] mUp = { 0, 1, 0, 1 };
    private float[] mPosition = { 0, 0, 0 };
    private float mRotation[];

    public OrientationSensor() {
    }

    public float[] getOldOrientation() {
        return mRotation;
    }

    //Initializes the sensors and start processing
    public abstract void start();

    public abstract boolean getStable();

    public void finish() {
        mMatrix = null;
        mLookAt = null;
        mUp = null;
        mPosition = null;
        mRotation = null;
    }

    // LowPass Filter
    protected float[] lowPass(float[] input, float[] output, float alpha) {
        if (output == null)
            return input;

        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + alpha * (input[i] - output[i]);
        }
        return output;
    }

    public float[] getMatrix() {
        return mMatrix;
    }

    public float[] getPosition() {
        return mPosition;
    }

    public float[] getLookAt() {
        float[] out = new float[3];
        out[0] = mLookAt[0];
        out[1] = mLookAt[1];
        out[2] = mLookAt[2];
        return out;
    }

    public float[] getUp() {
        float[] out = new float[3];
        out[0] = mUp[0];
        out[1] = mUp[1];
        out[2] = mUp[2];
        return out;
    }

    public void setLookAtOffset(float x, float y, float z) {
        mPosition[0] = x;
        mPosition[1] = y;
        mPosition[2] = z;
    }

    public void setMatrix(float[] value){
        this.mMatrix = value;
    }

    public void setUp(float[] value) {
        this.mUp = value;
    }
}
