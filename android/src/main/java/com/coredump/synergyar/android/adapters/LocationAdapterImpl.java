package com.coredump.synergyar.android.adapters;

import android.content.Context;
import android.location.Location;

import com.coredump.synergyar.android.sensors.geolocation.LocationSensor;
import com.coredump.synergyar.android.sensors.geolocation.LocationSensorListener;

/**
 * Created by Franz on 29/10/2015.
 */
public class LocationAdapterImpl implements LocationAdapter {

    private LocationSensor mlocationAdaptee;

    public LocationAdapterImpl(Context context, LocationSensorListener locationSensorListener) {
        mlocationAdaptee = new LocationSensor(context, locationSensorListener);
    }

    @Override
    public Location getLocation() {
        return mlocationAdaptee.getLocation();
    }

}