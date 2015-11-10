package com.coredump.synergyar.configuration;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.utils.Logger;

import java.nio.ByteBuffer;

/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 * Created by fabio on 11/1/15.
 */
public class SynergyAdapter implements ApplicationListener {
    private static final String TAG = SynergyAdapter.class.getName();;
    private PerspectiveCamera mPerspectiveCamera;

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
