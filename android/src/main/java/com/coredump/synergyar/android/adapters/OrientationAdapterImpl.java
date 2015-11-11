package com.coredump.synergyar.android.adapters;

import android.content.Context;

import com.coredump.synergyar.android.sensors.orientation.OrientationListener;
import com.coredump.synergyar.android.sensors.orientation.OrientationSensor;

/**
 * Created by fernando on 08-Nov-15.
 */
public class OrientationAdapterImpl implements OrientationAdapter {

    private OrientationSensor mOrientationAdaptee;

    public OrientationAdapterImpl(Context context){
        mOrientationAdaptee = new OrientationSensor(context);

    }

    @Override
    public float[] getOrientation() {
        return mOrientationAdaptee.getMatrix();
    }

    public void startOrientationSensor(){
        mOrientationAdaptee.start();
    }

    public void stopOrientationSensor(){
        mOrientationAdaptee.finish();
    }

}
