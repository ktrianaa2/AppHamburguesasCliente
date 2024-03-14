package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.User;
import com.example.apphamburguesascliente.Modelos.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MisUbicacionesActivity extends AppCompatActivity {


    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_ubicaciones);

        int idUsuario = getIntent().getIntExtra("idUsuario", 0);
        Log.d("MisUbicacionesActivity", "ID de usuario obtenida: " + idUsuario);

        ImageView imageViewFlecha = findViewById(R.id.flechaRetroceder);
        imageViewFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual y regresa a la anterior
            }
        });

        obtenerUsuario(idUsuario);
    }



    // Método para obtener datos del usuario mediante la API
    private void obtenerUsuario(int idUsuario) {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        int idCuenta = sharedPreferences.getInt("id_cuenta", 0); // 0 is the default value if not found

        // Verifica si idCuenta es 0, lo que significa que no se encontró o se guardó
        if (idCuenta == 0) {
            Log.e("PerfillFragment", "id_cuenta no encontrado en SharedPreferences.");
            return; // No continuar si no tenemos un id_cuenta válido
        }

        apiService = ApiClient.getInstance();

        // Make sure to convert idUsuario to String if your API expects a string
        Call<UserResponse> callUsuario = apiService.obtenerUsuario(String.valueOf(idUsuario));
        callUsuario.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    User usuario = response.body().getUsuario();
                    cargarFragmento(usuario);
                } else {
                    Log.e("Error", "Error al obtener los datos del usuario: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Error", "Error en la solicitud de obtención de usuario: " + t.getMessage());
            }
        });
    }


    // Método para cargar el fragmento con la información de ubicación
    private void cargarFragmento(User usuario) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        boolean ubicacionCasaConfigurada = obtenerUbicacionCasaConfigurada(usuario);
        boolean ubicacionTrabajoConfigurada = obtenerUbicacionTrabajoConfigurada(usuario);
        boolean ubicacionOtraConfigurada = obtenerUbicacionOtraConfigurada(usuario);

        // Constantes para tipos de ubicación
        int TIPO_CASA = 1;
        int TIPO_TRABAJO = 2;
        int TIPO_OTRO = 3;

        // Crear instancias de fragmentos después de obtener los datos del usuario
        if (ubicacionCasaConfigurada) {
            BotonUbicacionConfiguradaFragment fragmentCasa = new BotonUbicacionConfiguradaFragment();
            Bundle bundleCasa = new Bundle();
            bundleCasa.putInt("tipoUbicacion", TIPO_CASA);
            bundleCasa.putDouble("latitud", convertirStringADouble(usuario.getUbicacion1().getLatitud()));
            bundleCasa.putDouble("longitud", convertirStringADouble(usuario.getUbicacion1().getLongitud()));
            fragmentCasa.setArguments(bundleCasa);
            fragmentTransaction.replace(R.id.fragmentBtnCasa, fragmentCasa);
        } else {
            Bundle bundleCasa2 = new Bundle();
            bundleCasa2.putInt("tipoUbicacion", TIPO_CASA);
            BotonUbicacionNoConfiguradaFragment fragmentCasa = new BotonUbicacionNoConfiguradaFragment();
            fragmentCasa.setArguments(bundleCasa2);
            fragmentTransaction.replace(R.id.fragmentBtnCasa, fragmentCasa);
        }

        if (ubicacionTrabajoConfigurada) {
            BotonUbicacionConfiguradaFragment fragmentTrabajo = new BotonUbicacionConfiguradaFragment();
            Bundle bundleTrabajo = new Bundle();
            bundleTrabajo.putInt("tipoUbicacion", TIPO_TRABAJO);
            bundleTrabajo.putDouble("latitud", convertirStringADouble(usuario.getUbicacion2().getLatitud()));
            bundleTrabajo.putDouble("longitud", convertirStringADouble(usuario.getUbicacion2().getLongitud()));
            fragmentTrabajo.setArguments(bundleTrabajo);
            fragmentTransaction.replace(R.id.fragmentBtnTrabajo, fragmentTrabajo);
        } else {
            Bundle bundleTrabajo2 = new Bundle();
            bundleTrabajo2.putInt("tipoUbicacion", TIPO_TRABAJO);
            BotonUbicacionNoConfiguradaFragment fragmentTrabajo = new BotonUbicacionNoConfiguradaFragment();
            fragmentTrabajo.setArguments(bundleTrabajo2);
            fragmentTransaction.replace(R.id.fragmentBtnTrabajo, fragmentTrabajo);
        }

        if (ubicacionOtraConfigurada) {
            BotonUbicacionConfiguradaFragment fragmentOtro = new BotonUbicacionConfiguradaFragment();
            Bundle bundleOtro = new Bundle();
            bundleOtro.putInt("tipoUbicacion", TIPO_OTRO);
            bundleOtro.putDouble("latitud", convertirStringADouble(usuario.getUbicacion3().getLatitud()));
            bundleOtro.putDouble("longitud", convertirStringADouble(usuario.getUbicacion3().getLongitud()));
            fragmentOtro.setArguments(bundleOtro);
            fragmentTransaction.replace(R.id.fragmentBtnOtro, fragmentOtro);
        } else {
            Bundle bundleOtro2 = new Bundle();
            bundleOtro2.putInt("tipoUbicacion", TIPO_OTRO);
            BotonUbicacionNoConfiguradaFragment fragmentOtro = new BotonUbicacionNoConfiguradaFragment();
            fragmentOtro.setArguments(bundleOtro2);
            fragmentTransaction.replace(R.id.fragmentBtnOtro, fragmentOtro);
        }

        fragmentTransaction.commit();
    }



    // Método para convertir String a Double y manejar posibles excepciones
    private double convertirStringADouble(String valor) {
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            // Manejar la excepción según tus necesidades (puede imprimir un mensaje de error, lanzar una excepción personalizada, etc.)
            e.printStackTrace();
            return 0.0; // Valor por defecto en caso de error
        }
    }


    // Lógica para obtener la configuración de cada ubicación (casa, trabajo, otra)
    private boolean obtenerUbicacionCasaConfigurada(User usuario) {
        // Verifica si la ubicación de casa está configurada
        return usuario.getUbicacion1() != null && usuario.getUbicacion1().getLatitud() != null && usuario.getUbicacion1().getLongitud() != null;
    }
    private boolean obtenerUbicacionTrabajoConfigurada(User usuario) {
        // Verifica si la ubicación de trabajo está configurada
        return usuario.getUbicacion2() != null && usuario.getUbicacion2().getLatitud() != null && usuario.getUbicacion2().getLongitud() != null;
    }
    private boolean obtenerUbicacionOtraConfigurada(User usuario) {
        // Verifica si la otra ubicación está configurada
        return usuario.getUbicacion3() != null && usuario.getUbicacion3().getLatitud() != null && usuario.getUbicacion3().getLongitud() != null;
    }
}
