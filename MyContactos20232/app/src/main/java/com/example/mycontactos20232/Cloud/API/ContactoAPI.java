package com.example.mycontactos20232.Cloud.API;

import com.example.mycontactos20232.Contacto;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ContactoAPI {
    @GET("listarcontacto")
    Observable<List<Contacto>> getContacto();
    @POST("guardarcontacto")
    Call<Void> setContacto(@Body Contacto contacto);

    @POST("actualizarcontacto")
    Call<Void> updateContacto(@Body Contacto contacto);

    @POST("borrarcontacto")
    Call<Void> deleteContacto(@Body Contacto contacto);
}