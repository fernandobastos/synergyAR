package com.coredump.synergyar.android.configuration.camera;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.coredump.synergyar.configuration.DeviceCameraController;
import com.coredump.synergyar.android.augmentedview.CameraPreview;
import com.coredump.synergyar.android.SynergyActivity;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
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
public class CameraController implements DeviceCameraController, Camera.PictureCallback, Camera.AutoFocusCallback {

    private static final String TAG = "CameraController";
    private static final int ONE_SECOND_IN_MILI = 1000;
    private final SynergyActivity mActivity;
    private CameraPreview mCameraPreview;
    private byte[] mPictureData;

    public CameraController(SynergyActivity mActivity) {
        Log.d(TAG, "Constructor");
        this.mActivity = mActivity;
    }

    @Override
    public synchronized void prepareCamera() {
        Log.d(TAG,"Sync PrepareCamera");
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        //mActivity.setFixedSize(display.getWidth(), display.getHeight());

        if (mCameraPreview == null) {
            mCameraPreview = new CameraPreview(mActivity, CameraPreview.safeCameraOpen(1, mActivity));
        }
        // mActivity.addContentView( mCameraPreview, new LayoutParams(
        // LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
        //@// TODO: 11/2/15 This only works on API>10
        mActivity.addContentView(mCameraPreview, new ActionBar.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ));
    }

    @Override
    public synchronized void startPreview() {
        Log.d(TAG,"Sync StartPreview");
        // ...and start previewing. From now on, the camera keeps pushing
        // preview
        // images to the surface.
        if (mCameraPreview != null && mCameraPreview.getCamera() != null) {
            mCameraPreview.getCamera().startPreview();
        }
    }

    @Override
    public synchronized void stopPreview() {
        Log.d(TAG,"Sync StopPreview");
        // stop previewing.
        if (mCameraPreview != null) {
            ViewParent parentView = mCameraPreview.getParent();
            if (parentView instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) parentView;
                viewGroup.removeView(mCameraPreview);

            }
            if (mCameraPreview.getCamera() != null) {
                mCameraPreview.getCamera().stopPreview();
            }
            mCameraPreview = null;
        }
        mActivity.restoreFixedSize();
    }

    @Override
    public void takePicture() {
        // the user request to take a picture - start the process by requesting focus
        setCameraParametersForPicture(mCameraPreview.getCamera());
        mCameraPreview.getCamera().autoFocus(this);
    }

    @Override
    public byte[] getPictureData() {
        return new byte[0];
    }

    private void setCameraParametersForPicture(Camera camera) {
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
        Runnable runnable = new Runnable() {
            public void run() {
                prepareCamera();
            }
        };
        mActivity.post(runnable);
    }

    @Override
    public synchronized void startPreviewAsync() {
        Log.d(TAG, "Async PreviewAsync");
        Runnable runnable = new Runnable() {
            public void run() {
                startPreview();
            }
        };
        mActivity.post(runnable);
    }

    @Override
    public synchronized void stopPreviewAsync() {
        Log.d(TAG,"ASync StopPreview");
        Runnable runnable = new Runnable() {
            public void run() {
                stopPreview();
            }
        };
        mActivity.post(runnable);
    }

    @Override
    public byte[] takePictureAsync(long timeout) {
        timeout *= ONE_SECOND_IN_MILI;
        mPictureData = null;
        Runnable r = new Runnable() {
            public void run() {
                takePicture();
            }
        };
        mActivity.post(r);
        while (mPictureData == null && timeout > 0) {
            try {
                Thread.sleep(ONE_SECOND_IN_MILI);
                timeout -= ONE_SECOND_IN_MILI;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (mPictureData == null) {
            mCameraPreview.getCamera().cancelAutoFocus();
        }
        return mPictureData;
    }

    @Override
    public void saveAsJpeg(FileHandle jpgfile, Pixmap cameraPixmap) {
        FileOutputStream fos;
        int x=0,y=0;
        int xl=0,yl=0;
        try {
            Bitmap bmp = Bitmap.createBitmap(cameraPixmap.getWidth(), cameraPixmap.getHeight(), Bitmap.Config.ARGB_8888);
            // we need to switch between LibGDX RGBA format to Android ARGB format
            for (x=0,xl=cameraPixmap.getWidth(); x<xl;x++) {
                for (y=0,yl=cameraPixmap.getHeight(); y<yl;y++) {
                    int color = cameraPixmap.getPixel(x, y);
                    // RGBA => ARGB
                    int RGB = color >> 8;
                    int A = (color & 0x000000ff) << 24;
                    int ARGB = A | RGB;
                    bmp.setPixel(x, y, ARGB);
                }
            }
            fos = new FileOutputStream(jpgfile.file());
            bmp.compress(CompressFormat.JPEG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "Error file not found", e);
        } catch (IOException e) {
            Log.d(TAG, "Error while saving image", e);
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Error while saving image", e);
        }
    }

    @Override
    public boolean isReady() {
        Log.d(TAG, "Is Ready?");
        if (mCameraPreview != null && mCameraPreview.getCamera() != null) {
            return true;
        }
        return false;
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

    @Override
    public synchronized void onPictureTaken(byte[] pictureData, Camera camera) {
        // We got the picture data - keep it
        this.mPictureData = pictureData;
    }
}