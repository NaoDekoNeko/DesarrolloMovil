package com.example.mycontactos20232.API;

import com.example.mycontactos20232.Contacto;

import java.lang.ref.Reference;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.DELETE;

public interface ContactoAPI {
    @GET("listarcontacto")
    Observable<List<Contacto>> getContacto();
    @POST("guardarcontacto")
    Call<Contacto> setContacto(@Body Contacto contacto);

    @PUT("actualizarcontacto/{id}")
    Call<Void> updateContacto(@Path("id") int id, @Body Contacto contacto);

    @DELETE("borrarcontacto/{id}")
    Call<Void> deleteContacto(@Path("id") int id);
}
