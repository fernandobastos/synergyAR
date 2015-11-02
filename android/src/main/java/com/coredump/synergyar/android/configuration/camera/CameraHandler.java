package com.coredump.synergyar.android.configuration.camera;

import java.util.List;

import com.coredump.synergyar.DeviceCameraController;
import com.coredump.synergyar.android.augmentedview.CameraPreviewKitKat;
import com.coredump.synergyar.android.configuration.SynergyActivity;

import android.hardware.Camera;
import android.view.Display;
import android.view.ViewGroup;
import android.view.ViewParent;

//DeviceCameraController
/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 */
public class CameraHandler implements  Camera.PictureCallback, Camera.AutoFocusCallback {

    private static final int ONE_SECOND_IN_MILI = 1000;
    private final SynergyActivity activity;
    private CameraPreviewKitKat cameraPreview;
    private byte[] pictureData;

    public CameraHandler(SynergyActivity activity) {
        this.activity = activity;
    }


    public synchronized void prepareCamera() {
        Display display = activity.getWindowManager().getDefaultDisplay();
        activity.setFixedSize(display.getWidth(), display.getHeight());

        if (cameraPreview == null) {
            cameraPreview = new CameraPreviewKitKat(activity);
        }
        // activity.addContentView( cameraPreview, new LayoutParams(
        // LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
        activity.addView(cameraPreview, 0);
    }

    public synchronized void startPreview() {
        // ...and start previewing. From now on, the camera keeps pushing
        // preview
        // images to the surface.
        if (cameraPreview != null && cameraPreview.getCamera() != null) {
            cameraPreview.getCamera().startPreview();
        }
    }


    public synchronized void stopPreview() {
        // stop previewing.
        if (cameraPreview != null) {
            ViewParent parentView = cameraPreview.getParent();
            if (parentView instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) parentView;
                viewGroup.removeView(cameraPreview);

            }
            if (cameraPreview.getCamera() != null) {
                cameraPreview.getCamera().stopPreview();
            }
            cameraPreview = null;
        }
        activity.restoreFixedSize();
    }

    public void setCameraParametersForPicture(Camera camera) {
        // Before we take the picture - we make sure all camera parameters are
        // as we like them
        // Use max resolution and auto focus
        Camera.Parameters p = camera.getParameters();
        List<Camera.Size> supportedSizes = p.getSupportedPictureSizes();
        int maxSupportedWidth = -1;
        int maxSupportedHeight = -1;
        for (Camera.Size size : supportedSizes) {
            if (size.width > maxSupportedWidth) {
                maxSupportedWidth = size.width;
                maxSupportedHeight = size.height;
            }
        }
        p.setPictureSize(maxSupportedWidth, maxSupportedHeight);
        p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(p);
    }

    @Override
    public synchronized void onAutoFocus(boolean success, Camera camera) {
        // Focus process finished, we now have focus (or not)
        if (success) {
            if (camera != null) {
                camera.stopPreview();
                // We now have focus take the actual picture
                camera.takePicture(null, null, null, this);
            }
        }
    }


    public void prepareCameraAsync() {
        Runnable r = new Runnable() {
            public void run() {
                prepareCamera();
            }
        };
        activity.post(r);
    }


    public synchronized void startPreviewAsync() {
        Runnable r = new Runnable() {
            public void run() {
                startPreview();
            }
        };
        activity.post(r);
    }


    public synchronized void stopPreviewAsync() {
        Runnable r = new Runnable() {
            public void run() {
                stopPreview();
            }
        };
        activity.post(r);
    }


    public boolean isReady() {
        if (cameraPreview != null && cameraPreview.getCamera() != null) {
            return true;
        }
        return false;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {


    }
}