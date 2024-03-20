package com.example.apphamburguesascliente.Interfaces;

import com.example.apphamburguesascliente.Modelos.AvisosModelo;
import com.example.apphamburguesascliente.Modelos.LoginRequest;
import com.example.apphamburguesascliente.Modelos.LoginResponse;
import com.example.apphamburguesascliente.Modelos.Pedido;
import com.example.apphamburguesascliente.Modelos.RegistroRequest;
import com.example.apphamburguesascliente.Modelos.RegistroResponse;
import com.example.apphamburguesascliente.Modelos.RespuestaEmpresa;
import com.example.apphamburguesascliente.Modelos.RolResponse;
import com.example.apphamburguesascliente.Modelos.Sucursal;
import com.example.apphamburguesascliente.Modelos.SucursalResponse;
import com.example.apphamburguesascliente.Modelos.TokenRequest;
import com.example.apphamburguesascliente.Modelos.User;
import com.example.apphamburguesascliente.Modelos.UserResponse;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

    @Multipart
    @POST("cliente/realizar_pedido/{id_cuenta}/")
    Call<JsonObject> realizarPedido(
            @Path("id_cuenta") String id_cuenta,
            @Part("cpuntos") RequestBody puntos,
            @Part("precio") RequestBody precio,
            @Part("tipo_de_pedido") RequestBody tipoPedido,
            @Part("metodo_de_pago") RequestBody metodoPago,
            @Part("estado_del_pedido") RequestBody estadoPedido,
            @Part("id_sucursal") RequestBody idSucursal,
            @Part("latitud") RequestBody latitud,
            @Part("longitud") RequestBody longitud,
            @Part("estado_pago") RequestBody estadoPago,
            @Part("fecha_hora") RequestBody fechaHora,
            @Part("fecha_minutos") RequestBody fechaMinutos,
            @Part("detalles_pedido") RequestBody detallesPedidoJson,
            @Part("imagen_base64") RequestBody imagenBase64
    );

    @FormUrlEncoded
    @POST("Login/editar_ubicacion/{id_cuenta}/")
    Call<UserResponse> actualizarUsuario(
            @Path("id_cuenta") String id_cuenta,
            @Field("latitud1") String latitud1,
            @Field("longitud1") String longitud1,
            @Field("latitud2") String latitud2,
            @Field("longitud2") String longitud2,
            @Field("latitud3") String latitud3,
            @Field("longitud3") String longitud3
    );

    @GET("sucursal/sucusarleslist/")
    Call<SucursalResponse> obtenerSucursales();

    @FormUrlEncoded
    @POST("Login/editar_usuario/{id_cuenta}/")
    Call<UserResponse> actualizarUsuario(
            @Path("id_cuenta") String id_cuenta,
            @Field("snombre") String snombre,
            @Field("capellido") String capellido,
            @Field("ctelefono") String ctelefono,
            @Field("ruc_cedula") String ruc_cedula,
            @Field("crazon_social") String crazon_social
    );

    @GET("avisos/avisos")
    Call<List<AvisosModelo>> getAvisos();
}
