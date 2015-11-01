package com.coredump.synergyar.android.configuration;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * @author fabio
 * @version 0.0.1
 * @see Camera
 */
public class CameraPreviewKitKat extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CameraPreview(old)";
    private Camera camera;
    private SurfaceHolder holder;



    private static boolean hasCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static Camera getCamera(){
        Log.d(TAG, "Getting camera");
        Camera camera = null;
        //@// TODO: 10/31/15 algorithm to wait response for camera
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return camera;
    }

    public CameraPreviewKitKat(Context context, Camera pcamera) {
        super(context);
        camera = pcamera;
        Log.d(TAG, "Constructor");
        initialize();

    }

    public CameraPreviewKitKat(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "Constructor");
        initialize();
    }

    public CameraPreviewKitKat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Log.d(TAG, "Constructor");
        initialize();
    }

    private void initialize() {
        Log.d(TAG, "Initializing object");
        //monitor changes to the surface
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "Create surface");
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview", e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "Change surface");
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (holder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            camera = null;
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Destroy surface");
        //Release camera preview on synergy activity

    }

    //@todo Needs to be declared on an interface
    public void startPreview() {

    }

    //@todo Needs to be declared on an interface
    public void stopPreview() {

    }
    
    //@todo Needs to be declared on an interface
    public static void releaseCamera() {

    }
}