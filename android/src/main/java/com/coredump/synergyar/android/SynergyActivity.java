package com.coredump.synergyar.android;


import android.graphics.PixelFormat;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.coredump.synergyar.android.adapters.LocationAdapter;
import com.coredump.synergyar.android.adapters.LocationAdapterImpl;
import com.coredump.synergyar.android.adapters.OrientationAdapter;
import com.coredump.synergyar.android.adapters.OrientationAdapterImpl;
import com.coredump.synergyar.android.ar.PerspectiveAR;
import com.coredump.synergyar.android.camera.CameraController;
import com.coredump.synergyar.android.camera.CameraPreview;
import com.coredump.synergyar.android.camera.Preview;
import com.coredump.synergyar.android.sensors.geolocation.LocationSensorListener;
import com.coredump.synergyar.configuration.DeviceCameraController;
import com.coredump.synergyar.configuration.SynergyAdapter;

/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 */

public class SynergyActivity extends AndroidApplication implements LocationSensorListener {
    private static final String TAG = SynergyActivity.class.getName();
    private int mOrigWidth;
    private int mOrigHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useGLSurfaceView20API18 = false;
        // we need to change the default pixel format - since it does not include an alpha channel
        // we need the alpha channel so the camera preview will be seen behind the GL scene
        configuration.r = 8;
        configuration.g = 8;
        configuration.b = 8;
        configuration.a = 8;

        initializeApp(configuration);
    }

    private void initializeApp(AndroidApplicationConfiguration configuration) {

        // TODO: 11/15/15 inject this initialization by configuration
        Preview preview = new CameraPreview(this);
        DeviceCameraController cameraController = new CameraController(this, preview);
        PerspectiveCamera perspectiveCamera = new PerspectiveAR(this);
        initialize(new SynergyAdapter(cameraController,perspectiveCamera), configuration);
        if (graphics.getView() instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) graphics.getView();
            // @// TODO: 11/8/15 check this comment
            // force alpha channel - I'm not sure we need this as the GL surface is already using alpha channel
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        }
        // we don't want the screen to turn off during the long image saving process
        graphics.getView().setKeepScreenOn(true);
        // keep the original screen size
        mOrigHeight = graphics.getWidth();
        mOrigWidth = graphics.getHeight();

        //Location
        final LocationAdapter locationSensor = new LocationAdapterImpl(this);
        Location lastKnownLocation = locationSensor.getLocation();
        showToast("Lat: " + lastKnownLocation.getLatitude() + " | Long: " +
                lastKnownLocation.getLongitude());

        locationSensor.startRequestLocationUpdates(this);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                locationSensor.removeUpdates();
            }
        }, 40000);

        //Para probar la orientacion imprimiendo matriz que se actualiza con los sensores
        final OrientationAdapter orientationAdapter = new OrientationAdapterImpl(this);
        orientationAdapter.startOrientationSensor();
        orientationAdapter.getOrientation();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "OnDestroy");
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "OnPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "OnResume");
        super.onResume();
        //restart camera
    }

    public void post(Runnable r) {
        handler.post(r);
    }

    public void setFixedSize(int width, int height) {
        if (graphics.getView() instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) graphics.getView();
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            glView.getHolder().setFixedSize(width, height);
        }
    }

    public void restoreFixedSize() {
        if (graphics.getView() instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) graphics.getView();
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            glView.getHolder().setFixedSize(mOrigWidth, mOrigHeight);
        }
    }

    @Override
    public void locationChanged(Location location) {
        showToast("Lat: " + location.getLatitude() + " | Long: " + location.getLongitude());
    }

    private void showToast(String message) {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
