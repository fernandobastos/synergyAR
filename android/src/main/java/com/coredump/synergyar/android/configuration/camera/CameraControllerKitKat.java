package com.coredump.synergyar.android.configuration.camera;

import java.util.List;

import com.coredump.synergyar.DeviceCameraController;
import com.coredump.synergyar.android.augmentedview.CameraSurfaceKitKat;
import com.coredump.synergyar.android.SynergyActivity;

import android.app.ActionBar;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewGroup.LayoutParams;

//DeviceCameraController
/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 */
public class CameraControllerKitKat implements  DeviceCameraController {

    private static final String TAG = "CameraControllerKitKat";
    private static final int ONE_SECOND_IN_MILI = 1000;
    private final SynergyActivity mactivity;
    private CameraSurfaceKitKat mcameraSurface;

    public CameraControllerKitKat(SynergyActivity mactivity) {
        Log.d(TAG, "Constructor");
        this.mactivity = mactivity;
    }

    @Override
    public synchronized void prepareCamera() {
        Log.d(TAG,"Sync PrepareCamera");
        Display display = mactivity.getWindowManager().getDefaultDisplay();
        //mactivity.setFixedSize(display.getWidth(), display.getHeight());

        if (mcameraSurface == null) {
            mcameraSurface = new CameraSurfaceKitKat(mactivity, CameraSurfaceKitKat.initializeCamera());
        }
        // mactivity.addContentView( mcameraSurface, new LayoutParams(
        // LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
        //@// TODO: 11/2/15 This only works on API>10
        mactivity.addContentView(mcameraSurface, new ActionBar.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ));
    }

    @Override
    public synchronized void startPreview() {
        Log.d(TAG,"Sync StartPreview");
        // ...and start previewing. From now on, the camera keeps pushing
        // preview
        // images to the surface.
        if (mcameraSurface != null && mcameraSurface.getCamera() != null) {
            mcameraSurface.getCamera().startPreview();
        }
    }

    @Override
    public synchronized void stopPreview() {
        Log.d(TAG,"Sync StopPreview");
        // stop previewing.
        if (mcameraSurface != null) {
            ViewParent parentView = mcameraSurface.getParent();
            if (parentView instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) parentView;
                viewGroup.removeView(mcameraSurface);

            }
            if (mcameraSurface.getCamera() != null) {
                mcameraSurface.getCamera().stopPreview();
            }
            mcameraSurface = null;
        }
        //mactivity.restoreFixedSize();
    }

    public void setCameraParametersForPicture(Camera camera) {
        // Before we take the picture - we make sure all camera parameters are
        // as we like them
        // Use max resolution and auto focus
        Camera.Parameters params = camera.getParameters();
        List<Camera.Size> supportedSizes = params.getSupportedPictureSizes();
        int maxSupportedWidth = -1;
        int maxSupportedHeight = -1;
        for (Camera.Size size : supportedSizes) {
            if (size.width > maxSupportedWidth) {
                maxSupportedWidth = size.width;
                maxSupportedHeight = size.height;
            }
        }
        params.setPictureSize(maxSupportedWidth, maxSupportedHeight);
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(params);
    }

    //async methods
    @Override
    public void prepareCameraAsync() {
        Log.d(TAG,"Async PrepareCamera");
        Runnable r = new Runnable() {
            public void run() {
                prepareCamera();
            }
        };
        //mactivity.post(r);
    }

    @Override
    public synchronized void startPreviewAsync() {
        Log.d(TAG,"Async PreviewAsync");
        Runnable r = new Runnable() {
            public void run() {
                startPreview();
            }
        };
        //mactivity.post(r);
    }

    @Override
    public synchronized void stopPreviewAsync() {
        Log.d(TAG,"ASync StopPreview");
        Runnable r = new Runnable() {
            public void run() {
                stopPreview();
            }
        };
        //mactivity.post(r);
    }

    @Override
    public boolean isReady() {
        Log.d(TAG,"Is Ready?");
        if (mcameraSurface != null && mcameraSurface.getCamera() != null) {
            return true;
        }
        return false;
    }
}