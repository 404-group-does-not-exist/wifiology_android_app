package com.example.wifiology;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefs {

    private static String prefix = "com.example.wifiology_";
    public static String username = "USERNAME";
    public static String password = "PASSWORD";

    public static void putString(String key, String val, Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(prefix + key, val);
        editor.commit();
    }

    public static String getString(String key, Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(prefix + key,"");
    }
}
