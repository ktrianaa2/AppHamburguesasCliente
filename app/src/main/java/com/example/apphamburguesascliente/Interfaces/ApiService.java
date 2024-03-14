package com.example.apphamburguesascliente.Interfaces;

import com.example.apphamburguesascliente.Modelos.DetallesPedido;
import com.example.apphamburguesascliente.Modelos.LoginRequest;
import com.example.apphamburguesascliente.Modelos.LoginResponse;
import com.example.apphamburguesascliente.Modelos.Pedido;
import com.example.apphamburguesascliente.Modelos.RegistroRequest;
import com.example.apphamburguesascliente.Modelos.RegistroResponse;
import com.example.apphamburguesascliente.Modelos.RespuestaEmpresa;
import com.example.apphamburguesascliente.Modelos.RolResponse;
import com.example.apphamburguesascliente.Modelos.SucursalResponse;
import com.example.apphamburguesascliente.Modelos.TokenRequest;
import com.example.apphamburguesascliente.Modelos.UserResponse;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("producto/listar/")
    Call<JsonObject> obtenerProductos();

    @POST("Login/iniciar_sesion/")
    Call<LoginResponse> iniciarSesion(@Body LoginRequest loginRequest);

    @POST("Login/rol/")
    Call<RolResponse> verificarRol(@Body TokenRequest tokenRequest);

    @GET("Login/obtener_usuario/{id_cuenta}/")
    Call<UserResponse> obtenerUsuario(@Path("id_cuenta") String id_cuenta);

    @POST("Login/crear/")
    Call<RegistroResponse> registrarUsuario(@Body RegistroRequest registroRequest);

    @POST("Login/cuentaexist/")
    Call<JsonObject> verificarUsuarioExistente(@Body Map<String, String> usuario);

    @POST("Login/phoneExist/")
    Call<JsonObject> verificarTelefonoExistente(@Body Map<String, String> telefono);

    @POST("empresa/infoEmpresa/")
    Call<RespuestaEmpresa> obtenerInfoEmpresa();

    @FormUrlEncoded
    @POST("sucursal/secSucursal/")
    Call<SucursalResponse> obtenerSucursalPorUbicacion(
            @Field("latitud") String latitud,
            @Field("longitud") String longitud
    );

    @FormUrlEncoded
    @POST("cliente/realizar_pedido/{id_cuenta}/")
    Call<JsonObject> realizarPedido(
            @Path("id_cuenta") String id_cuenta,
            @Field("cpuntos") int puntos,
            @Field("precio") double precio,
            @Field("tipo_de_pedido") String tipoPedido,
            @Field("metodo_de_pago") String metodoPago,
            @Field("estado_del_pedido") String estadoPedido,
            @Field("id_sucursal") int idSucursal,
            @Field("latitud") String latitud,
            @Field("longitud") String longitud,
            @Field("estado_pago") String estadoPago,
            @Field("fecha_hora") String fechaHora,
            @Field("fecha_minutos") String fechaMinutos,
            @Field("detalles_pedido") String detallesPedidoJson // Aquí envías el JSON serializado como String
    );
}
