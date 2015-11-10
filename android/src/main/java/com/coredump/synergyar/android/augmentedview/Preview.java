package com.coredump.synergyar.android.augmentedview;

/**
 * @author fabio
 * @since 0.0.1
 * @version 0.0.1
 * Created by fabio on 11/9/15.
 */
public interface Preview {
    void start();

    void stop();

    void release();

    boolean hasCamera();
}
