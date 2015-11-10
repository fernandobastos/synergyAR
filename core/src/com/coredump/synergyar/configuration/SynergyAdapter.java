package com.coredump.synergyar.configuration;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;



/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 * Created by fabio on 11/1/15.
 */
public class SynergyAdapter implements ApplicationListener {
    private static final String TAG = SynergyAdapter.class.getName();;
    private final DeviceCameraController mDeviceCameraController;
    private Mode mode = Mode.normal;

    public SynergyAdapter(DeviceCameraController cameraControl) {
        mDeviceCameraController = cameraControl;
    }

    public enum Mode {
        normal,
        prepare,
        preview
    }

    @Override
    public void create() {
        Gdx.app.debug(TAG,"OnCreate");

    }

    @Override
    public void dispose() {
        Gdx.app.log(TAG, "OnDispose");
        //texture.dispose();
        //for (int i=0;i<6;i++) {
        //    mesh[i].dispose();
        //    mesh[i] = null;
        //}
        // texture = null;
    }

    @Override
    public void render() {
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
