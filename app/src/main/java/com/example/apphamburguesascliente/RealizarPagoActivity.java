package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.SucursalResponse;
import com.example.apphamburguesascliente.Modelos.Ubicacion;
import com.example.apphamburguesascliente.Modelos.User;
import com.example.apphamburguesascliente.Modelos.UserResponse;
import com.example.apphamburguesascliente.PagoEfectivoFragment;
import com.example.apphamburguesascliente.PagoFranccionadoFragment;
import com.example.apphamburguesascliente.PagoTransferenciaFragment;
import com.example.apphamburguesascliente.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RealizarPagoActivity extends AppCompatActivity {

    private ApiService apiService;
    private int idCuentaUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_pago);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://9jpn4ctd-8000.use2.devtunnels.ms/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // Inicializa el Spinner y define las opciones
        Spinner metodosPagoSpinner = findViewById(R.id.metodosPagoSpinner);
        String[] opcionesMetodoPago = {"Transferencia", "Efectivo", "Fraccionado"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesMetodoPago);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metodosPagoSpinner.setAdapter(adapter);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        idCuentaUsuario = sharedPreferences.getInt("id_cuenta", -1); // -1 es un valor por defecto en caso de que no se encuentre nada


        Button btnPagar = findViewById(R.id.pagarButton);
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarPago();
            }
        });

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Verifica qué radio button fue seleccionado y muestra un log correspondiente
                if (checkedId == R.id.radioButtonOptionRetiro) {
                    Log.d("RealizarPagoActivity", "Tipo de pedido: R (Retiro)");
                } else if (checkedId == R.id.radioButtonOptionDomicilio) {
                    Log.d("RealizarPagoActivity", "Tipo de pedido: D (Domicilio)");
                }
            }
        });

        // Maneja la selección del Spinner
        metodosPagoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obtiene el fragmento seleccionado
                Fragment fragment = obtenerFragmentoSegunSeleccion(position);

                // Reemplaza el fragmento actual con el nuevo fragmento
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit();

                // Muestra la abreviatura en la consola
                mostrarAbreviatura(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No es necesario hacer nada aquí
            }
        });

        // Por defecto, muestra el fragmento de Transferencia
        Fragment defaultFragment = obtenerFragmentoSegunSeleccion(0);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, defaultFragment)
                .commit();


        // Establecer por defecto que el radioButtonOptionRetiro esté marcado
        RadioButton radioButtonRetiro = findViewById(R.id.radioButtonOptionRetiro);
        radioButtonRetiro.setChecked(true);
    }

    // Método para obtener el fragmento según la posición seleccionada en el Spinner
    private Fragment obtenerFragmentoSegunSeleccion(int position) {
        switch (position) {
            case 0:
                return new PagoTransferenciaFragment();
            case 1:
                return new PagoEfectivoFragment();
            case 2:
                return new PagoFranccionadoFragment();
            default:
                return new PagoTransferenciaFragment();
        }
    }

    // Método para mostrar la abreviatura en la consola
    private void mostrarAbreviatura(int position) {
        switch (position) {
            case 0:
                Log.d("RealizarPagoActivity", "Tipo de pago seleccionado: T (Transferencia)");
                break;
            case 1:
                Log.d("RealizarPagoActivity", "Tipo de pago seleccionado: E (Efectivo)");
                break;
            case 2:
                Log.d("RealizarPagoActivity", "Tipo de pago seleccionado: F (Fraccionado)");
                break;
        }
    }
    private void realizarPago() {
        if (idCuentaUsuario != -1) {
            apiService.obtenerUsuario(String.valueOf(idCuentaUsuario)).enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    UserResponse usuarioResponse = response.body();
                    if (usuarioResponse != null) {
                        User usuario = usuarioResponse.getUsuario();
                        if (usuario != null) {
                            Ubicacion ubicacion = usuario.getUbicacion1();
                            if (ubicacion != null) {
                                String latitud = ubicacion.getLatitud();
                                String longitud = ubicacion.getLongitud();
                                Log.d("Pago", "Latitud: " + latitud + ", Longitud: " + longitud);
                                // Aquí realizas la nueva solicitud a la API con la latitud y longitud
                                obtenerSucursal(latitud, longitud);
                            } else {
                                Log.e("Pago", "Ubicación no disponible en la respuesta del usuario");
                            }
                        } else {
                            Log.e("Pago", "Usuario no disponible en la respuesta");
                        }
                    } else {
                        Log.e("Pago", "Respuesta de usuario nula");
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Log.e("Pago", "Error al obtener la ubicación del usuario", t);
                }
            });
        } else {
            Log.e("Pago", "ID de usuario no disponible");
        }
    }

    private void obtenerSucursal(String latitud, String longitud) {
        apiService.obtenerSucursalPorUbicacion(latitud, longitud).enqueue(new Callback<SucursalResponse>() {
            @Override
            public void onResponse(Call<SucursalResponse> call, Response<SucursalResponse> response) {
                SucursalResponse sucursalResponse = response.body();
                if (sucursalResponse != null && sucursalResponse.getSucursalList() != null && !sucursalResponse.getSucursalList().isEmpty()) {
                    // Suponiendo que sucursalList es una lista y queremos la primera
                    int idSucursal = sucursalResponse.getSucursalList().get(0).getIdSucursal();
                    Log.d("Pago", "ID Sucursal: " + idSucursal);
                } else {
                    Log.e("Pago", "Sucursal no disponible en la respuesta");
                }
            }

            @Override
            public void onFailure(Call<SucursalResponse> call, Throwable t) {
                Log.e("Pago", "Error al obtener la sucursal por ubicación", t);
            }
        });
    }
}
