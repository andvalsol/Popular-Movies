package com.example.luthiers.popularmovies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.example.luthiers.popularmovies.dependencyInjection.components.CinephileApplicationComponent;
import com.example.luthiers.popularmovies.dependencyInjection.components.DaggerCinephileApplicationComponent;
import com.example.luthiers.popularmovies.dependencyInjection.modules.ContextModule;

public class CinephileApplication extends Application {
    
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    
    private CinephileApplicationComponent mCinephileApplicationComponent;
    
    
    public void onCreate() {
        super.onCreate();
    
        //Setup the dependency injection
        mCinephileApplicationComponent = DaggerCinephileApplicationComponent.builder()
                .contextModule(new ContextModule(this))
//                .picassoModule(new PicassoModule()) since only need to add modules that need constructor arguments we can remove this line
                .build(); //Each time I call .build() we create new instances with the desired scope retention
        
        sContext = mCinephileApplicationComponent.getContext();
    }
    
    public static Context getAppContext() {
        return sContext;
    }
    
    //Create a new method so that MainActivity can have access to the CinephileApplicationComponent instance
    
    public CinephileApplicationComponent getCinephileApplicationComponent() {
        return mCinephileApplicationComponent;
    }
    
    public static CinephileApplication get(Activity activity) {
        return (CinephileApplication) activity.getApplication();
    }
}