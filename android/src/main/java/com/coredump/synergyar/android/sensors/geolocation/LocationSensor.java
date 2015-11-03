package com.coredump.synergyar.android.sensors.geolocation;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * This class abstracts the communication with android Location sensor
 * @author francisco
 * @version 0.0,1
 * @see LocationManager
 * @see LocationListener
 * @see LocationSensorListener
 * Created by Franz on 30/10/2015.
 */
public class LocationSensor {

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private LocationSensorListener mLocationSensorListener;
    private static final int ONE_MINUTE = 1000 * 60 * 1;

    public LocationSensor(Context context, LocationSensorListener locationSensorListener) {
        mLocationSensorListener = locationSensorListener;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MyLocationListener();
        mLocationManager
                .requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
    }

    public Location getLocation() {
        Criteria criteria = new Criteria();
        String bestProvider = mLocationManager.getBestProvider(criteria, true);
        return mLocationManager.getLastKnownLocation(bestProvider);
    }

    public String getBestProvider() {
        Criteria criteria = new Criteria();

        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setSpeedRequired(true);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setCostAllowed(false);

        return mLocationManager.getBestProvider(criteria, true);
    }

    public boolean isBetterLocation(Location newLocation, Location currentBestLocation) {

        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > ONE_MINUTE;
        boolean isSignificantlyOlder = timeDelta < ONE_MINUTE;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;

    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            LocationSensor.this.mLocationSensorListener.locationChanged(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            LocationSensor.this.mLocationSensorListener.locationChanged(new Location("Provider E"));
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
