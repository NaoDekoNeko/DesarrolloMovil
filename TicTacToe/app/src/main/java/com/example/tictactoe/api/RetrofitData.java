package com.example.tictactoe.api;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitData {
    private static Retrofit salidaRetrofit;

    public static Retrofit getInstance(){
        if (salidaRetrofit!=null)
            return salidaRetrofit;
        salidaRetrofit = new Retrofit.Builder()
                .baseUrl("https://naodeko.pythonanywhere.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        return salidaRetrofit;
    }
}