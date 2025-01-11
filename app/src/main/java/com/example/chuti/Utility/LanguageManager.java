package com.example.chuti.Utility;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {
    private Context ct;
    public  LanguageManager (Context context){
        ct=context;
    }
    public void updateResource(String code){
        Locale locale=new Locale(code);
        locale.setDefault(locale);
        Resources resources=ct.getResources();
        Configuration configuration=resources.getConfiguration();
        configuration.locale=locale;
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }
}
