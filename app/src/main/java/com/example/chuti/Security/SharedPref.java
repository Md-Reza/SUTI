package com.example.chuti.Security;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPref {
    public static final String SHARED_PREF_MAIN="shared_preference_main";
    private static final String PREF_NAME = "app_preferences";
    private static SharedPreferences mSharedPref;
    public static final String NAME = "mysharedpref";

    private SharedPref()
    {

    }
    public static void init(Context context)
    {
        if(mSharedPref == null)
            mSharedPref = context.getSharedPreferences( NAME, Context.MODE_PRIVATE);
    }

    public static String read(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static void remove(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }
}
