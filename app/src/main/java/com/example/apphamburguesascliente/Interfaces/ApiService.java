package com.example.apphamburguesascliente.Interfaces;

import com.example.apphamburguesascliente.Modelos.LoginRequest;
import com.example.apphamburguesascliente.Modelos.LoginResponse;
import com.example.apphamburguesascliente.Modelos.UserResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("producto/listar/")
    Call<JsonObject> obtenerProductos(); // Cambiado a JsonObject
    @POST("Login/iniciar_sesion/")
    Call<LoginResponse> iniciarSesion(@Body LoginRequest loginRequest);
    @GET("Login/obtener_usuario/{id_cuenta}/")
    Call<UserResponse> obtenerUsuario(@Path("id_cuenta") String id_cuenta);
}