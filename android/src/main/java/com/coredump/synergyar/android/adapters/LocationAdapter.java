package com.coredump.synergyar.android.adapters;

import com.coredump.synergyar.android.sensors.geolocation.LocationSensorListener;

import android.location.Location;

/**
 * @author francisco
 * @version 0.0,1
 * Created by Franz on 30/10/2015.
 */
public interface LocationAdapter {

    Location getLocation();
    void startRequestLocationUpdates(LocationSensorListener locationSensorListener);
    void removeUpdates();
}