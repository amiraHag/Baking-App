package com.example.android.baking.idlingresource;

import android.app.Application;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.example.android.baking.BuildConfig;

import io.paperdb.Paper;


public class MyCustomApplication extends Application {


    @Nullable private SimpleIdlingResource mIdlingResource;

    // Only called from esspresso test, creates and returns SimpleIdlingResource.
    @VisibleForTesting
    @NonNull
    private SimpleIdlingResource setmIdlingResource() {
        if (mIdlingResource==null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @VisibleForTesting
    @Nullable
    public SimpleIdlingResource getmIdlingResource() {
        if (mIdlingResource==null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    public MyCustomApplication() {
        if (BuildConfig.DEBUG) {
            setmIdlingResource();
        }


    }

    public void setIdleState(boolean idleState) {
        if (mIdlingResource!=null) {
            mIdlingResource.setIdleState(idleState);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //initiate paper database to be available for widget
        Paper.init(this);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}