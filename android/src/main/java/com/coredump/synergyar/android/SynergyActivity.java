package com.coredump.synergyar.android;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.coredump.synergyar.Synergy;

import java.io.IOException;

/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 */
public class SynergyActivity extends AndroidApplication {
    private static final String TAG = "SynergyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new Synergy(), config);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "OnDestroy");
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "OnPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "OnResume");
        super.onResume();
    }


}
