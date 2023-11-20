package com.example.neurapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitData {
    private static Retrofit salidaRetrofit;

    public static Retrofit getInstance(){
        if (salidaRetrofit!=null)
            return salidaRetrofit;
        salidaRetrofit = new Retrofit.Builder()
                .baseUrl("https://apipy-uni-435755b56d9c.herokuapp.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        return salidaRetrofit;
    }
}