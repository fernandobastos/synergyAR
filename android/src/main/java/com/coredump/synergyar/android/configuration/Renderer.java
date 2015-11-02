package com.coredump.synergyar.android.configuration;


import java.util.concurrent.Semaphore;

import android.content.Context;
import android.os.Looper;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.coredump.synergyar.android.configuration.camera.CameraHandler;

public class Renderer implements ApplicationListener {

    public enum Mode {
        normal, prepare, preview
    }

    private Mode mode = Mode.normal;
    private final CameraHandler deviceCameraControl;
    public Screen screen;
    private Context context;
    private InputMultiplexer multiplexer;
    public Semaphore canRender = new Semaphore(1);

    public Renderer(Context context, CameraHandler cameraControl) {
        this.deviceCameraControl = cameraControl;
        this.context = context;
    }

    @Override
    public void create() {
        Looper.prepare();
        //screen = new Screen(context);
        //screen.init(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //multiplexer = new InputMultiplexer();
        //multiplexer.addProcessor(screen.getGestureDetector());
        Gdx.input.setInputProcessor(multiplexer);

    }

    @Override
    public void render() {
        if (mode == Mode.normal) {
            mode = Mode.prepare;
            if (deviceCameraControl != null) {
                System.out.println("Camera-Prepare");
                deviceCameraControl.prepareCameraAsync();
            }
        }
        //Gdx.gl30.glHint(GL30.GL_PERSPECTIVE_CORRECTION_HINT, GL30.GL_NICEST);
        if (mode == Mode.prepare) {
            Gdx.gl30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            if (deviceCameraControl != null) {
                if (deviceCameraControl.isReady()) {
                    System.out.println("Camera-Preview");
                    deviceCameraControl.startPreviewAsync();
                    mode = Mode.preview;
                }
            }
        } else if (mode == Mode.preview) {
            Gdx.gl30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        } else { // mode = normal
            Gdx.gl30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        }
        // ----------------------------------------------------------------------------//
        Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        Gdx.gl30.glEnable(GL30.GL_DEPTH_TEST);
        Gdx.gl30.glEnable(GL30.GL_TEXTURE);
        Gdx.gl30.glEnable(GL30.GL_TEXTURE_2D);
        //Gdx.gl30.glEnable(GL30.GL_LINE_SMOOTH);
        Gdx.gl30.glDepthFunc(GL30.GL_LEQUAL);
        Gdx.gl30.glClearDepthf(1.0F);
        //Gdx.gl30.glEnable(GL30.GL_ALPHA_TEST);
        //Gdx.gl30.glDisable(GL30.GL_COLOR_MATERIAL);
       // Gdx.gl30.glAlphaFunc(GL30.GL_GREATER, 0.0f);
        Gdx.gl30.glEnable(GL30.GL_BLEND);
        Gdx.gl30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        if (canRender.tryAcquire()) {
            //((LibGDXPerspectiveCamera) screen.getCamera()).render();
            screen.render(2);
            //screen.render();
            canRender.release();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        deviceCameraControl.stopPreviewAsync();
        mode = Mode.normal;
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        if (multiplexer != null) {
            multiplexer.clear();
            multiplexer = null;
        }
        canRender = null;
    }
}

