package com.coredump.synergyar.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;
import com.coredump.synergyar.entities.components.Model3DComponent;
import com.coredump.synergyar.entities.components.Object3DComponent;
import com.coredump.synergyar.entities.components.Position3DComponent;
import com.coredump.synergyar.util.ZIndexComparator;

import java.util.Comparator;

/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 * Created by fabio on 11/18/15.
 */
public class Render extends IteratingSystem {
    private final static String TAG = Render.class.getName();

    private ComponentMapper<Model3DComponent> mModel3DMapper;
    private ComponentMapper<Position3DComponent> mPosition3DMapper;
    private Array<Entity> mRenderQueue;
    Array<ModelInstance> mInstances;
    private final Camera mCamera;
    private final CameraInputController mController;
    private final ModelBatch mBatch;
    private Environment mEnvironment;

    private boolean mIsLoading;

    //private ComponentMapper<Geolocation> mPositions = ComponentMapper.getFor(Geolocation.class);

    public Render(Camera camera, CameraInputController controller) {
        super(Family.all(Position3DComponent.class, Object3DComponent.class).get());
        mModel3DMapper = ComponentMapper.getFor(Model3DComponent.class);
        mPosition3DMapper = ComponentMapper.getFor(Position3DComponent.class);
        //Might be encapsulated inside the PerspectiveAR class
        mCamera = camera;
        mController = controller;
        mBatch = new ModelBatch();
        mEnvironment = new Environment();
        mIsLoading = true;
        mInstances = new Array<ModelInstance>();
        mRenderQueue = new Array<Entity>();
    }

    private void loadInstances(){
        Gdx.app.log(TAG, "Loading instances");
        ModelInstance instance = null;
        ZIndexComparator comparator = new ZIndexComparator();
        //Sorts the entities
        if(mRenderQueue.size > 1) {
            mRenderQueue.sort(comparator);
        }
        for(Entity entity: mRenderQueue) {
            Model3DComponent model3D = mModel3DMapper.get(entity);
            Position3DComponent position = mPosition3DMapper.get(entity);
            instance = new ModelInstance(model3D.model);
            mInstances.add(instance);
        }

        mIsLoading = false;
    }

    @Override
    public void update(float deltaTime) {
        Gdx.app.log(TAG, "updating system");
        super.update(deltaTime);
        if (mIsLoading) {
            loadInstances();
        }
        mBatch.begin(mCamera);
        mBatch.render(mInstances, mEnvironment);
        mBatch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Gdx.app.log(TAG, "Processing entity");
        mRenderQueue.add(entity);
    }
}
