package com.coredump.synergyar.entities.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;

/**
 * Created by fernando on 09-Dec-15.
 */
public class CollisionComponent implements Component {
    public btCollisionConfiguration collisionConfig;
    public btDispatcher dispatcher;
    public ContactListener contactListener;
    public btBroadphaseInterface broadphase;
    public btCollisionWorld collisionWorld;

}
