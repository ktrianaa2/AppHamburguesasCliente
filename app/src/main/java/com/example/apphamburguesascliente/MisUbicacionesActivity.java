package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.User;
import com.example.apphamburguesascliente.Modelos.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MisUbicacionesActivity extends AppCompatActivity {
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
        // Llamada a la API para obtener los datos del usuario
        obtenerUsuario(idUsuario);
    }

    // Método para obtener datos del usuario mediante la API
    private void obtenerUsuario(int idUsuario) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://9jpn4ctd-8000.use2.devtunnels.ms/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        // Asegúrate de convertir idUsuario a String si tu API espera una cadena
        Call<UserResponse> callUsuario = service.obtenerUsuario(String.valueOf(idUsuario));
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
        boolean ubicacionCasaConfigurada = obtenerUbicacionCasaConfigurada(usuario);
        boolean ubicacionTrabajoConfigurada = obtenerUbicacionTrabajoConfigurada(usuario);
        boolean ubicacionOtraConfigurada = obtenerUbicacionOtraConfigurada(usuario);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Reemplaza el fragmento de la casa en el contenedor según la ubicación configurada
        if (ubicacionCasaConfigurada) {
            BotonUbicacionConfiguradaFragment fragmentCasa = new BotonUbicacionConfiguradaFragment();
            Bundle bundleCasa = new Bundle();
            bundleCasa.putInt("idUsuario", usuario.getIdCliente());
            bundleCasa.putDouble("latitud", convertirStringADouble(usuario.getUbicacion1().getLatitud()));
            bundleCasa.putDouble("longitud", convertirStringADouble(usuario.getUbicacion1().getLongitud()));
            fragmentCasa.setArguments(bundleCasa);
            fragmentTransaction.replace(R.id.fragmentBtnCasa, fragmentCasa);
        } else {
            fragmentTransaction.replace(R.id.fragmentBtnCasa, new BotonUbicacionNoConfiguradaFragment());
        }

        // Reemplaza el fragmento del trabajo en el contenedor según la ubicación configurada
        if (ubicacionTrabajoConfigurada) {
            BotonUbicacionConfiguradaFragment fragmentTrabajo = new BotonUbicacionConfiguradaFragment();
            Bundle bundleTrabajo = new Bundle();
            bundleTrabajo.putInt("idUsuario", usuario.getIdCliente());
            bundleTrabajo.putDouble("latitud", convertirStringADouble(usuario.getUbicacion2().getLatitud()));
            bundleTrabajo.putDouble("longitud", convertirStringADouble(usuario.getUbicacion2().getLongitud()));
            fragmentTrabajo.setArguments(bundleTrabajo);
            fragmentTransaction.replace(R.id.fragmentBtnTrabajo, fragmentTrabajo);
        } else {
            fragmentTransaction.replace(R.id.fragmentBtnTrabajo, new BotonUbicacionNoConfiguradaFragment());
        }

        // Reemplaza el fragmento de otra en el contenedor según la ubicación configurada
        if (ubicacionOtraConfigurada) {
            BotonUbicacionConfiguradaFragment fragmentOtra = new BotonUbicacionConfiguradaFragment();
            Bundle bundleOtra = new Bundle();
            bundleOtra.putInt("idUsuario", usuario.getIdCliente());
            bundleOtra.putDouble("latitud", convertirStringADouble(usuario.getUbicacion3().getLatitud()));
            bundleOtra.putDouble("longitud", convertirStringADouble(usuario.getUbicacion3().getLongitud()));
            fragmentOtra.setArguments(bundleOtra);
            fragmentTransaction.replace(R.id.fragmentBtnOtro, fragmentOtra);
        } else {
            fragmentTransaction.replace(R.id.fragmentBtnOtro, new BotonUbicacionNoConfiguradaFragment());
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
