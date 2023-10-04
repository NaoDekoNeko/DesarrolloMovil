package com.example.mycontactos20232.API;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitContacto {
    private static Retrofit salidaRetrofit;

    public static Retrofit getInstance() {
        if (salidaRetrofit == null) {
            salidaRetrofit = new Retrofit.Builder().
                    baseUrl("https://fatamaademar.000webhostapp.com/").
                    addConverterFactory(GsonConverterFactory.create()).
                    addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                    build();
        }
        return salidaRetrofit;
    }
    private RetrofitContacto(){}
}