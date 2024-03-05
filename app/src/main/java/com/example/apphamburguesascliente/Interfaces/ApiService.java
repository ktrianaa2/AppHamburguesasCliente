package com.example.apphamburguesascliente.Interfaces;

import com.example.apphamburguesascliente.Modelos.ProductoModelo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("producto/listar/")
    Call<List<ProductoModelo>> obtenerProductos();

}
