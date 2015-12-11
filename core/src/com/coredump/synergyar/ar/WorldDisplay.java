package com.coredump.synergyar.ar;

/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 * Created by fabio on 11/15/15.
 */

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.coredump.synergyar.entities.components.Model3DComponent;
import com.coredump.synergyar.entities.components.Position3DComponent;
import com.coredump.synergyar.entities.systems.RenderSystem;
import com.coredump.synergyar.util.ZCoordComparator;

public class WorldDisplay extends ScreenAdapter{
    private final static String TAG = WorldDisplay.class.getName();

    private PerspectiveAR mCamera;
    private ModelBatch mBatch;
    private PooledEngine mEngine;

    // TODO check if this should go on the geolocation component system
    //This contains the location, rotation and scale the model should be rendered at.
    //By default this is at (0,0,0)
    private ModelInstance mInstance;

    public WorldDisplay(PerspectiveAR camera) {
        Gdx.app.log(TAG, "Constructor");
        mCamera = camera;
        initialize();
    }

    public void setCamera(PerspectiveAR camera) {
        this.mCamera = camera;
    }
    public PerspectiveAR getCamera() {
        return this.mCamera;
    }

    private void initialize() {
        Gdx.app.log(TAG, "Init");
        mCamera.far = 1000.0f;
        mCamera.near = 0.1f;
        mCamera.position.set(0.0f, 0.0f, 0.0f);
        mCamera.update();
        //TODO this is just POC code
        mBatch = new ModelBatch();
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
        Entity entity = null;
        Model3DComponent squareModel=null;
        Position3DComponent position = null;

        entity = mEngine.createEntity();
        squareModel = mEngine.createComponent(Model3DComponent.class);
        position = mEngine.createComponent(Position3DComponent.class);

        squareModel.model =  modelBuilder.createBox(5f, 5f, 5f,
                new Material(ColorAttribute.createDiffuse(Color.GOLD)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        position.currentPosition = new Vector3(2f,2f,2f);
        entity.add(squareModel);
        entity.add(position);
        mEngine.addEntity(entity);

        entity = mEngine.createEntity();
        squareModel = mEngine.createComponent(Model3DComponent.class);
        position = mEngine.createComponent(Position3DComponent.class);
        squareModel.model =  modelBuilder.createBox(4f, 8f, 7f,
                new Material(ColorAttribute.createDiffuse(Color.BLACK)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        entity.add(squareModel);
        entity.add(position);
        position.currentPosition = new Vector3(9f,5f,1f);
        mEngine.addEntity(entity);
        EntitySystem system = new RenderSystem(mCamera, new CameraInputController(mCamera), new ZCoordComparator());

        mEngine.addSystem(system);
    }

    public void onSurfaceChanged(int width, int height) {
        mCamera.fieldOfView = 30f;
        mCamera.viewportWidth = width;
        mCamera.viewportHeight = height;
        mCamera.far = 1000.0f;
        mCamera.near = 0.1f;
        mCamera.position.set(0.0f, 0.0f, 0.0f);
    }

    public void resize(int width, int height) {
        //mViewport.setScreenSize(width, height);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.app.log(TAG, "Rendering");
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        mEngine.update(deltaTime);
    }

    public void dispose() {
        mBatch.dispose();
        mCamera.dispose();
        //mEngine = null;
    }
}
