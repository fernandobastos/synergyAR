package com.coredump.synergyar.android.augmentedview;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Class for displaying the camera view
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 * @see Camera
 * @see SurfaceHolder
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = CameraPreview.class.getName();
    //device camera
    private Camera mCamera;
    //pass image data from the camera to the application
    private SurfaceHolder holder;

    public CameraPreview(Context context) {
        super(context);
        Log.d(TAG, "Constructor");
        initialize();
    }

    /**
     * Releases the camera object and cleans the
     * {@link Camera} field
     */
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
        Log.d(TAG, "Stop preview");
        // stop preview before making changes
        mCamera.stopPreview();

    }

    public Camera getCamera() {
        return mCamera;
    }

    /**
     * should just 'start up' rendering code
     * normal rendering will be in another thread.
     */
     @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "Create surface");
         int cameraFacingBack = Camera.CameraInfo.CAMERA_FACING_BACK;
         try {
            mCamera = CameraPreview.safeCameraOpen(cameraFacingBack, this.getContext());
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.e(TAG, "Error setting Camera preview", e);
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

        //Get the device's supported sizes and pick the first,
        // which is the largest
        List<Camera.Size> sizes =
                params.getSupportedPreviewSizes();
        Camera.Size selected = sizes.get(0);

        params.setPreviewSize(selected.width,selected.height);
        mCamera.setParameters(params);

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(holder);
            Log.d(TAG, "StarPreview");
            mCamera.startPreview();
        } catch (IOException e) {
            Log.e(TAG, "Error starting mCamera preview", e);
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
     * Checks if the device has a {@link Camera) needs
     * the {@link Context} to get the {@link PackageManager}
     * <p> This method should be run before the opening the camera
     *
     * @param context application context, needed to check device hardware
     *               using the PackageManager.
     */
    private static boolean hasCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * This class initializes the camera object in a safe way
     *
     * <p>The {@link #surfaceCreated(SurfaceHolder)} method uses this method to
     * get an instance of the {@link Camera}
     * @param cameraId identifies the device camera
     * @param context check the existence of the camera
     * @return a Camera instance
     */
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