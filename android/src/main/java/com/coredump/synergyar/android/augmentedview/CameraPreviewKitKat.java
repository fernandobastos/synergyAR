package com.coredump.synergyar.android.augmentedview;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 * @see Camera
 */
public class CameraPreviewKitKat extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CameraPreview(old)";
    private Camera camera;
    private SurfaceHolder holder;



    private static boolean hasCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static Camera initializeCamera(){
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

    public CameraPreviewKitKat(Context context) {
        super(context);
        initialize();
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
        // We're changing the surface to a PUSH surface, meaning we're receiving
        // all buffer data from another component - the camera, in this case.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    /**should just 'start up' rendering code
    * normal rendering will be in another thread.
    **/
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

    // This method is called when the surface changes, e.g. when it's size is set.
    // We use the opportunity to initialize the camera preview display dimensions.
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "Change surface");
        //if there is no surface return
        if (holder.getSurface() == null){
            // preview surface does not exist
            return;
        }
        // Stop the preview before resizing or reformatting it.
        stopPreview();

        // set preview size and make any resize, rotate or
        // reformatting changes here
        Camera.Parameters params = camera.getParameters();
        params.setPreviewSize( width, height );
        camera.setParameters(params);

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
        camera.stopPreview();
        camera.release();
        camera = null;
        Log.d(TAG, "Clean camera");
    }

    private void stopPreview() {
        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }
        Log.d(TAG, "Stop preview");
    }


    //@todo Needs to be declared on an interface
    public void startPreview() {
        surfaceCreated(holder);
    }

    //@todo Needs to be declared on an interface
    public void releaseCamera() {
        surfaceDestroyed(holder);
    }

    //@todo Needs to be declared on an interface
    public Camera getCamera() {
        return camera;
    }

}