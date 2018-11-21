package com.example.ak.rxproject.client;


import com.example.ak.rxproject.model.WorldPopulation;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("tutorial/jsonparsetutorial.txt")
    Observable<WorldPopulation> getWorld();
}
