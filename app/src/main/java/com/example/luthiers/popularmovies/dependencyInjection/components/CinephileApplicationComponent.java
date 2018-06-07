package com.example.luthiers.popularmovies.dependencyInjection.components;

import android.content.Context;

import com.example.luthiers.popularmovies.dependencyInjection.scopes.CinephileApplicationScope;
import com.example.luthiers.popularmovies.dependencyInjection.modules.PicassoModule;
import com.squareup.picasso.Picasso;

import dagger.Component;

@CinephileApplicationScope //Use the CinephileApplicationScope in order to create unique instances of each method inside this interface
@Component(modules = PicassoModule.class)
public interface CinephileApplicationComponent {
    //The CinephileApplication cares about Picasso and Context
    
    Picasso getPicasso();
    
    Context getContext();
}