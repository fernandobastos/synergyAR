package com.coredump.synergyar.ar;

/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 * Created by fabio on 11/15/15.
 */

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
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
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.coredump.synergyar.entities.components.Model3DComponent;
import com.coredump.synergyar.entities.components.Object3DComponent;
import com.coredump.synergyar.entities.systems.Render;

public class Display extends ScreenAdapter{
    private Camera mCamera;
    private final static String TAG = Display.class.getName();
    //Temporal
    private Model mModel;
    private ModelBatch mBatch;
    private Environment mEnvironment;
    private PooledEngine mEngine;
    private AssetManager assets;
    private boolean mLoading;
    public Array<ModelInstance> instances = new Array<ModelInstance>();

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

        assets = new AssetManager();
        assets.load("models/pokemon/johnny_d_wicked_bulbasaur_01.g3dj", Model.class);
        mLoading = true;

        //JsonReader jsonReader = new JsonReader();
        //G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        //mModel = modelLoader.loadModel(Gdx.files.getFileHandle("models/pokemon/johnny_d_wicked_bulbasaur_01.g3dj", Files.FileType.Internal));
        //mModel = assets.get("models/pokemon/johnny_d_wicked_bulbasaur_01.g3dj", Model.class);
       // mInstance = new ModelInstance(mModel);
        //mInstance.transform.scale(0.1f,0.1f,0.1f);
       // mInstance.transform.rotate(1, 0, 0, -90);
        //mInstance.transform.setToTranslation(5, 0, 2);

        ModelBuilder modelBuilder = new ModelBuilder();
        /*mModel = modelBuilder.createBox(5f, 5f, 5f,
                new Material(ColorAttribute.createDiffuse(Color.GOLD)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        ///mInstance = new ModelInstance(mModel);
        */
        //TODO put this in other class
        mEngine = new PooledEngine();
        //TODO just testing
        Entity entity = mEngine.createEntity();
        Model3DComponent squareModel = new Model3DComponent();
        squareModel.model =  modelBuilder.createBox(5f, 5f, 5f,
                new Material(ColorAttribute.createDiffuse(Color.GOLD)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        entity.add(squareModel);
        mEngine.addEntity(entity);
        mEngine.addSystem(new Render(mCamera, new CameraInputController(mCamera)));
        //mEnvironment = new Environment();
        //mEnvironment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        //mEnvironment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
    }

    public void resize(int width, int height) {
        //mViewport.setScreenSize(width, height);
    }

    private void doneLoading() {
        Model ship = assets.get("models/ship/ship.g3db", Model.class);
        for (float x = -5f; x <= 5f; x += 2f) {
            for (float z = -5f; z <= 5f; z += 2f) {
                ModelInstance shipInstance = new ModelInstance(ship);
                shipInstance.transform.setToTranslation(x, 0, z);
                instances.add(shipInstance);
            }
        }
        mLoading = false;
    }

    @Override
    public void render(float deltaTime) {
        Gdx.app.log(TAG, "rendering");
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        mEngine.update(deltaTime);
        /*mBatch.begin(mCamera);
        mBatch.render(mInstance, mEnvironment);
        mBatch.end();*/
    }

    public void dispose() {
        mBatch.dispose();
        mModel.dispose();
    }
}
