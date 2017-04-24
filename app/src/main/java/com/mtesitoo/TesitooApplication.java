package com.mtesitoo;

import android.content.Context;

/**
 * Created by cma on 1/8/17.
 */

public class TesitooApplication extends android.support.multidex.MultiDexApplication {

    private static TesitooApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static TesitooApplication getInstance(){
        return instance;
    }

    public Context getContext(){
        return this.getApplicationContext();
    }
}
