package com.coredump.synergyar.android;


import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;

import android.view.SurfaceView;


import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.coredump.synergyar.android.configuration.CameraController;
import com.coredump.synergyar.android.sensors.geolocation.LocationSensor;
import com.coredump.synergyar.android.sensors.geolocation.LocationSensorListener;
import com.coredump.synergyar.configuration.DeviceCameraController;
import com.coredump.synergyar.configuration.SynergyAdapter;

/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 */

public class SynergyActivity extends AndroidApplication {
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
        DeviceCameraController cameraControl = new CameraController(this);
        initialize(new SynergyAdapter(cameraControl), configuration);
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
        //test implementation
        LocationSensor locationSensor = new LocationSensor(this.getContext());
        //locationSensor.startRequestLocationUpdates();
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
}
