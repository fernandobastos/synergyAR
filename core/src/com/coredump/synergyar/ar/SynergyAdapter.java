package com.coredump.synergyar.ar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.coredump.synergyar.ar.hardware.DeviceCameraController;
import java.util.concurrent.Semaphore;


/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 * Created by fabio on 11/1/15.
 */
public class SynergyAdapter extends Game {
    private static final String TAG = SynergyAdapter.class.getName();;
    private final DeviceCameraController mDeviceCameraController;
    private Mode mode = Mode.normal;
    private PerspectiveAR mCamera;
    public Semaphore mCanRender = new Semaphore(1);

    public SynergyAdapter(com.coredump.synergyar.ar.hardware.DeviceCameraController cameraControl, PerspectiveAR camera) {
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
        mCamera.fieldOfView = 67;
        mCamera.viewportWidth = Gdx.graphics.getWidth();
        mCamera.viewportHeight = Gdx.graphics.getHeight();
        setScreen(new WorldDisplay(mCamera));
    }

    @Override
    public void dispose() {
        Gdx.app.log(TAG, "OnDispose");
        getScreen().dispose();
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
        if(mCanRender.tryAcquire()) {
            super.render();
            mCanRender.release();
        }
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
