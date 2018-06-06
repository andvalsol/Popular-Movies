package com.example.luthiers.popularmovies.repository.network.models;

public enum Status {
    SUCCESS,
    ERROR,
    LOADING;
    
    public final boolean isSuccessful() {
        return (Status)this == SUCCESS;
    }
    
    public final boolean isLoading() {
        return (Status)this == LOADING;
    }
}