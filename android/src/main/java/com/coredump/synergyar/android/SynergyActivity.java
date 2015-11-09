package com.coredump.synergyar.android;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.coredump.synergyar.android.augmentedview.CameraPreview;
import com.coredump.synergyar.android.configuration.camera.CameraController;
import com.coredump.synergyar.configuration.DeviceCameraController;
import com.coredump.synergyar.configuration.SynergyAdapter;

import java.io.IOException;
import java.util.List;

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

        //@// TODO: 11/3/15 remove this
        /*setContentView(R.layout.activity_main);
        mCameraPreview = (CameraPreview) findViewById(R.id.view_camera);
        mCameraPreview.getHolder().addCallback(this);
        //Only for API<3.0
        mCameraPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        Log.d(TAG, "BeforeCamera");
        int cameraFacingBack = Camera.CameraInfo.CAMERA_FACING_BACK;
        mCamera = Camera.open(cameraFacingBack);
        Log.d(TAG, "AfterCamera");
        */
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

    /*
    //Surface callback Methods
    //@// TODO: 11/3/15 Move this to other class
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(mCameraPreview.getHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters params = mCamera.getParameters();
//Get the device's supported sizes and pick the first,
// which is the largest
        List<Camera.Size> sizes =
                params.getSupportedPreviewSizes();
        Camera.Size selected = sizes.get(0);
        params.setPreviewSize(selected.width,selected.height);
        mCamera.setParameters(params);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }*/
}
