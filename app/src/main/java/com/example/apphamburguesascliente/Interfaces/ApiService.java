package com.example.apphamburguesascliente.Interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("producto/listar/")
    Call<JsonObject> obtenerProductos(); // Cambiado a JsonObject
}