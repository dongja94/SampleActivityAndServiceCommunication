package com.example.dongja94.sampleactivityandservicecommunication;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

public class RouteService extends Service {
    private static final String TAG = "RouteService";
    public RouteService() {
    }

    Handler mHandler = new Handler(Looper.getMainLooper());


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    RemoteCallbackList<IRouteCallback> mCallbacks = new RemoteCallbackList<IRouteCallback>();

    IRouteService.Stub mBinder = new IRouteService.Stub() {
        @Override
        public boolean startRouting(double lat, double lng, int endCount) throws RemoteException {
            return RouteService.this.startRouting(lat, lng, endCount);
        }

        @Override
        public boolean registerCallback(IRouteCallback callback) throws RemoteException {
            return mCallbacks.register(callback);
        }

        @Override
        public boolean unregisterCallback(IRouteCallback callback) throws RemoteException {
            return mCallbacks.unregister(callback);
        }
    };

    boolean startRouting(double lat, double lng, int endCount) {
        if (PropertyManager.getInstance().isRouting()) {
            return false;
        }
        mLat = lat;
        mLng = lng;
        mEndCount = endCount;
        PropertyManager.getInstance().setRouting(true);
        mHandler.post(mRouting);
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            if (PropertyManager.getInstance().isRouting()) {
                restartRouting();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    double mLat, mLng;
    int mEndCount;
    int mCount = 0;

    private void restartRouting() {
        mLat = PropertyManager.getInstance().getEndLat();
        mLng = PropertyManager.getInstance().getEndLng();
        mHandler.post(mRouting);
    }

    Runnable mRouting = new Runnable() {
        @Override
        public void run() {
            if (PropertyManager.getInstance().isRouting()) {
                mCount++;
                Log.i(TAG, "lat, lng, count : " + mLat + "," + mLng + "," + mCount);
                int count = mCallbacks.beginBroadcast();
                for (int i = 0; i < count; i++) {
                    IRouteCallback callback = mCallbacks.getBroadcastItem(i);
                    try {
                        callback.onRouteChange(mCount);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                mCallbacks.finishBroadcast();
                mHandler.postDelayed(this, 3000);
                if (mCount == mEndCount) {
                    Log.i(TAG, "routing finish");
                    PropertyManager.getInstance().setRouting(false);
                    mCount = 0;
                    stopSelf();
                }
            }
        }
    };
}
