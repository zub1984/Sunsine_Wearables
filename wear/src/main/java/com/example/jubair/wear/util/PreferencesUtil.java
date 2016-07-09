package com.example.jubair.wear.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class PreferencesUtil {

    public static void savePrefs(Context context, String key, int value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static void savePrefs(Context context, String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getPrefs(Context context, String key, String defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, defaultValue);
    }

    public static int getPrefs(Context context, String key, int defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(key, defaultValue);
    }

}
