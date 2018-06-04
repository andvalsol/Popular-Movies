package com.example.luthiers.popularmovies;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class CinephileApplication extends Application {
    
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }
    
    public static Context getAppContext() {
        return sContext;
    }
}