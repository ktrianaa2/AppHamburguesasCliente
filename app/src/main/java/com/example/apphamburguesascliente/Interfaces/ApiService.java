package com.example.apphamburguesascliente.Interfaces;

import com.example.apphamburguesascliente.Modelos.LoginRequest;
import com.example.apphamburguesascliente.Modelos.LoginResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @GET("producto/listar/")
    Call<JsonObject> obtenerProductos(); // Cambiado a JsonObject

    @POST("Login/iniciar_sesion/")
    Call<LoginResponse> iniciarSesion(@Body LoginRequest loginRequest);
}