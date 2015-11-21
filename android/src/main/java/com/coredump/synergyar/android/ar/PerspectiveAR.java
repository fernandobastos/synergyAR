package com.coredump.synergyar.android.ar;

import android.content.Context;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.coredump.synergyar.android.sensors.orientation.OrientationSensor;

/**
 * Created by fernando on 08-Nov-15.
 */
public class PerspectiveAR extends PerspectiveCamera{
    public OrientationSensor orientation;
    float [] orientationMatrix;
    float [] lookVector;
    float [] upVector;
    boolean geo ;



    public PerspectiveAR(Context context){
        super();
        orientation = new OrientationSensor(context,1000,true);
        orientation.start();
        geo = false;
    }

    public PerspectiveAR(Context context, float f, int i, int j) {
        super(f,i,j);
        orientation = new OrientationSensor(context,1000,!geo);
        orientation.start();

    }


    public void setPosition(float x,float y,float z){
        this.position.set(x, y, z);
        orientation.setLookAtOffset(x,y,z);
    }

    public void render(){
        this.update();
        orientationMatrix = orientation.getMatrix();
        upVector = orientation.getUp();
        this.up.set(upVector[0],upVector[1], upVector[2]);
        lookVector = orientation.getLookAt();
        this.lookAt(lookVector[0],lookVector[1],lookVector[2]);

    }
    @Override
    public void update() {
        super.update();

    }

    @Override
    public void update(boolean updateFrustum) {
        super.update(updateFrustum);

    }
    public void dispose(){
        orientation.finish();
        orientation = null;
        orientationMatrix = null;
        lookVector = null;
        upVector = null;
    }

    public float[] getOrientationMatrix(){
        return orientationMatrix;
    }

    public float[] geUpVector(){
        return upVector;
    }

    public float[] getLookVector(){
        float [] out = new float[3];
        out[0] = lookVector[0];
        out[1] = lookVector[1];
        out[2] = lookVector[2];
        return out;
    }

    public boolean getStability(){
        return orientation.getStable();
    }
}
