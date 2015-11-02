package com.coredump.synergyar.android;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.coredump.synergyar.android.R;
import com.coredump.synergyar.android.configuration.Renderer;
import com.coredump.synergyar.android.configuration.camera.CameraControllerKitKat;
/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 */
public class SynergyActivity extends AndroidApplication {
    private int origWidth;
    private int origHeight;
    private FrameLayout main;

    private Renderer renderer;

    public void addView(View view, int index) {
        main.addView(view, index);
    }

    public void addView(View view) {
        main.addView(view);
    }

    public Handler getMainHandler() {
        return this.handler;
    }

    public void post(Runnable r) {
        handler.post(r);
    }

    public void setFixedSize(int width, int height) {
        if (graphics.getView() instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) graphics.getView();
            glView.getHolder().setFormat(PixelFormat.RGBA_8888);
            glView.getHolder().setFixedSize(width, height);
        }
    }

    public void restoreFixedSize() {
        if (graphics.getView() instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) graphics.getView();
            glView.getHolder().setFormat(PixelFormat.RGBA_8888);
            glView.getHolder().setFixedSize(origWidth, origHeight);
        }
    }

    /*@Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new Synergy(), config);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGLSurfaceView20API18= false;
        cfg.r = 8;
        cfg.g = 8;
        cfg.b = 8;
        cfg.a = 8;

        // Camera Part
        CameraControllerKitKat cameraControl = new CameraControllerKitKat(this);

        View view = initializeForView(renderer = new Renderer(this, cameraControl),
                cfg);
        // keep the original screen size
        origWidth = graphics.getWidth();
        origHeight = graphics.getHeight();

        if (graphics.getView() instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) graphics.getView();
            // force alpha channel - I'm not sure we need this as the GL surface
            // is already using alpha channel
            glView.getHolder().setFormat(PixelFormat.RGBA_8888);
        }
        // we don't want the screen to turn off during the long image saving
        // process

        graphics.getView().setKeepScreenOn(true);

        main = (FrameLayout) findViewById(R.id.main_layout);
        main.addView(view, 0);
    }

    @Override
    protected void onDestroy() {
        renderer.dispose();
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}
