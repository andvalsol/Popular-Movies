package com.example.luthiers.popularmovies.dependencyInjection.modules;

import android.content.Context;

import com.example.luthiers.popularmovies.dependencyInjection.scopes.CinephileApplicationScope;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class) //We need the ContextModule
public class PicassoModule {
    
    @Provides
    @CinephileApplicationScope //This means that this method cannot be used outside the CinephileApplicationScope which is linked by the CinephileApplicationScope
    public Picasso picasso(Context context) {
        return new Picasso.Builder(context).build();
    }
}