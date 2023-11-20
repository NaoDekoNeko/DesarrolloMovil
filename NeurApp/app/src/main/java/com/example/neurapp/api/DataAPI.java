package com.example.neurapp.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DataAPI {
    @GET("apipy/{x0}/{x1}/{x2}/{x3}/{x4}/{x5}/{x6}/{x7}/0")
    Call<ResponseBody> getPredict(@Path("x0") int x0, @Path("x1") int x1, @Path("x2") int x2,
                                  @Path("x3") int x3, @Path("x4") int x4, @Path("x5") int x5,
                                  @Path("x6") int x6, @Path("x7") int x7);
}
