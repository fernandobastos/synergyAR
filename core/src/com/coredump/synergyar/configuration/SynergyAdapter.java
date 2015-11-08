package com.coredump.synergyar.configuration;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;

import sun.rmi.runtime.Log;

/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 * Created by fabio on 11/1/15.
 */
public class SynergyAdapter implements ApplicationListener {
    private final Logger mLogger = new Logger(getClass().getName());

    //temporal
    SpriteBatch batch;
	Texture img;

	//real fields
    private DeviceCameraController mCameraController;
    private PerspectiveCamera mPerspectiveCamera;
    //@// TODO: 11/3/15 Remove temporal constructor
    public SynergyAdapter() {

    }

    public SynergyAdapter(DeviceCameraController controller) {

    }

    @Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		//@// TODO: 11/3/15 remove this
        Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();

        //realcode
        /*
        if(mCameraController != null) mCameraController.prepareCameraAsync();
        */
	}

	@Override
	public void resize(int width, int height) {
        mLogger.debug("Resize");
	}

	@Override
	public void pause() {
        mLogger.debug("Pause");
	}

	@Override
	public void resume() {
        mLogger.debug("Resume");
	}

	@Override
	public void dispose() {
        mLogger.debug("Dispose");
	}
}
