package com.coredump.synergyar.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.coredump.synergyar.entities.components.Model3DComponent;
import com.coredump.synergyar.entities.components.Position3DComponent;

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
    private Comparator<Entity> mComparator;

    private boolean mIsLoading;

    //private ComponentMapper<Geolocation> mPositions = ComponentMapper.getFor(Geolocation.class);

    public Render(Camera camera, CameraInputController controller,
                  Comparator<Entity> comparator) {
        super(Family.all(Position3DComponent.class, Model3DComponent.class).get());
        Bullet.init();
        mModel3DMapper = ComponentMapper.getFor(Model3DComponent.class);
        mPosition3DMapper = ComponentMapper.getFor(Position3DComponent.class);
        //Might be encapsulated inside the PerspectiveAR class
        mCamera = camera;
        Gdx.app.log(TAG,mCamera.toString()+" "+mCamera.near);
        mController = controller;
        mBatch = new ModelBatch();
        mEnvironment = new Environment();
        mIsLoading = true;
        mInstances = new Array<ModelInstance>();
        mRenderQueue = new Array<Entity>();
        mComparator = comparator;
        mEnvironment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        mEnvironment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
    }

    private void loadInstances() {
        Gdx.app.log(TAG, "Loading instances");
        ModelInstance instance = null;
        Gdx.app.log(TAG, mRenderQueue.size+"");
        //Sorts the entities
        if(mRenderQueue.size > 1) {
            mRenderQueue.sort(mComparator);
        }
        for(Entity entity: mRenderQueue) {
            Model3DComponent model3D = mModel3DMapper.get(entity);
            Position3DComponent position = mPosition3DMapper.get(entity);
            instance = new ModelInstance(model3D.model);
            mInstances.add(instance);
        }
        Gdx.app.log(TAG, mInstances.size+"");
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


    static class GameObject extends ModelInstance implements Disposable {
        public final btCollisionObject body;
        public boolean moving;

        public GameObject (Model model, String node, btCollisionShape shape) {
            super(model, node);
            body = new btCollisionObject();
            body.setCollisionShape(shape);
        }

        @Override
        public void dispose () {
            body.dispose();
        }

        static class Constructor implements Disposable {
            public final Model model;
            public final String node;
            public final btCollisionShape shape;

            public Constructor (Model model, String node, btCollisionShape shape) {
                this.model = model;
                this.node = node;
                this.shape = shape;
            }

            public GameObject construct () {
                return new GameObject(model, node, shape);
            }

            @Override
            public void dispose () {
                shape.dispose();
            }
        }
    }
}
