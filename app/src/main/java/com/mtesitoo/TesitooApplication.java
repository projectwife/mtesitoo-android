package com.mtesitoo;

import android.app.Application;
import android.content.Context;

/**
 * Created by cma on 1/8/17.
 */

public class TesitooApplication extends Application {

    private static TesitooApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public TesitooApplication getInstance(){
        return instance;
    }

    public Context getContext(){
        return this.getApplicationContext();
    }
}
