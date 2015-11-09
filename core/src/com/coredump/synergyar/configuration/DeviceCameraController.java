package com.coredump.synergyar.configuration;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 * Created by fabio on 11/1/15.
 */

public interface DeviceCameraController {

    // Synchronous interface
    void saveAsJpeg(FileHandle jpgfile, Pixmap cameraPixmap);

    boolean isReady();

    void prepareCamera();

    void startPreview();

    void stopPreview();

    void takePicture();

    byte[] getPictureData();

    /** Asynchronous interface - need when called from a non platform thread (GDX OpenGl thread)
     * called asynchronous from the Libgdx rendering thread
     */
    void prepareCameraAsync();

    /** Asynchronous interface - need when called from a non platform thread (GDX OpenGl thread)
     * called asynchronous from the Libgdx rendering thread
     */
    void startPreviewAsync();

    /** Asynchronous interface - need when called from a non platform thread (GDX OpenGl thread)
     * called asynchronous from the Libgdx rendering thread
     */
    void stopPreviewAsync();

    /** Asynchronous interface - need when called from a non platform thread (GDX OpenGl thread)
     * called asynchronous from the Libgdx rendering thread
     */
    byte[] takePictureAsync(long timeout);

}
