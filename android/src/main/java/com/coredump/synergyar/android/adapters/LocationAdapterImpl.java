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

    public LocationAdapterImpl(Context context) {
        mlocationAdaptee = new LocationSensor(context);
    }

    @Override
    public Location getLocation() {
        return mlocationAdaptee.getLocation();
    }

    @Override
    public void startRequestLocationUpdates(LocationSensorListener locationSensorListener) {
        mlocationAdaptee.startRequestLocationUpdates(locationSensorListener);
    }

    @Override
    public void removeUpdates() {
        mlocationAdaptee.removeUpdates();
    }
}