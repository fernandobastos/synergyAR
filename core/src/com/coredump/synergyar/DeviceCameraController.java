package com.coredump.synergyar;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

/**
 * @author fabio
 * @version 0.0.1
 * Created by fabio on 11/1/15.
 */

public interface DeviceCameraController {

    // Synchronous interface
    void prepareCamera();

    void startPreview();

    void stopPreview();

    void takePicture();

    byte[] getPictureData();

    // Asynchronous interface - need when called from a non platform thread (GDX OpenGl thread)
    void startPreviewAsync();

    void stopPreviewAsync();

    byte[] takePictureAsync(long timeout);

    void saveAsJpeg(FileHandle jpgfile, Pixmap cameraPixmap);

    boolean isReady();

    void prepareCameraAsync();
}
