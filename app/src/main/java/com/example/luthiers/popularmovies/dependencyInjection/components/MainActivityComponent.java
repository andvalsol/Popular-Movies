package com.example.luthiers.popularmovies.dependencyInjection.components;

import com.example.luthiers.popularmovies.dependencyInjection.modules.MainActivityModule;
import com.example.luthiers.popularmovies.dependencyInjection.scopes.MainActivityScope;
import com.example.luthiers.popularmovies.views.MainActivity;

import dagger.Component;

@MainActivityScope
@Component(modules = MainActivityModule.class, dependencies = CinephileApplicationComponent.class) //dependencies tells Dagger that if there are missing dependencies look there
public interface MainActivityComponent {
    
    void injectMainActivity(MainActivity mainActivity); //Dagger will look at the return type and see that we need to inject things in MainActivity
}