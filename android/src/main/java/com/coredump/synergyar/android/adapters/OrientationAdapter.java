package com.coredump.synergyar.android.adapters;

/**
 * Created by fernando on 08-Nov-15.
 */
public interface OrientationAdapter {

    float[] getOrientation();
    void startOrientationSensor();
    void stopOrientationSensor();
}
