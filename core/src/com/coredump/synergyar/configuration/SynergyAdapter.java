package com.coredump.synergyar.configuration;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;

/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 * Created by fabio on 11/1/15.
 */
public class SynergyAdapter implements ApplicationListener {
    private static final String TAG = SynergyAdapter.class.getName();;
    private final DeviceCameraController mDeviceCameraController;
    private Display mDisplay;
    private Mode mode = Mode.normal;
    private PerspectiveCamera mCamera;


    public SynergyAdapter(DeviceCameraController cameraControl,PerspectiveCamera camera) {
        mDeviceCameraController = cameraControl;
        mCamera = camera;
    }

    public enum Mode {
        normal,
        prepare,
        preview
    }

    @Override
    public void create() {
        Gdx.app.log(TAG, "Create");
        //TODO PUT this on PerspectiveAR
        //Is here because it needs the App listener to be initialized
        mDisplay = new Display(mCamera);
    }

    @Override
    public void dispose() {
        Gdx.app.log(TAG, "OnDispose");
        mDisplay.dispose();
    }

    @Override
    public void render() {
        Gdx.app.log(TAG, "Rendering");
        if (mode == Mode.normal) {
            if (mDeviceCameraController != null) {
                mode = Mode.prepare;
                mDeviceCameraController.prepareCameraAsync();
            }
        }
        if (mode == Mode.prepare) {
            if (mDeviceCameraController != null && mDeviceCameraController.isReady()) {
                mode = Mode.preview;
                mDeviceCameraController.startPreviewAsync();
            }
        }
        mDisplay.render();
        mCamera.update();

    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(TAG, "Resizing");

    }

	@Override
	public void pause() {
        Gdx.app.log(TAG, "OnPause");
        if(mDeviceCameraController!= null && mDeviceCameraController.isReady()) {
            mode = Mode.normal;
            mDeviceCameraController.stopPreviewAsync();
        }
	}

	@Override
	public void resume() {
        Gdx.app.log(TAG,"OnResume");
	}

}
