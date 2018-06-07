package com.example.luthiers.popularmovies.dependencyInjection.modules;

import android.content.Context;

import com.example.luthiers.popularmovies.dependencyInjection.scopes.CinephileApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {
    
    private final Context mContext;
    
    //Since we have an external dependency  we need to get it using a constructor
    public ContextModule(Context context) {
        mContext = context;
    }
    
    //Even though is a single-module module is a singleton it's better to add an scope
    @Provides
    @CinephileApplicationScope //This means that this method cannot be used outside the CinephileApplicationScope which is linked by the CinephileApplicationScope
    public Context context() { //This is an external dependency, because it depends on the system
        return mContext;
    }
}