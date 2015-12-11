package com.coredump.synergyar.entities.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;

/**
 * Created by fernando on 09-Dec-15.
 */
public class CollisionSystem extends EntitySystem {
    final static short GROUND_FLAG = 1<<8;
    final static short OBJECT_FLAG = 1<<9;
    final static short ALL_FLAG = -1;

    public void init(){
        Bullet.init();
    }

    public btCollisionWorld getCollisionWorld(){
        btDefaultCollisionConfiguration collisionConfig = new btDefaultCollisionConfiguration();
        btCollisionDispatcher dispatcher = new btCollisionDispatcher(collisionConfig);
        btDbvtBroadphase broadphase = new btDbvtBroadphase();
        return new btCollisionWorld(dispatcher, broadphase, collisionConfig);
    }
    //

}
