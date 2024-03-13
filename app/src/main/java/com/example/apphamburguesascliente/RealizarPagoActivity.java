package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.DetallesPedido;
import com.example.apphamburguesascliente.Modelos.Pedido;
import com.example.apphamburguesascliente.Modelos.SucursalResponse;
import com.example.apphamburguesascliente.Modelos.Ubicacion;
import com.example.apphamburguesascliente.Modelos.User;
import com.example.apphamburguesascliente.Modelos.UserResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RealizarPagoActivity extends AppCompatActivity {

    private ApiService apiService;
    private int idCuentaUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_pago);

        apiService = ApiClient.getInstance();
        TimePicker timePicker = findViewById(R.id.timePicker);

        timePicker.setIs24HourView(true);

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

    private void realizarPago() {
        // Obtén los datos necesarios para realizar el pago
        Intent intent = getIntent();
        int idCliente = intent.getIntExtra("idCliente", -1);
        int totalPuntos = intent.getIntExtra("totalPuntos", 0);
        double totalAPagar = intent.getDoubleExtra("totalAPagar", 0.0);
        DetallesPedido detallesPedidoObjeto = (DetallesPedido) intent.getSerializableExtra("detallesPedidoObjeto");

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

                                // Obtener el tipo de pedido seleccionado (R para retiro, D para domicilio)
                                RadioGroup radioGroup = findViewById(R.id.radioGroup);
                                String tipoPedido;
                                if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonOptionRetiro) {
                                    tipoPedido = "R";
                                } else {
                                    tipoPedido = "D";
                                }
                                Log.d("Pago", "Tipo de Pedido: " + tipoPedido);

                                // Obtener el método de pago seleccionado (E para efectivo, T para transferencia, F para fraccionado)
                                Spinner metodosPagoSpinner = findViewById(R.id.metodosPagoSpinner);
                                String metodoPago = metodosPagoSpinner.getSelectedItem().toString().substring(0, 1);
                                Log.d("Pago", "Método de Pago: " + metodoPago);

                                // Hacer la solicitud para obtener la sucursal
                                obtenerSucursal(idCliente, totalPuntos, totalAPagar, tipoPedido, metodoPago, latitud, longitud, detallesPedidoObjeto);
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

    private void obtenerSucursal(int idCliente, int totalPuntos, double totalAPagar, String tipoPedido, String metodoPago, String latitud, String longitud, DetallesPedido detallesPedidoObjeto) {
        apiService.obtenerSucursalPorUbicacion(latitud, longitud).enqueue(new Callback<SucursalResponse>() {
            @Override
            public void onResponse(Call<SucursalResponse> call, Response<SucursalResponse> response) {
                SucursalResponse sucursalResponse = response.body();
                if (sucursalResponse != null && sucursalResponse.getSucursalList() != null && !sucursalResponse.getSucursalList().isEmpty()) {
                    int idSucursal = sucursalResponse.getSucursalList().get(0).getIdSucursal();
                    Log.d("Pago", "ID Sucursal: " + idSucursal);

                    if (idSucursal != 0) {
                        // Llamar a realizarPedido con el ID de la sucursal
                        realizarPedido(idCliente, totalPuntos, totalAPagar, tipoPedido, metodoPago, idSucursal, latitud, longitud, detallesPedidoObjeto);
                    } else {
                        Log.e("Pago", "ID de sucursal inválido: " + idSucursal);
                    }
                } else {
                    Log.e("Pago", "Sucursal no disponible en la respuesta o lista vacía");
                }
            }

            @Override
            public void onFailure(Call<SucursalResponse> call, Throwable t) {
                Log.e("Pago", "Error al obtener la sucursal por ubicación", t);
            }
        });
    }


    private void realizarPedido(int idCliente, int totalPuntos, double totalAPagar, String tipoPedido, String metodoPago, int idSucursal, String latitud, String longitud, DetallesPedido detallesPedidoObjeto) {
        Pedido pedido = new Pedido(idCliente, totalPuntos, totalAPagar, tipoPedido, metodoPago, idSucursal, latitud, longitud, "", "", detallesPedidoObjeto);
        Log.d("Pago", "Datos del Pedido: " + pedido.toString());

        // Log del ID de la cuenta antes de realizar la solicitud
        Log.d("Pago", "ID de la Sucursal: " + idSucursal);

        // Hacer la solicitud para realizar el pedido
        apiService.realizarPedido(String.valueOf(idCuentaUsuario), pedido).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    // Éxito en la solicitud, procesar la respuesta si es necesario
                    Log.d("Pago", "Solicitud de pedido exitosa");
                } else {
                    // Error en la solicitud
                    Log.e("Pago", "Error al realizar el pedido: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Pago", "Error al realizar el pedido", t);
            }
        });
    }
}
