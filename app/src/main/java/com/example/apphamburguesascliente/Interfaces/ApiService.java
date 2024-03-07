package com.example.apphamburguesascliente.Interfaces;

import com.example.apphamburguesascliente.Modelos.LoginRequest;
import com.example.apphamburguesascliente.Modelos.LoginResponse;
import com.example.apphamburguesascliente.Modelos.RegistroRequest;
import com.example.apphamburguesascliente.Modelos.RegistroResponse;
import com.example.apphamburguesascliente.Modelos.UserResponse;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("producto/listar/")
    Call<JsonObject> obtenerProductos();
    @POST("Login/iniciar_sesion/")
    Call<LoginResponse> iniciarSesion(@Body LoginRequest loginRequest);
    @GET("Login/obtener_usuario/{id_cuenta}/")
    Call<UserResponse> obtenerUsuario(@Path("id_cuenta") String id_cuenta);
    @POST("Login/crear/")
    Call<RegistroResponse> registrarUsuario(@Body RegistroRequest registroRequest);
    @POST("Login/cuentaexist/")
    Call<JsonObject> verificarUsuarioExistente(@Body Map<String, String> usuario);

    @POST("Login/phoneExist/")
    Call<JsonObject> verificarTelefonoExistente(@Body Map<String, String> telefono);

}