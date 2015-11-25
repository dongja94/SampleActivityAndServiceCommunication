package com.example.dongja94.sampleactivityandservicecommunication;

import android.app.Application;
import android.content.Context;

/**
 * Created by dongja94 on 2015-11-25.
 */
public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
