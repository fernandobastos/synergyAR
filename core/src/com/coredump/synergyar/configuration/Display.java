package com.coredump.synergyar.configuration;

/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 * Created by fabio on 11/15/15.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Display {
    private Camera mCamera;
    private final static String TAG = Display.class.getName();
    //Temporal
    private Model mModel;
    private ModelBatch mBatch;
    private Environment mEnvironment;

    // TODO check if this should go on the geolocation component system
    //This contains the location, rotation and scale the model should be rendered at.
    //By default this is at (0,0,0)
    private ModelInstance mInstance;

    public Display(Camera camera) {
        Gdx.app.log(TAG, "Constructor");
        mCamera = camera;
        initialize();
    }

    private void initialize() {
        Gdx.app.log(TAG, "Init");

        //TODO this is just POC code
        mBatch = new ModelBatch();
        mCamera.position.set(10f, 10f, 10f);
        mCamera.lookAt(0, 0, 0);
        mCamera.near = 1f;
        mCamera.far = 300f;
        mCamera.update();

        ModelBuilder modelBuilder = new ModelBuilder();
        mModel = modelBuilder.createBox(5f, 5f, 5f,
                new Material(ColorAttribute.createDiffuse(Color.GOLD)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        mInstance = new ModelInstance(mModel);

        mEnvironment = new Environment();
        mEnvironment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        mEnvironment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
    }

    public void resize(int width, int height) {
        //mViewport.setScreenSize(width, height);
    }

    public void render() {
        Gdx.app.log(TAG, "rendering");
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        mBatch.begin(mCamera);
        mBatch.render(mInstance, mEnvironment);
        mBatch.end();
    }

    public void dispose() {
        mBatch.dispose();
        mModel.dispose();
    }
}
