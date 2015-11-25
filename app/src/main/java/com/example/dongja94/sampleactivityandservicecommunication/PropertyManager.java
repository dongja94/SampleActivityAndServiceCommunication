package com.example.dongja94.sampleactivityandservicecommunication;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by dongja94 on 2015-11-25.
 */
public class PropertyManager {
    private static PropertyManager instance;
    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PropertyManager() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        mEditor = mPrefs.edit();
    }

    private static final String FIELD_IS_ROUTING = "isrouting";
    public boolean isRouting() {
        return mPrefs.getBoolean(FIELD_IS_ROUTING, false);
    }
    public void setRouting(boolean isRouting) {
        mEditor.putBoolean(FIELD_IS_ROUTING, isRouting);
        mEditor.commit();
    }

    private static final String FIELD_END_LAT = "endlat";
    private static final String FIELD_END_LNG = "endlng";

    public double getEndLat() {
        return mPrefs.getFloat(FIELD_END_LAT, 0);
    }
    public double getEndLng() {
        return mPrefs.getFloat(FIELD_END_LNG, 0);
    }

    public void setEnd(double lat, double lng) {
        mEditor.putFloat(FIELD_END_LAT, (float)lat);
        mEditor.putFloat(FIELD_END_LNG, (float)lng);
        mEditor.commit();
    }

}
