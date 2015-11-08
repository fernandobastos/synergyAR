package com.coredump.synergyar.android.augmentedview;

import com.coredump.synergyar.android.R;

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
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CameraSurfaceKitKat";

    private Camera mCamera;
    private SurfaceHolder holder;

    public CameraPreview(Context context, Camera pcamera) {
        super(context);
        mCamera = pcamera;
        Log.d(TAG, "Constructor");
        initialize();

    }

    private void releaseCamera() {
        if(mCamera != null){
            //Disconnects and releases the Camera object resources.
            mCamera.release();
            //Cleans the camera reference
            mCamera = null;
        }
    }

    private void initialize() {
        Log.d(TAG, "Initializing object");
        //monitor changes to the surface
        holder = getHolder();
        holder.addCallback(this);
        // We're changing the surface to a PUSH surface, meaning we're receiving
        // all buffer data from another component - the mCamera, in this case.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void stopPreview() {
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        Log.d(TAG, "Stop preview");
    }

    public Camera getCamera() {
        return mCamera;
    }

    public void setCamera(Camera mCamera) {
        this.mCamera = mCamera;
    }


    /**should just 'start up' rendering code
    * normal rendering will be in another thread.
    **/
     @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "Create surface");
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting Camera preview", e);
        }
    }

    // This method is called when the surface changes, e.g. when it's size is set.
    // We use the opportunity to initialize the mCamera preview display dimensions.
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "Change surface");

        //if there is no surface return
        if (holder.getSurface() == null) {
            return;
        }

        // Stop the preview before resizing or reformatting it.
        stopPreview();

        // set preview size and make any resize, rotate or
        // reformatting changes here
        Camera.Parameters params = mCamera.getParameters();
        params.setPreviewSize( width, height );
        mCamera.setParameters(params);

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error starting mCamera preview", e);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Destroy surface");
        //Release mCamera preview on synergy activity
        mCamera.stopPreview();
        releaseCamera();
        Log.d(TAG, "Clean Camera");
    }

    /**
     * @// TODO: 11/7/15 put this on controller
     * Check if device has a mCamera
     *****************************************************************************************/
    private static boolean hasCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static Camera safeCameraOpen(int cameraId, Context context){
        //useful if we need to send a notification to the user
        Log.d(TAG, "Getting mCamera");
        Camera camera = null;
        //@// TODO: 10/31/15 algorithm to wait response for mCamera
        if(hasCameraHardware(context))
        camera = Camera.open(cameraId);
        return camera;
    }
    /****************************************************************************************/
}