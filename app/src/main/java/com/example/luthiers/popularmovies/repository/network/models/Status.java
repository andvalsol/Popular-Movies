package com.example.luthiers.popularmovies.repository.network.models;

/*
* We could set this enum as a public class of integer for better memory performance, this will be done in the
* II Stage TODO change Status enum to a class of integers with a private constructor
* */
public enum Status {
    SUCCESS,
    ERROR,
    LOADING
}