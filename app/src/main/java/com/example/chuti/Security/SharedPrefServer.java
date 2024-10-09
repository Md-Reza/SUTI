package com.example.chuti.Security;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefServer {
    public static final String SHARED_PREF_MAIN="shared_preference_main";
    private static SharedPreferences mSharedServerPref;
    public static final String NAME = "mysharedpref";

    private SharedPrefServer()
    {

    }
    public static void init(Context context)
    {
        if(mSharedServerPref == null)
            mSharedServerPref = context.getSharedPreferences( NAME, Context.MODE_PRIVATE);
    }

    public static String read(String key, String defValue) {
        return mSharedServerPref.getString(key, defValue);
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedServerPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static void remove(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedServerPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }
}
